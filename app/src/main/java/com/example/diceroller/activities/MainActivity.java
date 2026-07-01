package com.example.diceroller.activities;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import com.example.diceroller.R;
import com.example.diceroller.databinding.ActivityMainBinding;
import com.example.diceroller.fragments.AboutFragment;
import com.example.diceroller.fragments.CardsFragment;
import com.example.diceroller.fragments.DiceFragment;
import com.example.diceroller.fragments.HelpFragment;
import com.example.diceroller.fragments.NumbersFragment;
import com.example.diceroller.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        if (savedInstanceState == null) {
            binding.navView.setCheckedItem(R.id.nav_dice);
            replaceFragment(new DiceFragment(), getString(R.string.menu_dice));
        }
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        String title = "";
        
        int id = item.getItemId();
        if (id == R.id.nav_dice) {
            fragment = new DiceFragment();
            title = getString(R.string.menu_dice);
        } else if (id == R.id.nav_cards) {
            fragment = new CardsFragment();
            title = getString(R.string.menu_cards);
        } else if (id == R.id.nav_numbers) {
            fragment = new NumbersFragment();
            title = getString(R.string.menu_numbers);
        } else if (id == R.id.nav_settings) {
            fragment = new SettingsFragment();
            title = getString(R.string.menu_settings);
        } else if (id == R.id.nav_help) {
            fragment = new HelpFragment();
            title = getString(R.string.menu_help);
        } else if (id == R.id.nav_about) {
            fragment = new AboutFragment();
            title = getString(R.string.menu_about);
        }

        if (fragment != null) {
            replaceFragment(fragment, title);
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment fragment, String title) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
