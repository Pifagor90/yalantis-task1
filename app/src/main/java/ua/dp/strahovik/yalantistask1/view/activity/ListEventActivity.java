package ua.dp.strahovik.yalantistask1.view.activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.os.ResultReceiver;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.dp.strahovik.yalantistask1.R;
import ua.dp.strahovik.yalantistask1.adapters.PagerAdapter;
import ua.dp.strahovik.yalantistask1.services.PersistAccessTokenService;
import ua.dp.strahovik.yalantistask1.services.SyncEventsService;
import ua.dp.strahovik.yalantistask1.util.ToolbarNavigationUtil;
import ua.dp.strahovik.yalantistask1.view.fragment.ListEventFragment;

public class ListEventActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.NavigationView_footer_links)
    TextView mNavigationFooterLinksTextView;
    @BindView(R.id.pager)
    ViewPager mViewPager;
    @BindView(R.id.list_event_TabLayout)
    TabLayout mTabLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    private ImageButton mIconImage;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private LoginManager mFbLoginManager;
    private CallbackManager mCallbackManager;
    private AccessTokenTracker mAccessTokenTracker;
    private Context mContext;
    private MenuItem mEnterAccountItem;
    private MenuItem mEnterFbProfileItem;
    private MenuItem mEnterFbLogOutItem;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_event);
        mContext = this;
        initViews();
        initNavigationView();
        initPager();
        initFb();
        startService(SyncEventsService.getStartIntent(this, getResources().
                getInteger(R.integer.list_event_activity_default_amount_of_events_to_be_downloaded)));
    }

    private void initFb() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        mAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                if (newAccessToken != null) {
                    mEnterAccountItem.setVisible(false);
                    mEnterFbProfileItem.setVisible(true);
                    mEnterFbLogOutItem.setVisible(true);
                    startService(PersistAccessTokenService.getStartIntent(mContext, newAccessToken));
                } else {
                    mEnterAccountItem.setVisible(true);
                    mEnterFbProfileItem.setVisible(false);
                    mEnterFbLogOutItem.setVisible(false);
                }
            }
        };
        mFbLoginManager = LoginManager.getInstance();
    }


    private void initViews() {
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        Menu navigationViewMenu = mNavigationView.getMenu();
        mEnterAccountItem = navigationViewMenu.findItem(R.id.navigation_drawer_menu_account_enter_account);
        mEnterFbProfileItem = navigationViewMenu.findItem(R.id.navigation_drawer_menu_account_view_fb_profile);
        mEnterFbLogOutItem = navigationViewMenu.findItem(R.id.navigation_drawer_menu_account_log_out);
    }


    private void initNavigationView() {
        mActionBarDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.list_event_activity_navigation_drawer_open,
                R.string.list_event_activity_navigation_drawer_close);
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(false);

//        Set theme drawable instead of HamburgerIcon
        Drawable modifiedHamburgerIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_ab_hamburger, getTheme());
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
                    getInteger(R.integer.list_event_activity_nice_rotation_coefficient);

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                mIconImage.setRotation(slideOffset * niceRotationCoefficient);
            }
        });

        mActionBarDrawerToggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationFooterLinksTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }


    private void initPager() {
        FragmentLoadingStateResultReceiver receiver = new FragmentLoadingStateResultReceiver(new Handler());
        mPagerAdapter = new PagerAdapter(this, getSupportFragmentManager(),
                mTabLayout.getTabCount(), receiver);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//                NOP
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
//                NOP
            }
        });
    }

    public class UpdateFragment implements Runnable {
        private int[] mArrayRequests;
        private boolean mIsLoading;

        public UpdateFragment(int[] arrayRequests, boolean isLoading) {
            mArrayRequests = arrayRequests;
            mIsLoading = isLoading;
        }

        public void run() {
            mPagerAdapter.accept(mArrayRequests, mIsLoading);
            setLoadingStateToExistingFragment(mArrayRequests, mIsLoading);
        }
    }

    private void setLoadingStateToExistingFragment(int[] request, boolean isLoading) {
        for (Fragment fragment  : getSupportFragmentManager().getFragments()){
            try {
                if (Arrays.equals(request, ((ListEventFragment) fragment).getEventStateId())) {
                    ((ListEventFragment) fragment).setIsLoading(isLoading);
                }
            } catch (NullPointerException e){
                continue;
            }
        }
    }

    @SuppressLint("ParcelCreator")
    public class FragmentLoadingStateResultReceiver extends ResultReceiver {
        public static final String ARRAY_REQUEST = "ua.dp.strahovik.yalantistask1.view.activity." +
                "ListEventActivity.FragmentLoadingStateResultReceiver.arrayRequest";
        public static final String IS_LOADING = "ua.dp.strahovik.yalantistask1.view.activity." +
                "ListEventActivity.FragmentLoadingStateResultReceiver.isLoading";

        public FragmentLoadingStateResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            runOnUiThread(new UpdateFragment(resultData.getIntArray(ARRAY_REQUEST),
                    resultData.getBoolean(IS_LOADING)));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAccessTokenTracker.stopTracking();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_event, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.navigation_drawer_list_event_all_requests) {
            // Handle the all_requests
        } else if (id == R.id.navigation_drawer_list_event_requests_at_map) {
            // Handle the requests_at_map
        } else if (id == R.id.navigation_drawer_menu_account_enter_account) {
            logInToFb();
        } else if (id == R.id.navigation_drawer_menu_account_view_fb_profile) {
            startActivity(FbProfileActivity.getStartIntent(this));
        } else if (id == R.id.navigation_drawer_menu_account_log_out) {
            logOutFromFb();
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logOutFromFb() {
        mFbLoginManager.logOut();
        Toast.makeText(getApplicationContext(),
                getString(R.string.list_event_activity_navigation_message_fb_logged_out), Toast.LENGTH_LONG).show();
    }

    private void logInToFb() {
        mCallbackManager = CallbackManager.Factory.create();
        mFbLoginManager.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                startFbServices(loginResult);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {
                Log.e(mContext.getString(R.string.log_tag), e.toString());
            }
        });
        mFbLoginManager.logInWithReadPermissions(this, Arrays.asList(getString(R.string.facebook_permissions)));
    }


    private void startFbServices(LoginResult loginResult) {
        startService(PersistAccessTokenService.getStartIntent(this, loginResult.getAccessToken()));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
