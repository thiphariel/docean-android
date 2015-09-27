package com.thiphariel.docean;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thiphariel.docean.activities.DropletsActivity;
import com.thiphariel.docean.activities.LoginActivity;
import com.thiphariel.docean.activities.SshActivity;
import com.thiphariel.docean.classes.DoceanPreferences;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment implements DrawerAdapter.DrawerClickListener {

    private static final String KEY_DRAWER_USER_KNOWS_EXIST = "user_knows_drawer";
    private static final int[] ICONS = {R.drawable.droplets, /*R.drawable.ssh_keys, R.drawable.settings,*/ R.drawable.logout};
    private static final String[] TITLES = {"Droplets", /*"SSH keys", "Settings",*/ "Logout"};

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private RecyclerView mRecycleView;
    private DrawerAdapter mAdapter;
    private boolean mUserKnowsDrawer;
    private boolean mFromInstanceState;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserKnowsDrawer = Boolean.valueOf(DoceanPreferences.readSharedPreference(getActivity(), KEY_DRAWER_USER_KNOWS_EXIST, "false"));

        if (savedInstanceState != null) {
            mFromInstanceState = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        mRecycleView = (RecyclerView) layout.findViewById(R.id.drawer_recycler_view);
        mAdapter = new DrawerAdapter(getActivity(), getData());
        mAdapter.setDrawerClickListener(this);
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return layout;
    }

    private static List<DrawerViewItem> getData() {
        List<DrawerViewItem> data = new ArrayList<>();

        for (int i = 0; i < TITLES.length && i < ICONS.length; i++) {
            DrawerViewItem item = new DrawerViewItem(ICONS[i], TITLES[i]);
            data.add(item);
        }

        return data;
    }

    /**
     * Setting up our drawer
     *
     * @param drawerLayout
     * @param toolbar
     */
    public void setup(DrawerLayout drawerLayout, Toolbar toolbar) {
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if (!mUserKnowsDrawer) {
                    mUserKnowsDrawer = true;
                    DoceanPreferences.saveToSharedPreference(getActivity(), KEY_DRAWER_USER_KNOWS_EXIST, "true");
                }

                // Redraw Toolbar
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Redraw Toolbar
                getActivity().invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        if (!mUserKnowsDrawer && !mFromInstanceState) {
            //mDrawerLayout.openDrawer(mFragmentView);
        }
    }

    @Override
    public void click(View view, final int position) {
        mDrawerLayout.closeDrawer(Gravity.LEFT);

        // Handler to avoid direct activities change
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                switch (position) {
                    case 0:
                        startActivity(new Intent(getActivity(), DropletsActivity.class));
                        break;
                    /*case 1:
                        startActivity(new Intent(getActivity(), SshActivity.class));
                        break;
                    case 2:

                        break;*/
                    case 1:
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), android.R.style.Theme_DeviceDefault_Dialog_Alert));
                        alertDialogBuilder.setTitle("Logout");
                        alertDialogBuilder.setMessage("Are you sure ?")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DoceanPreferences.saveToSharedPreference(getActivity(), DoceanPreferences.API_TOKEN, "");
                                        startActivity(new Intent(getActivity(), LoginActivity.class));
                                        getActivity().finish();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        break;
                }
            }
        };

        handler.postDelayed(runnable, 500); // here 500 is the delay
    }
}
