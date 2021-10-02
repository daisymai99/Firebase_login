package com.example.myapplication;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.WebAdapter;
import com.example.myapplication.model.WebModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView reWeb;
    WebAdapter webAdapter;
    DatabaseReference db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reWeb = findViewById(R.id.reWeb);


        buildReWeb();
        getData();
        navigationBar();

    }

    private void navigationBar() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        NavigationView navigationView = findViewById(R.id.nav);
        MaterialToolbar toolbar = findViewById(R.id.toolbar);

//        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id){
                    case R.id.home:
                        onBackPressed();
                        break;



                    case R.id.log:
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getApplicationContext(), SignIn.class);
                        startActivity(intent);
                        finish();
                        break;


                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    private void getData() {
        List<WebModel> data = getFakeData();
        webAdapter.setDate(data);
    }

    private List<WebModel> getFakeData() {
        List<WebModel> webModelList = new ArrayList<>();
        db = FirebaseDatabase.getInstance().getReference("web");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot data : snapshot.getChildren()){
                        WebModel model = data.getValue(WebModel.class);
                        webModelList.add(model);
                    }
                    webAdapter.setDate(webModelList);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return webModelList;
    }

    private void buildReWeb() {
        webAdapter = new WebAdapter();
        webAdapter.setOnWebListener((webModel, i) -> {
            webAdapter.setSelect(i);
            openWeb(webModel);
        });
        reWeb.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        reWeb.setAdapter(webAdapter);

    }

    private void openWeb(WebModel webModel) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra("link", webModel.getUrl());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_web, menu);
        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;

    }
}