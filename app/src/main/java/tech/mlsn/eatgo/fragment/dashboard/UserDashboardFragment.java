package tech.mlsn.eatgo.fragment.dashboard;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.activity.MainActivity;
import tech.mlsn.eatgo.adapter.SliderAdapter;
import tech.mlsn.eatgo.adapter.UserDashboardAdapter;
import tech.mlsn.eatgo.fragment.account.ProfileFragment;
import tech.mlsn.eatgo.fragment.dashboard.resto.RestaurantProfileFragment;
import tech.mlsn.eatgo.network.ApiClient;
import tech.mlsn.eatgo.network.ApiInterface;
import tech.mlsn.eatgo.response.BaseResponse;
import tech.mlsn.eatgo.response.dashboard.SliderDataResponse;
import tech.mlsn.eatgo.response.dashboard.SlidersResponse;
import tech.mlsn.eatgo.response.restaurant.UserRestaurantDataResponse;
import tech.mlsn.eatgo.response.restaurant.UserRestaurantResponse;
import tech.mlsn.eatgo.tools.SPManager;
import tech.mlsn.eatgo.tools.SnackbarHandler;
import tech.mlsn.eatgo.tools.Tools;

public class UserDashboardFragment extends Fragment  {
    EditText etSearch;
    Button btnSearch;
    SliderView imgSlider;
    RecyclerView rvDashboard;
    Button btnScan;
    TextView tvEG;
    Double lat=0.0, lng=0.0;

    SliderAdapter sliderAdapter;

    SPManager spManager;
    SnackbarHandler snackbar;
    ApiInterface apiInterface;

    UserDashboardAdapter adapter;
    ArrayList<UserRestaurantDataResponse> listResto;

    LocationManager locationManager;
    private static final int REQUEST_LOCATION = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_dashboard, container, false);
        initialization(view);
        getSlider();
        getRestaurant(lat,lng);
        itemClickListener();
        btnListener();
        return view;
    }

    private void initialization(View view){
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        snackbar = new SnackbarHandler(getActivity());
        spManager = new SPManager(getContext());

        etSearch = view.findViewById(R.id.etSearch);
        btnSearch = view.findViewById(R.id.btnSearch);
        imgSlider = view.findViewById(R.id.imageSlider);
        rvDashboard = view.findViewById(R.id.rvDashboard);
        btnScan = view.findViewById(R.id.btnScan);
        tvEG = view.findViewById(R.id.tvEG);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getLocation();
        }

        sliderAdapter = new SliderAdapter(getContext());
        imgSlider.setSliderAdapter(sliderAdapter);
        rvDashboard.setLayoutManager(new LinearLayoutManager(getContext()));
        rvDashboard.setHasFixedSize(true);
        listResto = new ArrayList<>();
        adapter = new UserDashboardAdapter(getContext(), listResto);
        rvDashboard.setAdapter(adapter);
    }

    private void getRestaurant(Double lat, Double lng){
        Call<UserRestaurantResponse> getUserDashboard = apiInterface.getUserDashboard(
                String.valueOf(lat),
                String.valueOf(lng)
        );

        getUserDashboard.enqueue(new Callback<UserRestaurantResponse>() {
            @Override
            public void onResponse(Call<UserRestaurantResponse> call, Response<UserRestaurantResponse> response) {
                if (response.body().getSuccess()==1) {
                    for (int i= 0; i<response.body().getData().size();i++){
                        UserRestaurantDataResponse data = response.body().getData().get(i);
                        listResto.add(new UserRestaurantDataResponse(
                                data.getIdRestaurant(),
                                data.getName(),
                                data.getImage(),
                                data.getPhone(),
                                data.getAddress(),
                                data.getLink(),
                                data.getLatitude(),
                                data.getLongitude(),
                                data.getDistance(),
                                data.getIsActive()
                        ));
                        adapter.notifyDataSetChanged();
                    }
                    snackbar.snackSuccess("Success");
                } else{
                    snackbar.snackError("Failed");
                }
            }
            @Override
            public void onFailure(Call<UserRestaurantResponse> call, Throwable t) {
                snackbar.snackInfo("No Connection");
                Log.d("cek",t.toString());
            }
        });
    }

    private void getSlider(){
        Call<SlidersResponse> getSlider = apiInterface.getAllSliders();
        getSlider.enqueue(new Callback<SlidersResponse>() {
            @Override
            public void onResponse(Call<SlidersResponse> call, Response<SlidersResponse> response) {
                if (response.body().getSuccess()==1) {
                    for (int i=0; i<response.body().getData().size();i++){
                        sliderAdapter.addItem(new SliderDataResponse(
                                response.body().getData().get(i).getIdSlider(),
                                response.body().getData().get(i).getImage()
                        ));

                    }
                    snackbar.snackSuccess("Success");
                } else{
                    snackbar.snackError("Failed");
                }
            }
            @Override
            public void onFailure(Call<SlidersResponse> call, Throwable t) {
                snackbar.snackInfo("No Connection");
            }
        });
    }

    private void getPoint(){
//        Call<SlidersResponse> getSlider = apiInterface.getAllSliders();
//        getSlider.enqueue(new Callback<SlidersResponse>() {
//            @Override
//            public void onResponse(Call<SlidersResponse> call, Response<SlidersResponse> response) {
//                if (response.body().getSuccess()==1) {
//                    for (int i=0; i<response.body().getData().size();i++){
//                        sliderAdapter.addItem(new SliderDataResponse(
//                                response.body().getData().get(i).getIdSlider(),
//                                response.body().getData().get(i).getImage()
//                        ));
//
//                    }
//                    snackbar.snackSuccess("Success");
//                } else{
//                    snackbar.snackError("Failed");
//                }
//            }
//            @Override
//            public void onFailure(Call<SlidersResponse> call, Throwable t) {
//                snackbar.snackInfo("No Connection");
//            }
//        });
    }

    private void btnListener(){
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.addFragment(getActivity(),new ScanFragment(), null,"scan");
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.getFilter().filter(etSearch.getText().toString());
            }
        });
    }


    private void itemClickListener(){
        adapter.setOnItemClickListener(new UserDashboardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, UserRestaurantDataResponse obj, int position) {
                Bundle data = new Bundle();
                data.putString("id_restaurant", obj.getIdRestaurant());
                Tools.addFragment(getActivity(), new RestaurantProfileFragment(), data, "resto-profile");
            }
        });
    }


    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                lat = locationGPS.getLatitude();
                lng = locationGPS.getLongitude();
                snackbar.snackSuccess(lat+", "+lng);
            } else {
                snackbar.snackError("Failed");
            }
        }
    }


}