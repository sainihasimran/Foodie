package com.cegep.foodie.ui.chat;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.cegep.foodie.R;
import com.cegep.foodie.model.Message;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String room = getIntent().getStringExtra("room");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference roomDbReference = FirebaseDatabase.getInstance().getReference().child("messages").child(room);

        EditText messageInput = findViewById(R.id.messageEditText);
        findViewById(R.id.send_btn).setOnClickListener(v -> {
            String messageText = messageInput.getText().toString();
            if (TextUtils.isEmpty(messageText)) {
                Toast.makeText(this, "Message Empty", Toast.LENGTH_SHORT).show();
                return;
            }

            Message message = new Message(currentUser.getEmail(), messageText, System.currentTimeMillis());
            roomDbReference.push().setValue(message);
            messageInput.setText("");
        });

        Query query = roomDbReference.limitToLast(100);
        FirebaseRecyclerOptions<Message> options = new FirebaseRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .setLifecycleOwner(this)
                .build();

        FirebaseRecyclerAdapter<Message, MessageHolder> adapter = new FirebaseRecyclerAdapter<Message, MessageHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MessageHolder holder, int position, @NonNull Message model) {
                holder.bind(model);
            }

            @NonNull
            @Override
            public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(ChatActivity.this).inflate(R.layout.item_message, parent, false);
                return new MessageHolder(view, currentUser);
            }
        };

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter.registerAdapterDataObserver(new ScrollToBottomObserver(recyclerView, adapter, linearLayoutManager));
        recyclerView.setAdapter(adapter);
    }
}