package core.delta.ticketsystem.ui.ticket;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;

import core.delta.ticketsystem.R;

public class TicketTouchCallback extends ItemTouchHelper.SimpleCallback{
    private static final String TAG = "TicketTouchCallback";

    private final TicketAdapter ticketAdapter;

    private Drawable deleteIcon;
    int intrinsicWidth;
    int intrinsicHeight;
//    ColorDrawable background;
//    int backgroundColor;
//    Paint clearPaint;

    public TicketTouchCallback(Context context, TicketAdapter adapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT);
//                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);

        ticketAdapter = adapter;
        deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete_forever_28);
        assert deleteIcon != null;
        deleteIcon.setTint(context.getColor(R.color.redIcon));
        intrinsicWidth = deleteIcon.getIntrinsicWidth();
        intrinsicHeight = deleteIcon.getIntrinsicHeight();
//        background = new ColorDrawable();
//        backgroundColor = Color.parseColor("#f44336");
//        clearPaint = new Paint();
//        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {

        int draggedItemIndex = viewHolder.getAdapterPosition();
        int targetIndex = target.getAdapterPosition();
        Collections.swap(ticketAdapter.getDataSet(), draggedItemIndex, targetIndex);
        ticketAdapter.notifyItemMoved(draggedItemIndex,targetIndex);
        return true;
    }

    // draw delete view
    @Override
    public void onChildDraw(@NonNull Canvas c,
                            @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX, float dY,
                            int actionState,
                            boolean isCurrentlyActive) {

        View itemView = viewHolder.itemView;
        int itemHeight =  itemView.getHeight();
//        int backgroundCornerOffset = 20; //so background is behind the rounded corners of itemView

        // Draw the red delete background
//        background.setColor(backgroundColor);
//        background.setBounds(
//                itemView.getRight() + ((int) dX) - backgroundCornerOffset,
//                itemView.getTop(),
//                itemView.getRight(),
//                itemView.getBottom()
//        );
//        background.draw(c);

        // Calculate position of delete icon
        int iconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
        int iconLeft = itemView.getRight() - (intrinsicWidth *2);
        int iconRight = itemView.getRight() - intrinsicWidth;
        int iconBottom = iconTop + intrinsicHeight;

        // Draw the delete icon
        deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
        deleteIcon.draw(c);

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    // TODO add different actions on left/right direction
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        ticketAdapter.removeItemAt(viewHolder.getAdapterPosition());
    }

    // unused in this project
    @Override
    public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        // prevent deleting (swiping) the first + last item which are holding the add buttons
//        if (viewHolder.getAdapterPosition() == 0
//                || viewHolder.getAdapterPosition() == ticketAdapter.getItemCount() -1) {
//            return 0;
//        } else {
            return super.getSwipeDirs(recyclerView, viewHolder);
//        }
    }

    // unused in this project
    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);

        // animate item appearance
//        if (actionState == ACTION_STATE_DRAG) {
//            assert viewHolder != null;
//            viewHolder.itemView.setAlpha(0.5f);
//        }
    }

    // needed by moving items on drag
    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        // update after drag item to new position
        //ticketAdapter.updateNotePositions();
        // animate item appearance
//        viewHolder.itemView.setAlpha(1.0f);
    }
}
