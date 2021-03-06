package com.example.immortal.clock_seller.activity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.immortal.clock_seller.adapter.HistoryAdapter;
import com.example.immortal.clock_seller.model.Clock;
import com.example.immortal.clock_seller.utils.DataBase;
import com.example.immortal.clock_seller.model.User;
import com.example.immortal.clock_seller.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private Toolbar tbHistory;
    private TextView txtAnnouce;
    private ListView lvHistory;
    private ArrayList<Clock> clocks;
    private HistoryAdapter historyAdapter;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        inits();
        controls();
    }

    /**
     * Ánh xạ các view và khỏi tạo giá trị
     */
    private void inits() {
        tbHistory = findViewById(R.id.tb_History);
        lvHistory = findViewById(R.id.lv_HHistory);
        txtAnnouce = findViewById(R.id.txt_HAnnouce);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setSupportActionBar(tbHistory);
        setTitle("Lịch sử");
        loadingActionBar();
        clocks = new ArrayList<>();

        historyAdapter = new HistoryAdapter(this, R.layout.layout_history_item, clocks);
        lvHistory.setAdapter(historyAdapter);
        historyAdapter.notifyDataSetChanged();
        loadHistory(SignInActivity.user);


    }

    /**
     * Tạo đối tượng Navigation button trên ActionBar
     */
    private void loadingActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        tbHistory.setNavigationIcon(R.drawable.arrow_back_24dp);
    }

    /**
     * Thêm các sự kiện lắng nghe, điều khiển
     */
    private void controls() {
        tbHistory.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    /**
     * Load lịch sử mua hàng của một khách hàng từ database
     * @param sUser -khách hàng
     */
    private void loadHistory(User sUser) {
        String email = sUser.getEmail();
        email = email.replace("@", "");
        email = email.replace(".", "");
        mDatabase.child("History").child(email).addChildEventListener(new DataBase() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Clock clock = dataSnapshot.getValue(Clock.class);
                if ((!clock.getName().equals("Default")) && clock.getPrice() != 0) {
                    clocks.add(0, dataSnapshot.getValue(Clock.class));
                    historyAdapter.notifyDataSetChanged();
                    txtAnnouce.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
