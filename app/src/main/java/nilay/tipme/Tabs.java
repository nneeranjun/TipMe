package nilay.tipme;


import android.content.Context;

import android.content.res.ColorStateList;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;




import org.apache.http.Header;


public class Tabs extends AppCompatActivity {
private Toolbar mToolBar;
    private TabLayout tabLayout;
    private MyPagerAdapter myPagerAdapter;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);
        mToolBar = (Toolbar)findViewById(R.id.app_bar);

        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(),getApplicationContext());
        setSupportActionBar(mToolBar);
        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        viewPager= (ViewPager)findViewById(R.id.pager);
        viewPager.setAdapter(myPagerAdapter);

        tabLayout.setTabsFromPagerAdapter(myPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);



        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tabs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
class MyPagerAdapter extends FragmentStatePagerAdapter {
    Context context;
    public MyPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;



    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:

                fragment = new MakePayment();



                break;
            case 1:
                fragment = new ViewPayment();
                break;
            case 2:
                fragment = new ViewReceipts();
                break;
            case 3:
                fragment = new ViewProfile();



        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

   



    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence title = null;
       switch (position) {
            case 0:
                title = "Make a Payment";
                break;
            case 1:
                title= "View Payments";
                break;
            case 2:
                title = "View Receipts";
                break;
            case 3:
                title="Profile";
                break;

        }


    return title;
    }
}



