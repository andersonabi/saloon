package com.example.cityguide.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.cityguide.Common.LoginSignup.RetailerStartUpScreen;
import com.example.cityguide.HelperClasses.HomeAdapter.FeaturedAdapter;
import com.example.cityguide.HelperClasses.HomeAdapter.FeaturedHelperClass;
import com.example.cityguide.R;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class UserDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    static final float END_SCALE = 0.7f;

    RecyclerView featuredRecycler;
    RecyclerView.Adapter adapter;


    private GradientDrawable gradient1;

    ImageView menuIcon;
    LinearLayout contentView;

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_dashboard);

        featuredRecycler = findViewById(R.id.featured_recycler);
        contentView = findViewById(R.id.content);

        menuIcon = findViewById(R.id.menu_icon);


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);


        navigationDrawer();


        featuredRecycler();
    }

    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);


        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START))

                    drawerLayout.closeDrawer(GravityCompat.START);

                else drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        animateNavigationDrawer();


    }

    private void animateNavigationDrawer() {

             drawerLayout.setScrimColor(getResources().getColor(R.color.colorPrimary));
              drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                  @Override
                  public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

                      final float diffScaleOffset = slideOffset * (1- END_SCALE);
                      final float offsetScale = 1 - diffScaleOffset;
                      contentView.setScaleX(offsetScale);
                      contentView.setScaleY(offsetScale);

                      final float xOffset = drawerView.getWidth() * slideOffset;
                      final float xOffsetDiff = contentView.getWidth() * diffScaleOffset / 2;
                      final float xTranslation = xOffset - xOffsetDiff;
                      contentView.setTranslationX(xTranslation);


                  }

              });
    }


    public void callRetailerScreens(View view)
    {
        startActivity(new Intent(getApplicationContext(), RetailerStartUpScreen.class));
    }


    @Override
    public void onBackPressed() {

        if(drawerLayout.isDrawerVisible(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else

        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.nav_all_categories:

            startActivity(new Intent(getApplicationContext(),AllCategories.class));
            break;
        }

        return true;
    }

    private void featuredRecycler() {
        featuredRecycler.setHasFixedSize(true);
        featuredRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<FeaturedHelperClass> featuredLocations = new ArrayList<>();

        featuredLocations.add(new FeaturedHelperClass(R.drawable.mcdonald_img, "Mcdonald's", "ashdj ahsdkjh asdjhas jdhas jdhas jdhal shdash"));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.city_1, "Edenrobe", "ashdj ahsdkjh asdjhas jdhas jdhas jdhal shdash"));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.city_2, "Sweet and Bakers", "ashdj ahsdkjh asdjhas jdhas jdhas jdhal shdash"));


        adapter = new FeaturedAdapter(featuredLocations);
        featuredRecycler.setAdapter(adapter);


        GradientDrawable gradient1 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xffeff400, 0xffaff600});

    }


}