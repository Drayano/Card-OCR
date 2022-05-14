package com.drayano.card_ocr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.drayano.card_ocr.traitement.ExportCSV;

import java.io.IOException;

public class MainActivity extends AppCompatActivity
{
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;

    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Mettre ma Toolbar pour remplacer ActionBar
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Afficher up icon;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Trouver notre drawer view
        mDrawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();
        mDrawer.addDrawerListener(drawerToggle);

        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);
        Class FragmentClass;
        Fragment fragment = null;

        try
        {
            FragmentClass = MainFragment.class;
            fragment = (Fragment) FragmentClass.newInstance();
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }

    private ActionBarDrawerToggle setupDrawerToggle()
    {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView)
    {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener()
                {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem)
                    {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                }
        );
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);

        // Sync the toggle state after onRestoreInstanceState has occurred
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);

        // Passer tout les changements au drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    public void selectDrawerItem(MenuItem menuItem)
    {
        Fragment fragment = null;
        Class fragmentClass;

        switch (menuItem.getItemId())
        {
            // Switcher entre les fragment qu'on souhaite affichée
            case R.id.btn_home:
                fragmentClass = MainFragment.class;
                break;

            case R.id.listeEntreprise:
                fragmentClass = listeFragment.class;
                break;


            case R.id.export_excel:
                try
                {
                    Uri CsvUri;
                    ExportCSV exportCSV = new ExportCSV(getApplicationContext(),this,this);
                    CsvUri = exportCSV.exportCsv();
                    Intent shareCSV = new Intent();
                    shareCSV.setAction(Intent.ACTION_SEND);
                    shareCSV.putExtra(Intent.EXTRA_STREAM, CsvUri);
                    shareCSV.setType("text/csv");
                    startActivity(Intent.createChooser(shareCSV, getResources().getText(R.string.send_to)));
                }

                catch (IOException e)
                {
                    e.printStackTrace();
                }

            case R.id.logout:
                SharedPreferences userDetails = getSharedPreferences("userDetails", MODE_PRIVATE);
                SharedPreferences.Editor edit = userDetails.edit();
                edit.clear();
                edit.commit();

                Intent toLogin = new Intent(MainActivity.this,LoginActivity.class);
                toLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(toLogin);

            default:
                fragmentClass = MainFragment.class;
        }

        try
        {
            fragment = (Fragment) fragmentClass.newInstance();
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

        // Inserer le fragment par remplacement du fragment existant
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        menuItem.setChecked(true);

        // Mettre le titre par rapport a l'element selectionee
        setTitle(menuItem.getTitle());

        // Fermer le menu drawer
        mDrawer.closeDrawers();
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        //Le button home/up ouvre et ferme notre drawer
//        if (drawerToggle.onOptionsItemSelected(item)){
//            return true;
//        }
////        switch (item.getItemId()){
////            case android.R.id.home:
////                mDrawer.openDrawer(GravityCompat.START);
////                return true;
////        }
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    public void finish()
    {
        super.finish();
        finishAffinity();
    }
}
