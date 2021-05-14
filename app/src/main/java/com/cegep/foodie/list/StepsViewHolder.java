package com.cegep.foodie.list;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cegep.foodie.R;
import com.cegep.foodie.list.callback.RemoveItemClickListener;
import com.cegep.foodie.model.PreparationStep;

class StepsViewHolder extends RecyclerView.ViewHolder {

    private final TextView stepNumberTextView;
    private final TextView stepTextView;
    private final ImageView removeButton;

    private final RemoveItemClickListener removeItemClickListener;

    private PreparationStep step;

    StepsViewHolder(@NonNull View view, RemoveItemClickListener removeItemClickListener) {
        super(view);
        this.removeItemClickListener = removeItemClickListener;

        stepNumberTextView = view.findViewById(R.id.step_number);
        stepTextView = view.findViewById(R.id.step_text);
        removeButton = view.findViewById(R.id.remove_button);
    }

    void bind(PreparationStep step, int position, Boolean isEditable) {
        this.step = step;
        stepTextView.setText(step.stepDescription);
        stepNumberTextView.setText(String.valueOf(position + 1));
        removeButton.setVisibility(isEditable ? View.VISIBLE : View.INVISIBLE);
        removeButton.setOnClickListener(v -> {
            if (removeItemClickListener != null) {
                removeItemClickListener.onRemoveClick(step, getAdapterPosition());
            }
        });
    }
}
