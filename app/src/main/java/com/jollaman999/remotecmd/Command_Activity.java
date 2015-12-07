package com.jollaman999.remotecmd;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;
import java.net.Socket;

public class Command_Activity extends AppCompatActivity {

    private DrawerLayout drawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;

    static Socket socket;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.command_main);

        mContext = getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Command - Input");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setCheckedItem(R.id.nav_input);
        mNavigationView.setNavigationItemSelectedListener(new NavViewItemListener());

        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_layout, Fragment.instantiate(getApplicationContext(), "com.jollaman999.remotecmd.Command_Input"));
        ft.commit();
    }

    @Override
    public void onDestroy() {
        closeSocket();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private class NavViewItemListener implements NavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            int id = item.getItemId();
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            switch (id) {
                case R.id.nav_input:
                    mNavigationView.setCheckedItem(R.id.nav_input);
                    ft.replace(R.id.fragment_layout, Fragment.instantiate(getApplicationContext(), "com.jollaman999.remotecmd.Command_Input"));
                    ft.commit();
                    break;
                case R.id.nav_shutdown:
                    mNavigationView.setCheckedItem(R.id.nav_shutdown);
                    ft.replace(R.id.fragment_layout, Fragment.instantiate(getApplicationContext(), "com.jollaman999.remotecmd.Command_Shutdown"));
                    ft.commit();
                    break;
                case R.id.nav_schedule:

                    break;
                case R.id.nav_chat:
                    mNavigationView.setCheckedItem(R.id.nav_chat);
                    ft.replace(R.id.fragment_layout, Fragment.instantiate(getApplicationContext(), "com.jollaman999.remotecmd.Chatting_Login"));
                    ft.commit();
                    break;
            }
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
    }

    private static void closeSocket() {
        try {
            socket.close();
            Socket_Control.is_connected = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        Socket_Control.ResetMain();

        Toast.makeText(RemoteCMD.context,
                RemoteCMD.context.getString(R.string.command_disconnect),
                Toast.LENGTH_SHORT).show();
    }
}