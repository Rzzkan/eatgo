package tech.mlsn.eatgo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.karan.churi.PermissionManager.PermissionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.fragment.account.ProfileFragment;
import tech.mlsn.eatgo.fragment.chats.AllChatsFragment;
import tech.mlsn.eatgo.fragment.dashboard.AdminDashboardFragment;
import tech.mlsn.eatgo.fragment.dashboard.RestoDasboardFragment;
import tech.mlsn.eatgo.fragment.dashboard.UserDashboardFragment;
import tech.mlsn.eatgo.fragment.orders.AllOrdersFragment;
import tech.mlsn.eatgo.fragment.restaurants.AllRestaurantFragment;
import tech.mlsn.eatgo.fragment.users.AllUsersFragment;
import tech.mlsn.eatgo.network.ApiClient;
import tech.mlsn.eatgo.network.ApiInterface;
import tech.mlsn.eatgo.response.RestaurantInfoResponse;
import tech.mlsn.eatgo.response.dashboard.SlidersResponse;
import tech.mlsn.eatgo.tools.SPManager;
import tech.mlsn.eatgo.tools.SnackbarHandler;
import tech.mlsn.eatgo.tools.Tools;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView navigation;
    PermissionManager permissionManager;

    SPManager spManager;
    SnackbarHandler snackbar;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigation = findViewById(R.id.navigation);
        spManager = new SPManager(this);
        snackbar = new SnackbarHandler(this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        handleRole();
        permissionManager = new PermissionManager() {};
        permissionManager.checkAndRequestPermissions(this);
        if (spManager.getSpRole().equalsIgnoreCase("user")){
            Tools.removeAllFragment(MainActivity.this,new UserDashboardFragment(),"dashboard");
        }else if(spManager.getSpRole().equalsIgnoreCase("resto")){
            Tools.removeAllFragment(MainActivity.this,new RestoDasboardFragment(),"dashboard-resto");
            getRestoInfo();
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
                            getRestoInfo();
                        }else {
                            Tools.removeAllFragment(MainActivity.this,new AdminDashboardFragment(),"dashboard-admin");
                        }
                        break;
//                    case R.id.nav_all_user:
//                        Tools.removeAllFragment(MainActivity.this, new AllUsersFragment(),"users");
//                        break;
//                    case R.id.nav_all_resto:
//                        Tools.removeAllFragment(MainActivity.this, new AllRestaurantFragment(),"resto");
//                        break;
                    case R.id.nav_all_order:
                        Tools.removeAllFragment(MainActivity.this, new AllOrdersFragment(),"order");
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
        }else if(spManager.getSpRole().equalsIgnoreCase("resto")){
        }else{
            navigation.getMenu().getItem(1).setVisible(false);
            navigation.getMenu().getItem(2).setVisible(false);

        }
    }

    private void getRestoInfo(){
        Call<RestaurantInfoResponse> getResto = apiInterface.getRestoInfo(
                spManager.getSpIdResto()
        );

        getResto.enqueue(new Callback<RestaurantInfoResponse>() {
            @Override
            public void onResponse(Call<RestaurantInfoResponse> call, Response<RestaurantInfoResponse> response) {
                if (response.body().getSuccess()==1) {
                    snackbar.snackSuccess("Success");
                    spManager.saveSPString(SPManager.SP_IMG_RESTO,response.body().getData().getImage());
                    spManager.saveSPString(SPManager.SP_ADDRESS_RESTO,response.body().getData().getAddress());
                    spManager.saveSPString(SPManager.SP_PHONE_RESTO,response.body().getData().getPhone());
                    spManager.saveSPString(SPManager.SP_LINK_RESTO,response.body().getData().getLink());
                    spManager.saveSPString(SPManager.SP_NAME_RESTO,response.body().getData().getName());
                } else{
                    snackbar.snackError("Failed");
                }
            }
            @Override
            public void onFailure(Call<RestaurantInfoResponse> call, Throwable t) {
                snackbar.snackInfo("No Connection");
            }
        });
    }
}