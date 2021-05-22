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

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        EditText messageInput = findViewById(R.id.messageEditText);
        findViewById(R.id.send_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageInput.getText().toString();
                if (TextUtils.isEmpty(messageText)) {
                    Toast.makeText(ChatActivity.this, "Message Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                // TODO: 2021-05-22 generate id here
                Message message = new Message(currentUser.getEmail(), messageText, "", System.currentTimeMillis());
            }
        });
    }
}