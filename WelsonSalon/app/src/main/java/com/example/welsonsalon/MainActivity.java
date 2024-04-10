package com.example.welsonsalon;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TOOLBAR
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //ViewPager
        sectionPagerAdapter adapter = new sectionPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.ViewPager);
        viewPager.setAdapter(adapter);
        //TabLayout
        TabLayout tabLayout = findViewById(R.id.tab);
        tabLayout.setupWithViewPager(viewPager);

    }
    //for FAB
    private void onClickDone(View view) {
        CoordinatorLayout coordinatorLayout =findViewById(R.id.Coordinator);
        CharSequence text = "You Tabbed on FAB";
        int duration = Snackbar.LENGTH_SHORT;
        Snackbar snackbar = Snackbar.make(coordinatorLayout, text, duration);
        snackbar.setAction("Undo", view1 -> {
            Toast toast = Toast.makeText(MainActivity.this, "Undone!", LENGTH_SHORT);
            toast.show();
        });
        snackbar.show();
    }

    //Menu file for toolbar
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_for_salon,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            if(item.getItemId() == R.id.profile){
                ViewPager viewPager = findViewById(R.id.ViewPager);
                viewPager.setCurrentItem(1);
                Toast.makeText(this, "You are in Profile!", LENGTH_SHORT).show();
            }

        if(item.getItemId() == R.id.Logout){
            auth.signOut();
            Toast.makeText(this, "You have logged out!", LENGTH_SHORT).show();
            Intent intent = new Intent(this,LoginPage.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }



    boolean doubleBackToExitPressedOnce = false;

        //double tab to exit functionality
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    //ViewPager Adapter
    private static class sectionPagerAdapter extends FragmentPagerAdapter {
        public sectionPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new Barber_Fragment();
                case 1:
                    return new Profile_Fragment();
                case 2:
                    return new Contact_us_Fragment();
            }
            return null;
        }
        //get the number of pages
        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch(position){
                case 0:
                    return "Barber";
                case 1:
                    return "Profile";
                case 2:
                    return "Contact us";
            }
            return super.getPageTitle(position);
        }
    }
}