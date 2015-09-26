package com.thiphariel.docean;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment {

    private static final String SHARED_PREFERENCE_FILE_NAME = "docean_pref";
    private static final String KEY_DRAWER_USER_KNOWS_EXIST = "user_knows_drawer";

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View mFragmentView;
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

        mUserKnowsDrawer = Boolean.valueOf(readSharedPreference(getActivity(), KEY_DRAWER_USER_KNOWS_EXIST, "false"));

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
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return layout;
    }

    private static List<DrawerViewItem> getData() {
        List<DrawerViewItem> data = new ArrayList<>();

        int[] icons = {R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
        String[] titles = {"Droplets", "SSH keys", "Settings", "Logout"};

        for (int i = 0; i < titles.length && i < icons.length; i++) {
            DrawerViewItem item = new DrawerViewItem(icons[i], titles[i]);
            data.add(item);
        }

        return data;
    }

    /**
     * Setting up our drawer
     * @param drawerLayout
     * @param toolbar
     */
    public void setup(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {
        mFragmentView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if (!mUserKnowsDrawer) {
                    mUserKnowsDrawer = true;
                    saveToSharedPreference(getActivity(), KEY_DRAWER_USER_KNOWS_EXIST, "true");
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
            mDrawerLayout.openDrawer(mFragmentView);
        }
    }

    /**
     * Allow to save values in shared preferences
     * @param context
     * @param key
     * @param value
     */
    public static void saveToSharedPreference(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(key, value);
        editor.apply();
    }

    /**
     * Retrieve data from shared preferences
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static String readSharedPreference(Context context, String key, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }
}
