package com.cegep.foodie.ui.chat;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.RecyclerView;
import com.cegep.foodie.R;
import com.cegep.foodie.model.Message;
import com.google.firebase.auth.FirebaseUser;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageHolder extends RecyclerView.ViewHolder {

    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd hh:mm a";

    private final TextView foreignFromText;
    private final TextView foreignMessage;
    private final TextView foreignTimestamp;
    private final Group foreignMessageGroup;

    private final TextView selfMessage;
    private final TextView selfTimestamp;
    private final Group selfMessageGroup;

    private final FirebaseUser currentUser;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TIMESTAMP_FORMAT);

    public MessageHolder(@NonNull View itemView, FirebaseUser currentUser) {
        super(itemView);
        this.currentUser = currentUser;

        foreignFromText = itemView.findViewById(R.id.foreign_from_text);
        foreignMessage = itemView.findViewById(R.id.foreign_msg);
        foreignMessageGroup = itemView.findViewById(R.id.foreign_msg_group);
        foreignTimestamp = itemView.findViewById(R.id.foreign_timestamp);

        selfMessage = itemView.findViewById(R.id.self_msg);
        selfMessageGroup = itemView.findViewById(R.id.self_group);
        selfTimestamp = itemView.findViewById(R.id.self_timestamp);
    }

    public void bind(Message message) {
        String formattedTimestamp = simpleDateFormat.format(new Date(message.getTimestamp()));
        if (currentUser.getEmail().equals(message.getEmail())) {
            foreignMessageGroup.setVisibility(View.GONE);
            selfMessageGroup.setVisibility(View.VISIBLE);

            selfMessage.setText(message.getMessage());
            selfTimestamp.setText(formattedTimestamp);
        } else {
            foreignMessageGroup.setVisibility(View.VISIBLE);
            selfMessageGroup.setVisibility(View.GONE);

            foreignFromText.setText("From: " + message.getEmail().substring(0, message.getEmail().lastIndexOf("@")));
            foreignMessage.setText(message.getMessage());
            foreignTimestamp.setText(formattedTimestamp);
        }
    }
}
