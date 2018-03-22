package com.vb.tracker.free.routinecalendar;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.vb.tracker.R;
import com.vb.tracker.free.VBtracker;
import com.vb.tracker.free.colorpicker.ColorPalette;
import com.vb.tracker.free.helper.ItemTouchHelperAdapter;
import com.vb.tracker.free.helper.ItemTouchHelperViewHolder;

class RoutineSimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private static final float ALPHA_FULL = 1.0f;

    private final ItemTouchHelperAdapter mAdapter;

    RoutineSimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        if (viewHolder instanceof RoutineRecyclerListAdapter.ItemViewHolderNotDone) {
            return makeMovementFlags(0, ItemTouchHelper.END);
        } else if (viewHolder instanceof RoutineRecyclerListAdapter.ItemViewHolderDone) {
            return makeMovementFlags(0, ItemTouchHelper.START);
        }
        return makeMovementFlags(0, 0);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        if (source.getItemViewType() != target.getItemViewType()) {
            return false;
        }

        mAdapter.onItemMove(source.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        if (i == 32) {
            mAdapter.onItemRightDismiss(viewHolder.getAdapterPosition());
        }
        if (i == 16) {
            mAdapter.onItemLeftDismiss(viewHolder.getAdapterPosition());
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        final int scale = (int) recyclerView.getContext().getResources().getDisplayMetrics().density;

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            View itemView = viewHolder.itemView;
            final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
            viewHolder.itemView.setAlpha(alpha);
            viewHolder.itemView.setTranslationX(dX);

            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeWidth((float) 0.3);

            if (dX > 0) {
                paint.setColor(ColorPalette.GREEN.getColor());
                paint.setTextSize(20 * scale);
                paint.setTypeface(Typeface.createFromAsset(recyclerView.getContext().getAssets(), "font/Roboto-Thin.ttf"));
                c.drawText(VBtracker.getInstance().getString(R.string.done).toUpperCase(), itemView.getLeft() + 10 * scale, itemView.getBottom() - (itemView.getBottom() - itemView.getTop()) / 2 + 7 * scale, paint);
                c.drawLine(itemView.getRight(), (float) itemView.getBottom() - 1, 0, (float) itemView.getBottom() - 1, paint);
            } else {
                paint.setColor(ColorPalette.RED.getColor());
                paint.setTextSize(20 * scale);
                paint.setTypeface(Typeface.createFromAsset(recyclerView.getContext().getAssets(), "font/Roboto-Thin.ttf"));
                paint.setTextAlign(Paint.Align.RIGHT);
                c.drawText(VBtracker.getInstance().getString(R.string.undone).toUpperCase(), itemView.getRight() - 10 * scale, itemView.getBottom() - (itemView.getBottom() - itemView.getTop()) / 2 + 7 * scale, paint);
                c.drawLine(itemView.getRight(), (float) itemView.getBottom() - 1, 0, (float) itemView.getBottom() - 1, paint);

            }

        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {

        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof ItemTouchHelperViewHolder) {

                ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
                itemViewHolder.onItemSelected();
            }
        }

        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        viewHolder.itemView.setAlpha(ALPHA_FULL);

        if (viewHolder instanceof ItemTouchHelperViewHolder) {

            ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
            itemViewHolder.onItemClear();
        }
    }
}
