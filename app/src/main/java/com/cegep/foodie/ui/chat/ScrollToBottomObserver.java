package com.cegep.foodie.ui.chat;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.cegep.foodie.model.Message;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

class ScrollToBottomObserver extends RecyclerView.AdapterDataObserver {

    private final RecyclerView recycler;
    private final FirebaseRecyclerAdapter<Message, MessageHolder> adapter;
    private final LinearLayoutManager manager;

    public ScrollToBottomObserver(RecyclerView recycler,
                                  FirebaseRecyclerAdapter<Message, MessageHolder> adapter, LinearLayoutManager manager) {
        this.recycler = recycler;
        this.adapter = adapter;
        this.manager = manager;
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        super.onItemRangeInserted(positionStart, itemCount);

        int count = adapter.getItemCount();
        int lastVisiblePosition = manager.findLastCompletelyVisibleItemPosition();

        boolean loading = lastVisiblePosition == -1;
        boolean atBottom = positionStart >= count - 1 && lastVisiblePosition == positionStart - 1;
        if (loading || atBottom) {
            recycler.smoothScrollToPosition(positionStart);
        }
    }
}
