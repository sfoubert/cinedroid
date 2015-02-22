package fr.ippon.cinedroid.listener;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import fr.ippon.cinedroid.CineActivity;
import fr.ippon.cinedroid.R;
import fr.ippon.cinedroid.fragment.CineFragment;

/**
 * Created by sfoubert on 23/09/2014.
 */
public class DrawerItemClickListener implements ListView.OnItemClickListener {

    private CineActivity cineActivity;

    private String[] menuTitles;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private FragmentManager fragmentManager;


    public DrawerItemClickListener(CineActivity cineActivity) {
        this.cineActivity = cineActivity;
        this.menuTitles = cineActivity.getMenuTitles();
        this.drawerLayout = cineActivity.getDrawerLayout();
        this.drawerList = cineActivity.getDrawerList();
        this.fragmentManager = cineActivity.getFragmentManager();
    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        selectItem(position);
    }

    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment = new CineFragment();
        Bundle args = new Bundle();

        args.putInt(CineFragment.MENU_SELECTED, position);
        fragment.setArguments(args);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment).commit();

        // Highlight the selected item, update the title, and close the drawer
        drawerList.setItemChecked(position, true);
        cineActivity.setTitle(menuTitles[position]);
        drawerLayout.closeDrawer(drawerList);
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }
}
