package com.cegep.foodie.ui.chat;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.cegep.foodie.R;
import com.cegep.foodie.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    }
}