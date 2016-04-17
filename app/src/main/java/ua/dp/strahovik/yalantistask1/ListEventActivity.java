package ua.dp.strahovik.yalantistask1;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import ua.dp.strahovik.yalantistask1.adapters.PagerAdapter;
import ua.dp.strahovik.yalantistask1.services.EventDao;
import ua.dp.strahovik.yalantistask1.services.EventDaoMock;
import ua.dp.strahovik.yalantistask1.util.ToolbarNavigationUtil;

public class ListEventActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private EventDao mEventDao;
    private ActionBar mActionBar;
    private FloatingActionButton mFloatingActionButton;
    private TextView mNavigationFooterLinksTextView;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private NavigationView mNavigationView;
    private ImageButton mIconImage;
    private DrawerLayout mDrawer;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_event);

        initViewsAndData();
//        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//
//        setSupportActionBar(mToolbar);
//        mActionBar = getSupportActionBar();
//        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);

        initFloatingActionButton();
//        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                NOP
//            }
//        });


//        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        initDrawer();
//        mActionBarDrawerToggle = new ActionBarDrawerToggle(
//                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        mActionBarDrawerToggle.setDrawerIndicatorEnabled(false);
//        Drawable modifiedHamburgerIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_menu, getTheme());
//        mActionBarDrawerToggle.setHomeAsUpIndicator(modifiedHamburgerIcon);
//        mActionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mDrawer.isDrawerVisible(GravityCompat.START)) {
//                    mDrawer.closeDrawer(GravityCompat.START);
//                } else {
//                    mDrawer.openDrawer(GravityCompat.START);
//                }
//            }
//        });
//
//        mIconImage = (ImageButton) ToolbarNavigationUtil.getToolbarNavigationIcon(mToolbar);
//        mDrawer.addDrawerListener(mActionBarDrawerToggle);
//        mDrawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
//            @Override
//            public void onDrawerSlide(View drawerView, float slideOffset) {
//                super.onDrawerSlide(drawerView, slideOffset);
//                mIconImage.setRotation(slideOffset * 360);
//            }
//        });
//        mActionBarDrawerToggle.syncState();



//        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

//        mNavigationFooterLinksTextView = (TextView)findViewById(R.id.NavigationView_footer_links);
        mNavigationFooterLinksTextView.setMovementMethod(LinkMovementMethod.getInstance());


//        mEventDao = new EventDaoMock(this);
//        mTabLayout = (TabLayout) findViewById(R.id.list_event_TabLayout);
//        mViewPager = (ViewPager) findViewById(R.id.pager);

        initPager();
//        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),
//                mTabLayout.getTabCount(), getApplicationContext(), mEventDao, mFloatingActionButton);
//        mViewPager.setAdapter(adapter);
//        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
//        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                mViewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });

    }

    private void initFloatingActionButton() {
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                NOP
            }
        });
    }

    private void initPager() {
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),
                mTabLayout.getTabCount(), getApplicationContext(), mEventDao, mFloatingActionButton);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initDrawer() {
        mActionBarDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(false);

//        Set theme drawable instead of HamburgerIcon
        Drawable modifiedHamburgerIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_menu, getTheme());
        mActionBarDrawerToggle.setHomeAsUpIndicator(modifiedHamburgerIcon);

        mActionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawer.isDrawerVisible(GravityCompat.START)) {
                    mDrawer.closeDrawer(GravityCompat.START);
                } else {
                    mDrawer.openDrawer(GravityCompat.START);
                }
            }
        });

//        Adding rotation anim to opening closing of NavView
        mIconImage = (ImageButton) ToolbarNavigationUtil.getToolbarNavigationIcon(mToolbar);
        mDrawer.addDrawerListener(mActionBarDrawerToggle);
        mDrawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            private final int niceRotationCoefficient = getResources().
                    getInteger(R.integer.niceRotationCoefficient);
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                mIconImage.setRotation(slideOffset * niceRotationCoefficient);
            }
        });
        mActionBarDrawerToggle.syncState();
    }


    private void initViewsAndData (){
//        Data
        mEventDao = new EventDaoMock(this);

//        Views
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationFooterLinksTextView = (TextView)findViewById(R.id.NavigationView_footer_links);
        mTabLayout = (TabLayout) findViewById(R.id.list_event_TabLayout);
        mViewPager = (ViewPager) findViewById(R.id.pager);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();
    }

//    @Override
//    public void onBackPressed() {
//        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
//            mDrawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_event, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.navigation_drawer_list_event_all_requests) {
            // Handle the all_requests
        } else if (id == R.id.navigation_drawer_list_event_requests_at_map) {
            // Handle the requests_at_map
        }
        mDrawer.closeDrawer(GravityCompat.START);
//        TODO mb delete this supportInvalidateOptionsMenu();
        supportInvalidateOptionsMenu();
        return true;
    }
}
