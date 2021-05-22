package com.cegep.foodie.ui.chat;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.RecyclerView;
import com.cegep.foodie.R;
import com.cegep.foodie.model.Message;
import com.google.firebase.auth.FirebaseUser;

public class MessageHolder extends RecyclerView.ViewHolder {

    private final TextView foreignFromText;
    private final TextView foreignMessage;
    private final Group foreignMessageGroup;

    private final TextView selfMessage;
    private final Group selfMessageGroup;

    private final FirebaseUser currentUser;

    public MessageHolder(@NonNull View itemView, FirebaseUser currentUser) {
        super(itemView);
        this.currentUser = currentUser;

        foreignFromText = itemView.findViewById(R.id.foreign_from_text);
        foreignMessage = itemView.findViewById(R.id.foreign_msg);
        foreignMessageGroup = itemView.findViewById(R.id.foreign_msg_group);

        selfMessage = itemView.findViewById(R.id.self_msg);
        selfMessageGroup = itemView.findViewById(R.id.self_group);
    }

    public void bind(Message message) {
        if (currentUser.getEmail().equals(message.getEmail())) {
            foreignMessageGroup.setVisibility(View.GONE);
            selfMessageGroup.setVisibility(View.VISIBLE);

            selfMessage.setText(message.getMessage());
        } else {
            foreignMessageGroup.setVisibility(View.VISIBLE);
            selfMessageGroup.setVisibility(View.GONE);

            foreignFromText.setText("From: " + message.getEmail().substring(0, message.getEmail().lastIndexOf("@")));
            foreignMessage.setText(message.getMessage());
        }
    }
}
