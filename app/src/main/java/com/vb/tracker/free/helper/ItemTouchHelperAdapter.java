package com.vb.tracker.free.helper;

public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemLeftDismiss(int position);

    void onItemRightDismiss(int position);

}
