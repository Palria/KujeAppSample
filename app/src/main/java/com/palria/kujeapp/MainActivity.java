package com.palria.kujeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.palria.kujeapp.SearchActivity;
import com.palria.kujeapp.WelcomeActivity;
import com.palria.kujeapp.adapters.MoreActionsAdapter;
import com.palria.kujeapp.models.MoreActionsModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {

DrawerLayout drawerLayout;
BottomNavigationView bottomNavigationView;
BottomSheetDialog bottomSheetDialog;
TabLayout tabLayout;
FrameLayout allMessagesFrameLayout;

    BottomAppBar bottomAppBar;
    boolean isHomeFragmentOpen = false;
    boolean isUserProfileFragmentOpen = false;
    boolean isExploreFragmentOpen = false;
    boolean isMessagesFragmentOpen = false;
    boolean isMarketFragmentOpen = false;
    boolean isAdvertsFragmentOpen = false;
    FrameLayout homeFrameLayout;
    FrameLayout marketFrameLayout;
    FrameLayout advertsFrameLayout;
    FrameLayout exploreFrameLayout;
    FrameLayout userProfileFrameLayout;

ExtendedFloatingActionButton menuActionButton;
    NavigationView mainNavigationView;
//FrameLayout allCustomersFrameLayout;
    ImageButton drawerOpenerActionButton;
    ImageButton notificationActionButton;
    ImageButton searchActionButton;
    int productsFrameLayoutOpenCounter = 0;
    int allServicesFrameLayoutOpenCounter = 0;
    int userProfileFrameLayoutOpenCounter = 0;
    int allMessagesFrameLayoutOpenCounter = 0;
    int allCollectionsFrameLayoutOpenCounter = 0;
    int allUpdatesFrameLayoutOpenCounter = 0;
//    int allCustomersFrameLayoutOpenCounter =0;
    Menu mainNavigationMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!GlobalValue.isUserLoggedIn()){
            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            startActivity(intent);
            this.finish();
            return;
        }
        setContentView(R.layout.activity_main);
        initUI();
//        GlobalValue.setCurrentBusinessId();
       ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout/*,mainToolbar*/, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        initMoreActions();
//        manageTabLayout();
        manageBottomNavView();
        initNavigationView();
        drawerOpenerActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawerLayout();
            }
        });
//        if(!GlobalValue.isBusinessOwner()) {
        notificationActionButton.setVisibility(View.VISIBLE);
//        }
//        startService(new Intent(this,NotificationHandlerService.class));
        notificationActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), NotificationActivity.class));

            }
        });
        searchActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));

            }
        });
        menuActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.show();

            }
        });
//        postNewActionButton.setOnContextClickListener(new View.OnContextClickListener() {
//            @Override
//            public boolean onContextClick(View view) {
//                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
//                return false;
//            }
//        });
//        postNewActionButton.setOnHoverListener(new View.OnHoverListener() {
//            @Override
//            public boolean onHover(View view, MotionEvent motionEvent) {
//                return false;
//            }
//        });
        GlobalValue.setOnChatsFragmentRefreshTriggeredListener(new GlobalValue.OnRefreshChatsTriggeredListener() {
            @Override
            public void onRefreshTriggered() {
                refreshChatsFragment();
            }
        });

        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
        });
//
//
//        mainNavigationMenu.removeItem(R.id.feedbackId);
//        mainNavigationMenu.getItem(2).setTitle("Item Changed");

    }

    private void initUI(){
        drawerLayout = findViewById(R.id.drawerLayoutId);
        mainNavigationView = findViewById(R.id.mainNavigationViewId);
        drawerOpenerActionButton = findViewById(R.id.drawerOpenerActionButtonId);
        notificationActionButton = findViewById(R.id.notificationActionButtonId);
        searchActionButton = findViewById(R.id.searchActionButtonId);
        bottomNavigationView = findViewById(R.id.bottomNavigationViewId);
        bottomAppBar = findViewById(R.id.bottomAppBar);
        mainNavigationMenu = mainNavigationView.getMenu();
        tabLayout = findViewById(R.id.tabLayoutId);
//        productsFrameLayout = findViewById(R.id.homeFrameLayoutId);
//        allServicesFrameLayout = findViewById(R.id.allServicesFrameLayoutId);
        allMessagesFrameLayout = findViewById(R.id.allMessagesFrameLayoutId);
//        allCollectionsFrameLayout = findViewById(R.id.allCollectionsFrameLayoutId);
//        allUpdatesFrameLayout = findViewById(R.id.allUpdatesFrameLayoutId);
        menuActionButton = findViewById(R.id.menuActionButtonId);
        homeFrameLayout = findViewById(R.id.homeFragment);
        exploreFrameLayout = findViewById(R.id.exploreFrameLayoutId);
        userProfileFrameLayout = findViewById(R.id.userProfileFragment);
        marketFrameLayout = findViewById(R.id.marketFrameLayoutId);
        advertsFrameLayout = findViewById(R.id.advertsFrameLayoutId);
//        currentUserProfile = findViewById(R.id.currentUserProfile);


//        allCustomersFrameLayout = findViewById(R.id.allCustomersFrameLayoutId);


    }
    public void openDrawerLayout(){
//        drawerLayout = findViewById(R.id.mainDrawerLayoutId);
//        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawerLayout.addDrawerListener(actionBarDrawerToggle);
//        actionBarDrawerToggle.syncState();
        drawerLayout.openDrawer(GravityCompat.START);

    }
    private void initNavigationView(){

        mainNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
/*
                if (item.getItemId() == R.id.allOrdersId) {
                    startActivity( GlobalValue.getHostActivityIntent(getApplicationContext(),null,GlobalValue.ORDER_FRAGMENT_TYPE,null));
                }
                if (item.getItemId() == R.id.allRequestsId) {
                    startActivity( GlobalValue.getHostActivityIntent(getApplicationContext(),null,GlobalValue.REQUEST_FRAGMENT_TYPE,null));
                }
                if (item.getItemId() == R.id.allCustomersId) {
                    startActivity( GlobalValue.getHostActivityIntent(getApplicationContext(),null,GlobalValue.CUSTOMERS_FRAGMENT_TYPE,null));
                }
                if (item.getItemId() == R.id.allNotesId) {
                Intent intent = new Intent(MainActivity.this,NotesActivity.class);
                startActivity(intent);

                }
                */
                if (item.getItemId() == R.id.feedbackId) {
                    startActivity(new Intent(getApplicationContext(), SendFeedbackActivity.class));
                }
                if (item.getItemId() == R.id.signOutId) {
                    signOut();
                    MainActivity.this.finish();
                    startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
                    Toast.makeText(getApplicationContext(), "Signed out", Toast.LENGTH_SHORT).show();

                }
//               else if (item.getItemId() == R.id.notificationsId) {
//                    startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
//                }
                else if (item.getItemId() == R.id.aboutId) {
                    startActivity(new Intent(getApplicationContext(), AboutUsActivity.class));
                }
                else if (item.getItemId() == R.id.messageUsId) {
                    startActivity(GlobalValue.goToChatRoom(getApplicationContext(), GlobalValue.getCurrentBusinessId(), getString(R.string.app_name), new ImageView(getApplicationContext())));
                }
//                else if (item.getItemId() == R.id.submitProductId) {
//                    Intent intent = new Intent(getApplicationContext(), PostNewProductActivity.class);
//                    intent.putExtra(GlobalValue.IS_PRODUCT_SUBMISSION, true);
//                    startActivity(intent);
////                    Toast.makeText(getApplicationContext(), "post started//////////////////", Toast.LENGTH_SHORT).show();
//                }
               /*
                if (item.getItemId() == R.id.sendNotificationId) {
                    startActivity(new Intent(getApplicationContext(), CreateNewNotificationActivity.class));
                }
                if (item.getItemId() == R.id.addNewServiceId) {
                    startActivity(new Intent(getApplicationContext(), AddNewServiceActivity.class));
                }
                */
                else if (item.getItemId() == R.id.updateAppId) {

                    try {
                        Intent updateAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName()));
                        updateAppIntent.setPackage("com.android.vending");
                        updateAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        updateAppIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                        updateAppIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(updateAppIntent);
                    } catch (android.content.ActivityNotFoundException activityNotFoundException) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));

                    }
                }
//                    if (item.getItemId() == R.id.aboutId) {
////                    startActivity(new Intent(getApplicationContext(),AboutAppActivity.class));
//
                else if (item.getItemId() == R.id.shareAppId) {
                try {
                    Intent shareAppIntent = new Intent(Intent.ACTION_SEND);
                    shareAppIntent.putExtra(Intent.EXTRA_TEXT, "Download " + getString(R.string.app_name) + " App from Play store:  \r\n" + "https://play.google.com/store/apps/details?id=" + getPackageName() + "  \r\ncurrently available on ANDROID PLATFORM \r\n");

                    shareAppIntent.setType("text/plain");
                    startActivity(shareAppIntent);



                }catch(Exception ignored){}
                }
//                if (item.getItemId() == R.id.signOutId) {
//                    accountSignOutConfirmationDialog.show();
//
//                }
//                if (item.getItemId() == R.id.deleteAccountId) {
//                    accountDeletionConfirmationDialog.show();
//
//                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
    void initMoreActions(){
        ArrayList<MoreActionsModel> moreActionsList = new ArrayList<>();
        moreActionsList.add(new MoreActionsModel("Add product",R.drawable.ic_baseline_store_mall_directory_24));
        moreActionsList.add(new MoreActionsModel("Post Job",R.drawable.ic_baseline_note_add_24));
        moreActionsList.add(new MoreActionsModel("New Advert",R.drawable.ic_baseline_note_add_24));
        moreActionsList.add(new MoreActionsModel("New Inquiry",R.drawable.ic_baseline_help_center_24));
        moreActionsList.add(new MoreActionsModel("Create note",R.drawable.ic_baseline_note_add_24));
        moreActionsList.add(new MoreActionsModel("Notify",R.drawable.ic_baseline_add_alert_24));
        moreActionsList.add(new MoreActionsModel("New service",R.drawable.ic_baseline_miscellaneous_services_24));
        moreActionsList.add(new MoreActionsModel("Notes",R.drawable.ic_baseline_notes_24));
        moreActionsList.add(new MoreActionsModel("Records",R.drawable.ic_baseline_note_add_24));
        moreActionsList.add(new MoreActionsModel("Orders",R.drawable.ic_baseline_local_grocery_store_24));
        moreActionsList.add(new MoreActionsModel("Requests",R.drawable.ic_baseline_help_center_24));
        moreActionsList.add(new MoreActionsModel("People",R.drawable.ic_baseline_people_24));
        moreActionsList.add(new MoreActionsModel("New update",R.drawable.ic_baseline_local_fire_department_24));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false);
        bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
        MoreActionsAdapter moreActionsAdapter = new MoreActionsAdapter(this,moreActionsList,bottomSheetDialog);

        View sheet =  getLayoutInflater().inflate(R.layout.business_owner_more_actions_layout,drawerLayout,false);
        RecyclerView recyclerView  = sheet.findViewById(R.id.moreActionsRecyclerViewId);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(moreActionsAdapter);
        recyclerView.setHasFixedSize(true);
        bottomSheetDialog.setContentView(sheet);
    }
    private void manageBottomNavView(){

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
//                    case R.id.exploreItemId:
//
//                        if (isExploreFragmentOpen) {
//                            //Just set the frame layout visibility
//                            setFrameLayoutVisibility(exploreFrameLayout);
//
//                        } else {
//                            isExploreFragmentOpen = true;
//                            setFrameLayoutVisibility(exploreFrameLayout);
//                            ExploreFragment exploreFragment = new ExploreFragment(bottomAppBar);
//                            Bundle bundle = new Bundle();
//                            exploreFragment.setArguments(bundle);
//                            initFragment(exploreFragment, exploreFrameLayout);
//                        }
//                        return true;
                   case R.id.chatsItemId:

                        if (isMessagesFragmentOpen) {
                            //Just set the frame layout visibility
                            setFrameLayoutVisibility(allMessagesFrameLayout);

                        } else {
                            isMessagesFragmentOpen = true;
                            setFrameLayoutVisibility(allMessagesFrameLayout);
                            AllMessagesFragment allMessagesFragment = new AllMessagesFragment(bottomAppBar);
                            Bundle bundle = new Bundle();
                            allMessagesFragment.setArguments(bundle);
                            initFragment(allMessagesFragment, allMessagesFrameLayout);
                        }
                        return true;


//                return true;
                    case R.id.home_item:
                        if (isHomeFragmentOpen) {
                            //Just set the frame layout visibility
                            setFrameLayoutVisibility(homeFrameLayout);

                        } else {
                            isHomeFragmentOpen = true;
                            setFrameLayoutVisibility(homeFrameLayout);
                            HomeFragment homeFragment = new HomeFragment(bottomAppBar);
                            Bundle bundle = new Bundle();
                            homeFragment.setArguments(bundle);
                            initFragment(homeFragment, homeFrameLayout);

                        }
                        return true;
                    case R.id.marketItemId:
                        if (isMarketFragmentOpen) {
                            //Just set the frame layout visibility
                            setFrameLayoutVisibility(marketFrameLayout);

                        } else {
                            isMarketFragmentOpen = true;

                            setFrameLayoutVisibility(marketFrameLayout);
                            MarketFragment marketFragment = new MarketFragment(bottomAppBar);

                            initFragment(marketFragment, marketFrameLayout);

                        }
                        return true;
                    case R.id.advertsItemId:
                        if (isAdvertsFragmentOpen) {
                            //Just set the frame layout visibility
                            setFrameLayoutVisibility(advertsFrameLayout);

                        } else {
                            isAdvertsFragmentOpen = true;

                            setFrameLayoutVisibility(advertsFrameLayout);
                            AdvertsFragment advertsFragment = new AdvertsFragment(bottomAppBar);

                            initFragment(advertsFragment, advertsFrameLayout);

                        }
                        return true;
                    case R.id.profile_item:
                        if(GlobalValue.isUserLoggedIn()){
                            if (isUserProfileFragmentOpen) {
                                //Just set the frame layout visibility
                                setFrameLayoutVisibility(userProfileFrameLayout);
                            } else {
                                isUserProfileFragmentOpen = true;

                                setFrameLayoutVisibility(userProfileFrameLayout);
                                UserProfileFragment userProfileFragment = new UserProfileFragment(bottomAppBar);
                                Bundle bundle = new Bundle();
                                bundle.putString(GlobalValue.USER_ID, GlobalValue.getCurrentUserId());
                                userProfileFragment.setArguments(bundle);
                                initFragment(userProfileFragment, userProfileFrameLayout);
                            }
                            return true;

                        }else{

                            Intent intent = new Intent(MainActivity.this,SignInActivity.class);
                            startActivity(intent);
//                            super.onBackPressed();

                            return false;
                        }
                }
                return false;
            }

        });
        bottomNavigationView.setSelectedItemId(R.id.home_item);

    }
//
//    public void openFragment(Fragment fragment, FrameLayout frameLayoutToBeReplaced){
//        Bundle bundle = new Bundle();
//        bundle.putBoolean(GlobalValue.IS_FROM_SEARCH_CONTEXT, false);
//        bundle.putString(GlobalValue.USER_ID,GlobalValue.getCurrentUserId());
//        fragment.setArguments(bundle);
//        getSupportFragmentManager().beginTransaction().replace(frameLayoutToBeReplaced.getId(),fragment).commit();
//    }
//
//    public void setLayoutVisibility(FrameLayout layoutToBeVisible){
//
//        productsFrameLayout.setVisibility(View.GONE);
//        allServicesFrameLayout.setVisibility(View.GONE);
//        allMessagesFrameLayout.setVisibility(View.GONE);
//        userProfileFrameLayout.setVisibility(View.GONE);
//        allCollectionsFrameLayout.setVisibility(View.GONE);
//        allUpdatesFrameLayout.setVisibility(View.GONE);
////        allCustomersFrameLayout.setVisibility(View.GONE);
//        layoutToBeVisible.setVisibility(View.VISIBLE);
//    }


    private void initFragment(Fragment fragment,FrameLayout frameLayout){
        FragmentManager fragmentManager = getSupportFragmentManager();
        try {
            fragmentManager.beginTransaction()
                    .replace(frameLayout.getId(), fragment)
                    .commit();
        }catch(Exception e){}

    }

    private void setFrameLayoutVisibility(FrameLayout frameLayoutToSetVisible){
        homeFrameLayout.setVisibility(View.GONE);
        exploreFrameLayout.setVisibility(View.GONE);
        userProfileFrameLayout.setVisibility(View.GONE);
        marketFrameLayout.setVisibility(View.GONE);
        advertsFrameLayout.setVisibility(View.GONE);
        allMessagesFrameLayout.setVisibility(View.GONE);
        frameLayoutToSetVisible.setVisibility(View.VISIBLE);
    }

    void refreshChatsFragment(){
        Bundle bundle = new Bundle();

        allMessagesFrameLayoutOpenCounter = 1;
        initFragment(new AllMessagesFragment(), allMessagesFrameLayout);
        setFrameLayoutVisibility(allMessagesFrameLayout);
    }
    private void signOut(){
        FirebaseAuth.getInstance().signOut();

    }

    void paste(){
//         new ModelClasses.UserProfileModel("Nnamdi Henry");
//        "package_name": "com.anchorallianceglobal.aaghomesproperty"

    }
}