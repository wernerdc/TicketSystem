package core.delta.ticketsystem.ui.ticket;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import core.delta.ticketsystem.R;
import core.delta.ticketsystem.model.TicketModel;

public class InputDialog {

    private final TicketFragment fragment;
    private final TicketAdapter ticketAdapter;
    private String dialogTitle;

    private final String[] stateOptions;
    private final String[] priorityOptions;

    private EditText editTextSubject;
    private EditText editTextDescription;
    private EditText editTextDepartment;
    private EditText editTextUsername;
    private EditText editTextMandate;
    private TextInputLayout stateDropdownLayout;
    private AutoCompleteTextView stateDropdown;
    private TextInputLayout priorityDropdownLayout;
    private AutoCompleteTextView priorityDropdown;

    private int itemPosition;
    private TicketModel ticket;
    private DialogAction dialogAction;

    public enum DialogAction {
        ADD_ITEM, EDIT_ITEM, SET_STATE
    }

    public InputDialog(TicketAdapter adapter) {
        this.ticketAdapter = adapter;
        this.fragment = adapter.getTicketFragment();
        this.stateOptions = TicketModel.getStateOptions(fragment.requireContext());
        this.priorityOptions = TicketModel.getPriorityOptions(fragment.requireContext());
    }

    public void addItem(int itemPosition) {
        this.itemPosition = itemPosition;
        this.dialogAction = DialogAction.ADD_ITEM;
        this.dialogTitle = fragment.getString(R.string.add_ticket);

        // I'm using fragment here so I'm using getView() to provide ViewGroup
        // but you can provide here any other instance of ViewGroup from your Fragment / Activity
        View dialogView = LayoutInflater.from(fragment.getContext())
                .inflate(R.layout.dialog_add_ticket, (ViewGroup) fragment.getView(), false);

        editTextSubject = dialogView.findViewById(R.id.dialog_subject_editText);
        editTextDescription = dialogView.findViewById(R.id.dialog_description_editText);
        editTextDepartment = dialogView.findViewById(R.id.dialog_department_editText);
        editTextUsername = dialogView.findViewById(R.id.dialog_username_editText);

        showDialog(dialogView);
    }

    public void editItem(int itemPosition) {
        this.itemPosition = itemPosition;
        this.dialogAction = DialogAction.EDIT_ITEM;
        this.dialogTitle = fragment.getString(R.string.edit_ticket);

        ticket = ticketAdapter.getItemAt(itemPosition);

        View dialogView = LayoutInflater.from(fragment.getContext())
                .inflate(R.layout.dialog_edit_ticket, (ViewGroup) fragment.getView(), false);
        // Set up the input
        editTextSubject = dialogView.findViewById(R.id.dialog_subject_editText);
        editTextSubject.setText(ticket.getSubject());

        editTextDescription = dialogView.findViewById(R.id.dialog_description_editText);
        editTextDescription.setText(ticket.getDescription());

        editTextDepartment = dialogView.findViewById(R.id.dialog_department_editText);
        editTextDepartment.setText(ticket.getDepartment());

        editTextUsername = dialogView.findViewById(R.id.dialog_username_editText);
        editTextUsername.setText(ticket.getUserName());

        editTextMandate = dialogView.findViewById(R.id.dialog_mandate_editText);
        editTextMandate.setText(ticket.getMandate());

        stateDropdownLayout = dialogView.findViewById(R.id.dialog_state_menu_ti_layout);
        stateDropdown = dialogView.findViewById(R.id.dialog_state_menu_text_view);
        final ArrayAdapter<String> statesAdapter = new ArrayAdapter<>(
                fragment.requireContext(), R.layout.item_dropdown, stateOptions);
        stateDropdown.setAdapter(statesAdapter);
        stateDropdown.setText(statesAdapter.getItem(ticket.getState()), false);
//        Toast.makeText(fragment.requireContext(), stateDropdownLayout.getEditText().getText(),Toast.LENGTH_SHORT).show();

        priorityDropdownLayout = dialogView.findViewById(R.id.dialog_priority_menu_ti_layout);
        priorityDropdown = dialogView.findViewById(R.id.dialog_priority_menu_text_view);
        final ArrayAdapter<String> prioritiesAdapter = new ArrayAdapter<>(
                fragment.requireContext(), R.layout.item_dropdown, priorityOptions);
        priorityDropdown.setAdapter(prioritiesAdapter);
        priorityDropdown.setText(prioritiesAdapter.getItem(ticket.getPriority()), false);

        showDialog(dialogView);
    }

    public void setItemState(int itemPosition) {
        this.itemPosition = itemPosition;
        this.dialogAction = DialogAction.SET_STATE;
        this.dialogTitle = fragment.getString(R.string.set_state);

        ticket = ticketAdapter.getItemAt(itemPosition);

        View dialogView = LayoutInflater.from(fragment.getContext())
                .inflate(R.layout.dialog_set_state_ticket, (ViewGroup) fragment.getView(), false);
        // Set up the input
        stateDropdownLayout = dialogView.findViewById(R.id.dialog_state_menu_ti_layout);
        stateDropdown = dialogView.findViewById(R.id.dialog_state_menu_text_view);
        final ArrayAdapter<String> statesAdapter = new ArrayAdapter<>(
                fragment.requireContext(), R.layout.item_dropdown, stateOptions);
        stateDropdown.setAdapter(statesAdapter);
        stateDropdown.setText(statesAdapter.getItem(ticket.getState()), false);

        showDialog(dialogView);
    }

    private void showDialog(View dialogView) {
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(fragment.requireContext());
        dialogBuilder.setTitle(dialogTitle);

        dialogBuilder.setView(dialogView);

        // switch between dialog types
        dialogBuilder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            TicketModel changedData;
            switch (dialogAction) {
                case ADD_ITEM:
                    ticketAdapter.addItemAt(itemPosition,
                            editTextSubject.getText().toString(),
                            editTextDescription.getText().toString(),
                            editTextDepartment.getText().toString(),
                            editTextUsername.getText().toString());
                    break;

                case EDIT_ITEM:
                    changedData = new TicketModel(
                            ticket.getId(),
                            ticket.getDate(),
                            getDropdownState(),
                            getDropdownPriority(),
                            editTextDepartment.getText().toString(),
                            editTextMandate.getText().toString(),
                            ticket.getTargetDate(),
                            editTextSubject.getText().toString(),
                            editTextDescription.getText().toString(),
                            ticket.getUserId(),
                            editTextUsername.getText().toString()
                    );

                    ticketAdapter.editItemAt(itemPosition, changedData);

//                    Toast.makeText(fragment.requireContext(), Objects.requireNonNull(stateDropdownLayout.getEditText()).getText(),Toast.LENGTH_SHORT).show();
                    break;

                case SET_STATE:
                    ticketAdapter.setItemStateAt(itemPosition, getDropdownState());
                    break;
                default:
            }
            dialog.dismiss();
        });
        dialogBuilder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
            dialog.dismiss();
        });

        dialogBuilder.show();
    }

    private int getDropdownState() {
        return mapDropdownStringToIntValue(stateDropdownLayout,stateDropdown,stateOptions);
    }

    private int getDropdownPriority() {
        return mapDropdownStringToIntValue(priorityDropdownLayout,priorityDropdown,priorityOptions);
    }

    private int mapDropdownStringToIntValue(TextInputLayout dropdownLayout,
                                            AutoCompleteTextView dropdownView,
                                            String[] dropdownOptions) {

        String dropdownSelection = Objects.requireNonNull(dropdownLayout.getEditText()).getText().toString();
        int value = 0;
        for (int i = 0; i < dropdownView.getAdapter().getCount(); i++) {
            if (dropdownSelection.equals(dropdownOptions[i])) {
                value = i;
            }
        }
        return value;
    }
}
