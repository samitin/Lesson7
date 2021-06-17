package ru.samitin.lesson7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        readSettings();
    }

    private void initView(){
        Toolbar toolbar=initToolBar();
        initDrawer(toolbar);
        initButtonBack();
        initButtonSettings();
        initButtonFavorite();
        initButtonMain();
    }
    private Toolbar initToolBar(){
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    // регистрация drawer
    private void initDrawer(Toolbar toolbar) {
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Обработка навигационного меню
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (navigateFragment(id)){
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Обработка выбора пункта меню приложения (активити)
        int id = item.getItemId();

        if (navigateFragment(id)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private boolean navigateFragment(int id) {
        switch (id) {
            case R.id.action_settings:
                addFragment(new SettingsFragment());
                return true;
            case R.id.action_main:
                addFragment(new MainFragment());
                return true;
            case R.id.action_favorite:
                addFragment(new FavoriteFragment());
                return true;
        }
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Здесь определяем меню приложения (активити)
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem search = menu.findItem(R.id.action_search); // поиск пункта меню поиска
       SearchView searchText = (SearchView) search.getActionView(); // строка поиска
        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // реагирует на конец ввода поиска
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
                return true;
            }
            // реагирует на нажатие каждой клавиши
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return true;
    }

    private void initButtonBack(){
        Button buttonBack=findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager=getSupportFragmentManager();
                if (Settings.isBackAsRemove){
                    Fragment fragment=getVisibleFragment(fragmentManager);
                    if (fragment!=null){
                        fragmentManager.beginTransaction().remove(fragment).commit();
                    }
                }else {
                    fragmentManager.popBackStack();
                }
            }
        });
    }
    private void initButtonSettings(){
        Button buttonSettings=findViewById(R.id.buttonSettings);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFragment(new SettingsFragment());
            }
        });
    }
    private void initButtonFavorite(){
        Button buttonFavorite=findViewById(R.id.buttonFavorite);
        buttonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFragment(new FavoriteFragment());
            }
        });
    }
    private void initButtonMain(){
        Button buttonMain=findViewById(R.id.buttonMain);
        buttonMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFragment(new MainFragment());
            }
        });
    }

    private Fragment getVisibleFragment(FragmentManager fragmentManager){
        List<Fragment>fragments=fragmentManager.getFragments();
        int countFragments=fragments.size();
        for (int i=0;i<countFragments;i++){
            Fragment fragment=fragments.get(i);
            if (fragment.isVisible())
                return fragment;
        }
        return null;
    }
    private void addFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        // Удалить видимый фрагмент
        if (Settings.isDeleteBeforeAdd){
            Fragment fragmentToRemove=getVisibleFragment(fragmentManager);
            if (fragmentToRemove!=null)
                fragmentTransaction.remove(fragmentToRemove);
        }
        // Добавить фрагмент
        if (Settings.isAddFragment){
            fragmentTransaction.add(R.id.fragment_container,fragment);
        }else {
            fragmentTransaction.replace(R.id.fragment_container,fragment);
        }
        if (Settings.isBackStack){
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }
    private void readSettings(){
        SharedPreferences sharedPref=getSharedPreferences(Settings.SHARED_PREFERENCE_NAME,MODE_PRIVATE);
        Settings.isBackStack=sharedPref.getBoolean(Settings.IS_BACK_STACK_USED,false);
        Settings.isAddFragment=sharedPref.getBoolean(Settings.IS_ADD_FRAGMENT_USED,true);
        Settings.isBackAsRemove=sharedPref.getBoolean(Settings.IS_BACK_AS_REMOVE_FRAGMENT,true);
        Settings.isDeleteBeforeAdd=sharedPref.getBoolean(Settings.IS_DELETE_FRAGMENT_BEFORE_ADD,false);
    }
}