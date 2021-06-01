package tech.mlsn.eatgo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.karan.churi.PermissionManager.PermissionManager;

import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.fragment.account.ProfileFragment;
import tech.mlsn.eatgo.fragment.chats.AllChatsFragment;
import tech.mlsn.eatgo.fragment.dashboard.AdminDashboardFragment;
import tech.mlsn.eatgo.fragment.dashboard.RestoDasboardFragment;
import tech.mlsn.eatgo.fragment.dashboard.UserDashboardFragment;
import tech.mlsn.eatgo.fragment.restaurants.AllRestaurantFragment;
import tech.mlsn.eatgo.fragment.users.AllUsersFragment;
import tech.mlsn.eatgo.tools.SPManager;
import tech.mlsn.eatgo.tools.Tools;

public class MainActivity extends AppCompatActivity {
    SPManager spManager;
    private BottomNavigationView navigation;
    PermissionManager permissionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigation = findViewById(R.id.navigation);
        handleRole();
        spManager = new SPManager(this);
        permissionManager = new PermissionManager() {};
        permissionManager.checkAndRequestPermissions(this);
        if (spManager.getSpRole().equalsIgnoreCase("user")){
            Tools.removeAllFragment(MainActivity.this,new UserDashboardFragment(),"dashboard");
        }else if(spManager.getSpRole().equalsIgnoreCase("resto")){
            Tools.removeAllFragment(MainActivity.this,new RestoDasboardFragment(),"dashboard-resto");
        }else {
            Tools.removeAllFragment(MainActivity.this,new AdminDashboardFragment(),"dashboard-admin");
        }
        navigationListener();
    }

    private void navigationListener(){
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.nav_dashboard:
                        if (spManager.getSpRole().equalsIgnoreCase("user")){
                            Tools.removeAllFragment(MainActivity.this,new UserDashboardFragment(),"dashboard");
                        }else if(spManager.getSpRole().equalsIgnoreCase("resto")){
                            Tools.removeAllFragment(MainActivity.this,new RestoDasboardFragment(),"dashboard-resto");
                        }else {
                            Tools.removeAllFragment(MainActivity.this,new AdminDashboardFragment(),"dashboard-admin");
                        }
                        break;
                    case R.id.nav_all_user:
                        Tools.removeAllFragment(MainActivity.this, new AllUsersFragment(),"users");
                        break;
                    case R.id.nav_all_resto:
                        Tools.removeAllFragment(MainActivity.this, new AllRestaurantFragment(),"resto");
                        break;
                    case R.id.nav_chat:
                        Tools.removeAllFragment(MainActivity.this, new AllChatsFragment(),"chat");
                        break;
                    case R.id.nav_account:
                        Tools.removeAllFragment(MainActivity.this, new ProfileFragment(),"account");
                        break;
                }
                return true;
            }
        });
    }


    private void handleRole(){
        if (spManager.getSpRole().equalsIgnoreCase("user")){
            navigation.getMenu().getItem(1).setVisible(false);
            navigation.getMenu().getItem(2).setVisible(false);
        }else if(spManager.getSpRole().equalsIgnoreCase("resto")){
            navigation.getMenu().getItem(1).setVisible(true);
            navigation.getMenu().getItem(2).setVisible(true);
        }else{
            navigation.getMenu().getItem(1).setVisible(true);
            navigation.getMenu().getItem(2).setVisible(true);
            navigation.getMenu().getItem(3).setVisible(false);

        }
    }
}