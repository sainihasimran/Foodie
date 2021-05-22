package com.cegep.foodie.ui.chat.room;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cegep.foodie.R;
import com.cegep.foodie.model.Category;
import com.cegep.foodie.ui.chat.ChatActivity;

class SelectRoomAdapter extends RecyclerView.Adapter<SelectRoomAdapter.SelectRoomViewHolder> {

    private final Category[] cateories = Category.values();

    private final Activity activity;

    public SelectRoomAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public SelectRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SelectRoomViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SelectRoomAdapter.SelectRoomViewHolder holder, int position) {
        holder.bind(cateories[position]);
    }

    @Override
    public int getItemCount() {
        return cateories.length;
    }

    class SelectRoomViewHolder extends RecyclerView.ViewHolder {

        private final ImageView roomImageView;
        private final TextView roomName;

        private Category category;

        public SelectRoomViewHolder(@NonNull View itemView) {
            super(itemView);

            roomImageView = itemView.findViewById(R.id.image);
            roomName = itemView.findViewById(R.id.name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ChatActivity.class);
                    intent.putExtra("room", category.getNiceName());

                    activity.startActivity(intent);
                }
            });
        }

        void bind(Category category) {
            this.category = category;
            roomImageView.setImageResource(category.drawable);
            roomName.setText(category.getNiceName());
        }
    }
}
