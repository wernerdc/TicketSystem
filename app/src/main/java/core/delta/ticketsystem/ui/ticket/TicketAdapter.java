package core.delta.ticketsystem.ui.ticket;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import core.delta.ticketsystem.R;
import core.delta.ticketsystem.model.TicketDBHelper;
import core.delta.ticketsystem.model.TicketModel;

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder> {
    private static final String TAG = "TicketAdapter";

    private final TicketFragment ticketFragment;
    // data model created by database adapter/helper
    private ArrayList<TicketModel> ticketDataSet;
    private final TicketDBHelper ticketDBHelper;
    // restore ticket by undo button
    private TicketModel recentlyDeletedItem;
    private int recentlyDeletedItemPosition;
    // expand detail view on item
    private int expandedPosition = -1;
    private int previousExpandedPosition = -1;


    // Provide a direct reference to each of the views within a data item
    public class ViewHolder extends RecyclerView.ViewHolder {
        // holder contains member variables for any view that will be set as you render a row
        public View itemStateMarker;
        public TextView itemTextState;
        public TextView itemTextHeader;
        public TextView itemTextRow2Date;
        public TextView itemTextRow2Department;
        public TextView itemTextRow2Priority;
        public TextView itemTextRow3Id;
        public TextView itemTextRow3User;
        public TextView itemTextRow3Mandate;
        public MaterialButton itemStateButton;
        public MaterialButton itemEditButton;

        public ConstraintLayout itemExpandLayout;
        public TextView itemTextDescription;

        // constructor that accepts the entire item row
        public ViewHolder(View itemView) {
            super(itemView);

            itemStateMarker         = itemView.findViewById(R.id.ticket_item_state_marker);
            itemTextState           = itemView.findViewById(R.id.ticket_item_text_state);
            itemTextHeader          = itemView.findViewById(R.id.ticket_item_text_header);
            itemTextRow2Date        = itemView.findViewById(R.id.ticket_item_text_row2_date);
            itemTextRow2Department  = itemView.findViewById(R.id.ticket_item_text_row2_department);
            itemTextRow2Priority    = itemView.findViewById(R.id.ticket_item_text_row3_priority);
            itemTextRow3Id          = itemView.findViewById(R.id.ticket_item_text_row3_id);
            itemTextRow3User        = itemView.findViewById(R.id.ticket_item_text_user);
            itemTextRow3Mandate     = itemView.findViewById(R.id.ticket_item_text_mandate);
            itemStateButton         = itemView.findViewById(R.id.ticket_item_edit_state_button);
            itemEditButton          = itemView.findViewById(R.id.ticket_item_right_button);

            itemExpandLayout        = itemView.findViewById(R.id.ticket_item_expanded_layout);
            itemTextDescription     = itemView.findViewById(R.id.ticket_item_text_description);

            // Define click listener for the ViewHolder's View. (complete list item)
            itemView.setOnClickListener(v -> {
                Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");

            });
        }
    }


    // constructor
    public TicketAdapter(ArrayList<TicketModel> dataSet, TicketDBHelper ticketDBHelper, TicketFragment ticketFragment) {
        this.ticketDataSet = dataSet;
        this.ticketDBHelper = ticketDBHelper;
        this.ticketFragment = ticketFragment;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public TicketAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View itemView = inflater.inflate(R.layout.item_row_ticket, parent, false);
        // Return a new holder instance
        return new ViewHolder(itemView);
    }

    // Involves populating data into the item through holder
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // TODO: debug/solution for deleted item that changes "position" of previously expanded item

        Log.d(TAG, "Element " + position + " set.");

        // set item detail expanded layout visibility
//        final boolean isExpanded = isItemExpanded(position);
        final boolean isExpanded = (position == expandedPosition);
        holder.itemExpandLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.itemView.setActivated(isExpanded);

        if (isExpanded) {
            previousExpandedPosition = position;
        }

        holder.itemView.setOnClickListener(v -> {
            // get state of the item
//            boolean expanded = isItemExpanded(holder.getAdapterPosition());
//            setItemExpanded(holder.getAdapterPosition(), !expanded);
            expandedPosition = isExpanded ? -1 : holder.getAdapterPosition();
            notifyItemChanged(previousExpandedPosition);
            notifyItemChanged(holder.getAdapterPosition());
        });


        // Set item views based on your views and data model
        setStateMarkerViewColor(position, holder.itemStateMarker);

        MaterialButton stateButton          = holder.itemStateButton;
        TextView textViewState              = holder.itemTextState;
        TextView textViewHeader             = holder.itemTextHeader;
        TextView textViewRow2Date           = holder.itemTextRow2Date;
        TextView textViewRow2Department     = holder.itemTextRow2Department;
        TextView textViewRow2Priority       = holder.itemTextRow2Priority;
        TextView textViewRow3Id             = holder.itemTextRow3Id;
        TextView textViewRow3User           = holder.itemTextRow3User;
        TextView textViewRow3Mandate        = holder.itemTextRow3Mandate;
        MaterialButton rightButton          = holder.itemEditButton;
        TextView textViewDescription        = holder.itemTextDescription;

//        leftButton.setIconResource(R.drawable.ic_edit);
        stateButton.setOnClickListener(v -> {
            // set ticket state by button
            InputDialog inputDialog = new InputDialog(TicketAdapter.this);
            inputDialog.setItemState(holder.getAdapterPosition());
        });
        textViewState.setText(getItemStateTextAt(position));

        textViewHeader.setText(getItemSubjectAt(position));
        textViewRow2Date.setText(getItemDateAt(position));
        textViewRow2Department.setText(getItemDepartmentAt(position));
        textViewRow2Priority.setText("Priority: " + getItemPriorityTextAt(position));
        textViewRow3Id.setText("#" + getItemTicketIdAt(position));
        textViewRow3User.setText("Creator: " + getItemUserNameAt(position));
        textViewRow3Mandate.setText("Mandate to: " + getItemMandateAt(position));

        rightButton.setOnClickListener(v -> {
            InputDialog inputDialog = new InputDialog(TicketAdapter.this);
            inputDialog.editItem(holder.getAdapterPosition());
        });

        textViewDescription.setText(getItemDescriptionAt(position));
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return ticketDataSet.size();
    }

    public ArrayList<TicketModel> getDataSet() {
        return ticketDataSet;
    }

    public TicketModel getItemAt(int position) {
        return ticketDataSet.get(position);
    }

    public TicketFragment getTicketFragment() {
        return ticketFragment;
    }

    public void addItemAt(int position,
                          String subject,
                          String description,
                          String department,
                          String username) {


        // Format the current time.
        SimpleDateFormat formatter = new SimpleDateFormat ("dd.MM.yyyy");
        Date currentDate = new Date();
        String dateString = formatter.format(currentDate);

        TicketModel newTicket = new TicketModel(dateString, subject, description, department, username);
        addItemAt(position, newTicket);
    }

    public void addItemAt(int position, TicketModel newTicket) {

        ticketDataSet.add(position, newTicket);
        ticketDBHelper.addOne(newTicket);

        notifyItemInserted(position);
    }

    public void editItemAt(int position, TicketModel editedData) {
        ticketDataSet.get(position).updateData(editedData);

        ticketDBHelper.updateOne(ticketDataSet.get(position));
        notifyItemChanged(position);
    }

    public void removeItemAt(int position) {
        recentlyDeletedItemPosition = position;
        recentlyDeletedItem = ticketDataSet.get(position);

        ticketDBHelper.deleteOne(ticketDataSet.get(position));
        ticketDataSet.remove(position);

//        updateTicketPositions();
        notifyItemRemoved(position);

        showUndoSnackbar();
    }

    private void showUndoSnackbar() {

        View bottomBar = ticketFragment.requireActivity().findViewById(R.id.bottomBar);
        View mainView = ticketFragment.requireActivity().findViewById(R.id.main);
//        View view = ticketFragment.requireView();
        Snackbar snackbar = Snackbar.make(mainView, "Ticket removed from list.",
                Snackbar.LENGTH_LONG);
        snackbar.setAnchorView(bottomBar);
        snackbar.setAction("Undo", v -> undoDelete());
        snackbar.show();
    }

    private void undoDelete() {
        addItemAt(recentlyDeletedItemPosition, recentlyDeletedItem);
    }

    public void updateTicketPositions() {
        ticketDBHelper.updateAllPositions(ticketDataSet);
    }

    public void setItemStateAt(int position, int state) {
        ticketDataSet.get(position).setState(state);

        ticketDBHelper.updateOne(ticketDataSet.get(position));
        notifyItemChanged(position);
    }

    public int getItemStateAt(int position) {
        return ticketDataSet.get(position).getState();
    }

    public String getItemStateTextAt(int position) {
        int ticketState = getItemStateAt(position);
        String[] stateOptions = TicketModel.getStateOptions(ticketFragment.requireContext());
        return stateOptions[ticketState];
    }

    public String getItemSubjectAt(int position) {
        return ticketDataSet.get(position).getSubject();
    }

    public String getItemDateAt(int position) {
        return ticketDataSet.get(position).getDate();
    }

    public String getItemDepartmentAt(int position) {
        return ticketDataSet.get(position).getDepartment();
    }

    public int getItemPriorityAt(int position) {
        return ticketDataSet.get(position).getPriority();
    }

    public String getItemPriorityTextAt(int position) {
        int priority = getItemPriorityAt(position);
        String[] priorityOptions = TicketModel.getPriorityOptions(ticketFragment.requireContext());
        return priorityOptions[priority];
    }

    public int getItemTicketIdAt(int position) {
        return ticketDataSet.get(position).getId() + 10000;
    }

    public String getItemUserNameAt(int position) {
        return ticketDataSet.get(position).getUserName();
    }

    public String getItemMandateAt(int position) {
        return ticketDataSet.get(position).getMandate();
    }

    public String getItemDescriptionAt(int position) {
        return ticketDataSet.get(position).getDescription();
    }

    private void setStateMarkerViewColor(int itemPosition, View stateMarker) {
        switch (getItemStateAt(itemPosition)) {
            case 0:
                stateMarker.setBackgroundColor(ticketFragment.requireContext().getColor(R.color.blue));
                break;
            case 1:
                stateMarker.setBackgroundColor(ticketFragment.requireContext().getColor(R.color.purple));
                break;
            case 2:
                stateMarker.setBackgroundColor(ticketFragment.requireContext().getColor(R.color.orange));
                break;
            case 3:
                stateMarker.setBackgroundColor(ticketFragment.requireContext().getColor(R.color.green));
                break;
            case 4:
                stateMarker.setBackgroundColor(ticketFragment.requireContext().getColor(R.color.red));
                break;
            default:
        }
    }
}