package core.delta.ticketsystem.ui.ticket;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import core.delta.ticketsystem.MainActivity;
import core.delta.ticketsystem.R;
import core.delta.ticketsystem.model.TicketDBHelper;
import core.delta.ticketsystem.model.TicketModel;
import core.delta.ticketsystem.ui.FabListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TicketFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TicketFragment extends Fragment implements FabListener {
    private static final String TAG = "TicketFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // member vars
    protected RecyclerView recyclerView;
    protected TicketAdapter ticketListAadapter;
    protected RecyclerView.LayoutManager layoutManager;
    private TicketDBHelper ticketDBHelper;
    private ArrayList<TicketModel> ticketsDataSet;
    private ItemTouchHelper itemTouchHelper;


    public TicketFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TicketFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TicketFragment newInstance(String param1, String param2) {
        TicketFragment fragment = new TicketFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // initialize dataset form local database
        ticketDBHelper = new TicketDBHelper(this.getContext());
        ticketsDataSet = ticketDBHelper.getAll();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ticket, container, false);
        rootView.setTag(TAG);

        // get recycler view
        recyclerView = (RecyclerView) rootView.findViewById(R.id.ticket_recycler_view);

        // fix/removes blinks
//        ((SimpleItemAnimator) Objects.requireNonNull(recyclerView.getItemAnimator())).setSupportsChangeAnimations(false);

        // Create adapter passing in the sample user data
        ticketListAadapter = new TicketAdapter(ticketsDataSet, ticketDBHelper, this);
        // Attach the adapter to the recyclerview to populate items
        recyclerView.setAdapter(ticketListAadapter);
        // Set layout manager to position the items
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        // implement swipe on items
        ItemTouchHelper.Callback callback = new TicketTouchCallback(getContext(), ticketListAadapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity) requireActivity()).setFabListener(this);
    }

    @Override
    public void onFabClicked() {
        InputDialog inputDialog = new InputDialog(ticketListAadapter);
        inputDialog.addItem(ticketListAadapter.getItemCount());

//        Toast.makeText(((MainActivity) getActivity()), "fab button", Toast.LENGTH_SHORT).show();
    }

}