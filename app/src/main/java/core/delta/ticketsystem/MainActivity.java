package core.delta.ticketsystem;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import core.delta.ticketsystem.ui.FabListener;
import core.delta.ticketsystem.ui.chart.ChartFragment;
import core.delta.ticketsystem.ui.database.DatabaseFragment;
import core.delta.ticketsystem.ui.ticket.TicketFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FabListener fabListener;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ToDo: EdgeToEdge does not work at bottom bar, especially on older SDKs
        // set new edge to edge layout behind navigation/status bars
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // set padding of main layout for edge to edge
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);     // correct bottom padding. but why is it failing?
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);     // correct bottom padding. but why is it failing?
//            return insets;
//        });

        // set first fragment on startup
        replaceFragment(TicketFragment.newInstance("param1x", "param2x"));

        bottomNavigationView = findViewById(R.id.bottom_navigation);
//        bottomNavigationView.getMenu().findItem(R.id.nav_notes).setChecked(true);       // set initial button state to home fragment (notes)
//        bottomNavigationView.setSelectedItemId(R.id.nav_notes);

        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.nav_ticket) {
                MainActivity.this.replaceFragment(TicketFragment.newInstance("param1", "param2"));
            } else if (itemId == R.id.nav_chart) {
                MainActivity.this.replaceFragment(ChartFragment.newInstance("param1", "param2"));
            } else if (itemId == R.id.nav_database) {
                MainActivity.this.replaceFragment(DatabaseFragment.newInstance("param1", "param2"));
            }
            return true;        // changes selected icon
        });

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_wrapper);
            // decide on which fragment the FAB has been clicked
            if (currentFragment instanceof TicketFragment) {
                fabListener.onFabClicked();
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_wrapper, fragment)
                    .commit();
        }
    }

    public void setFabListener(FabListener fabListener) {
        this.fabListener = fabListener;
    }
}