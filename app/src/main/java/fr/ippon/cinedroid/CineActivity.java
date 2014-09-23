package fr.ippon.cinedroid;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import fr.ippon.cinedroid.listener.DrawerItemClickListener;


public class CineActivity extends Activity {

    private String[] menuTitles;
    private DrawerLayout drawerLayout;
    private ListView drawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cine);

        // Navigable Drawer
        menuTitles = getResources().getStringArray(R.array.menu_array);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        drawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, menuTitles));

        // Set the adapter for the list view
        drawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, menuTitles));

        // Set the list's click listener
        drawerList.setOnItemClickListener(new DrawerItemClickListener(this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cine, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String[] getMenuTitles() {
        return menuTitles;
    }

    public void setMenuTitles(String[] menuTitles) {
        this.menuTitles = menuTitles;
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public void setDrawerLayout(DrawerLayout drawerLayout) {
        this.drawerLayout = drawerLayout;
    }

    public ListView getDrawerList() {
        return drawerList;
    }

    public void setDrawerList(ListView drawerList) {
        this.drawerList = drawerList;
    }
}
