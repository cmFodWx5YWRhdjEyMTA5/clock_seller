package com.example.immortal.clock_seller.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.immortal.clock_seller.Adapter.HotProductAdapter;
import com.example.immortal.clock_seller.Adapter.MyMenuItemAdapter;
import com.example.immortal.clock_seller.Model.Cart;
import com.example.immortal.clock_seller.Model.Model;
import com.example.immortal.clock_seller.Model.MyMenuItem;
import com.example.immortal.clock_seller.Model.User;
import com.example.immortal.clock_seller.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainPageActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String manufaturer_name = "manufacture";
    Toolbar tb_MainPage;
    RecyclerView rv_NewProducts;
    ListView lv_Navigation;
    DrawerLayout drawerLayout;
    TextView txt_Product;

    ArrayList<MyMenuItem> myMenuItems;
    MyMenuItemAdapter myMenuItemAdapter;
    MyMenuItem mi_MainPage, mi_Profile, mi_SignOut, mi_History;

    ArrayList<Model> models;
    HotProductAdapter hotProductAdapter;

    public FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public static ArrayList<Cart> carts;
//    public static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inits();
        controls();
    }


    private void controls() {
        actionBarClick();
        drawerStateChange();
        navigationItemClick();

    }

    private void navigationItemClick() {
        lv_Navigation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MyMenuItem item = (MyMenuItem) adapterView.getItemAtPosition(i);
                String name = item.getName();
                if (name.equals("Trang chính")) {
                    Intent i_ToMainPage = new Intent(MainPageActivity.this, MainPageActivity.class);
                    startActivity(i_ToMainPage);
                } else if (name.equals("Thông tin tài khoản")) {
                    Intent i_ToProfile = new Intent(MainPageActivity.this, ProfileActivity.class);
                    startActivity(i_ToProfile);
                } else if (name.equals("Đăng xuất")) {
                    mAuth.signOut();
                    SignInActivity.user = null;
                    Intent i_ToSignIn = new Intent(MainPageActivity.this, SignInActivity.class);
                    startActivity(i_ToSignIn);
                } else if (name.equals("Lịch sử")) {
                    Intent i_ToHistory = new Intent(MainPageActivity.this, HistoryActivity.class);
                    startActivity(i_ToHistory);
                } else {
                    Intent i_ToProDuct = new Intent(MainPageActivity.this, ProducstActivity.class);
                    i_ToProDuct.putExtra(manufaturer_name, item.getName());
                    startActivity(i_ToProDuct);
                }

            }
        });
    }

    private void drawerStateChange() {
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (mi_Profile == null) {
                    mi_Profile = new MyMenuItem();
                    mi_Profile.setImage("https://firebasestorage.googleapis.com/v0/b/clockseller-5de25.appspot.com/o/profile.png?alt=media&token=1fda1b40-9620-4f1c-9266-b7d707df5256");
                    mi_Profile.setName("Thông tin tài khoản");
                    myMenuItems.add(mi_Profile);
                    myMenuItemAdapter.notifyDataSetChanged();
                }
                if (mi_SignOut == null) {
                    mi_SignOut = new MyMenuItem();
                    mi_SignOut.setImage("https://firebasestorage.googleapis.com/v0/b/clockseller-5de25.appspot.com/o/signout.png?alt=media&token=e10fe8fe-89b9-4c05-a3a5-3e4140531036");
                    mi_SignOut.setName("Đăng xuất");
                    myMenuItems.add(mi_SignOut);
                    myMenuItemAdapter.notifyDataSetChanged();
                }

                if (mi_History == null) {
                    mi_History = new MyMenuItem();
                    mi_History.setImage("https://firebasestorage.googleapis.com/v0/b/clockseller-5de25.appspot.com/o/history.png?alt=media&token=b03f4e74-4882-491f-95bf-945b1eb8c0db");
                    mi_History.setName("Lịch sử");
                    myMenuItems.add(mi_History);
                    myMenuItemAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                myMenuItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }


    private void actionBarClick() {
        tb_MainPage.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void inits() {
        rv_NewProducts = findViewById(R.id.rv_NewProducts);
        lv_Navigation = findViewById(R.id.lv_Menu);
        tb_MainPage = findViewById(R.id.tb_MainPage);
        drawerLayout = findViewById(R.id.dl_MainPageLayout);
        txt_Product = findViewById(R.id.txt_NewProduct);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        myMenuItems = new ArrayList<>();
        setSupportActionBar(tb_MainPage);
        setTitle("Trang chủ");

        if (carts == null) {
            carts = new ArrayList<>();
            loadCart();
        }


        models = new ArrayList<>();
        hotProductAdapter = new HotProductAdapter(getApplicationContext(), R.layout.layout_hot_product_item, models);
        rv_NewProducts.setHasFixedSize(true);
        rv_NewProducts.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        rv_NewProducts.setAdapter(hotProductAdapter);

        myMenuItems = new ArrayList<>();
        myMenuItemAdapter = new MyMenuItemAdapter(MainPageActivity.this, R.layout.layout_menu_item, myMenuItems);
        lv_Navigation.setAdapter(myMenuItemAdapter);
        ;
        myMenuItemAdapter.notifyDataSetChanged();

        loadHotModel();
        loadingActionBar();
        loadMenu();
    }

    private void loadCart() {
        Intent i_rc = getIntent();
        String mail = i_rc.getStringExtra(SignInActivity.email_key);
        mDatabase.child("Cart").child(mail).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Cart cart = dataSnapshot.getValue(Cart.class);
                if ((!cart.getName().equals("Default")) &&(cart.getPrice() != 0)){
                    carts.add(cart);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void loadMenu() {

        if (myMenuItems.size() <= 0) {
            mi_MainPage = new MyMenuItem();
            mi_MainPage.setImage("https://firebasestorage.googleapis.com/v0/b/clockseller-5de25.appspot.com/o/home.png?alt=media&token=7694d7ac-f5f5-44aa-91c8-fc51e19ecb13");
            mi_MainPage.setName("Trang chính");
            myMenuItems.add(mi_MainPage);
            myMenuItemAdapter.notifyDataSetChanged();

            mDatabase.child("Brand").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    myMenuItems.add(dataSnapshot.getValue(MyMenuItem.class));
                    myMenuItemAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

//        myMenuItemAdapter.notifyDataSetChanged();
    }


    private void loadHotModel() {
        if (models.size() <= 0) {
            mDatabase.child("Model").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                    Toast.makeText(MainPageActivity.this, models.size() + "", Toast.LENGTH_LONG).show();
                    Model model = dataSnapshot.getValue(Model.class);
                    models.add(model);
                    hotProductAdapter.notifyDataSetChanged();

//                    Toast.makeText(MainPageActivity.this, models.size() + "aaaaaaaaaaaaa", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    private void loadingActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        tb_MainPage.setNavigationIcon(R.drawable.menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mn_cart:
                Intent i_ToCart = new Intent(MainPageActivity.this, CartActivity.class);
                startActivity(i_ToCart);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.txt_NewProduct:
//                Intent i_ToProduct = new Intent(MainPageActivity.this, ProducstActivity.class);
//                startActivity(i_ToProduct);
        }
    }
}
