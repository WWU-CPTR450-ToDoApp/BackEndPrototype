package edu.wallawalla.dailytodolist;

/* http://stackoverflow.com/questions/34579614/how-to-implement-recyclerview-in-a-fragment-with-tablayout */

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import edu.wallawalla.dailytodolist.db.DBHandler;
import edu.wallawalla.dailytodolist.fragments.BlankFragment;
import edu.wallawalla.dailytodolist.broadcast_receivers.NotificationEventReceiver;
import edu.wallawalla.dailytodolist.db.ToDoTask;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        PagerAdapter pagerAdapter =
                new PagerAdapter(getSupportFragmentManager(), MainActivity.this);
        viewPager.setAdapter(pagerAdapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }
    }
	    
    public void newTask (View view) {
        NotificationEventReceiver.setupAlarm(getApplicationContext(),taskBox.getText().toString());
        ToDoTask task = new ToDoTask(taskBox.getText().toString());
        dbHandler.addTask(task);
        taskBox.setText("");
    }

    public void lookupTask (View view) {
        ToDoTask task = dbHandler.findTask(taskBox.getText().toString());
        if(task != null) {
            idView.setText(String.valueOf(task.getID()));
        } else {
            idView.setText("No Match Found");
    	}

    	// Set default start tab to a certain tab
    	viewPager.setCurrentItem(1);
	}

    @Override
    public void onResume() {
        super.onResume();
    }

    public void addTask(View view) {
        Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + viewPager.getCurrentItem());
        BlankFragment page2 = (BlankFragment) page;
        page2.addTask(view);
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */
}

class PagerAdapter extends FragmentPagerAdapter {

    String tabTitles[] = new String[] { "Done", "Today", "Tomorrow" };
    Context context;

    public PagerAdapter(FragmentManager fm, MainActivity context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        BlankFragment blankFragment = new BlankFragment();

        switch (position) {
            case 0: // DONE
            case 1: // TODAY
            case 2: // TOMORROW
                bundle.putInt("position", position);
                blankFragment.setArguments(bundle);
                return blankFragment;
        }

        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

    public View getTabView(int position) {
        View tab = LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_tab, null);
        TextView tv = (TextView) tab.findViewById(R.id.custom_text);
        tv.setText(tabTitles[position]);
        return tab;
    }
}

public ViewPager getViewPager() {
    return viewPager;
}