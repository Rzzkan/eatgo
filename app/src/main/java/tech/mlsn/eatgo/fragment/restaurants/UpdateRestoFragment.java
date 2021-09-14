package tech.mlsn.eatgo.fragment.restaurants;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.network.ApiClient;
import tech.mlsn.eatgo.network.ApiInterface;
import tech.mlsn.eatgo.response.BaseResponse;
import tech.mlsn.eatgo.response.review.ReviewDataResponse;
import tech.mlsn.eatgo.response.review.ReviewResponse;
import tech.mlsn.eatgo.tools.SPManager;
import tech.mlsn.eatgo.tools.SnackbarHandler;
import tech.mlsn.eatgo.tools.Tools;

public class UpdateRestoFragment extends Fragment {
    Button btnSave;
    TextInputLayout lytLatitude, lytLongitude;
    TextInputEditText etLatitude, etLongitude;
    Switch swActive;

    SPManager spManager;
    SnackbarHandler snackbar;
    ApiInterface apiInterface;

    String id_restaurant="0", is_active ="0", latitude ="", longitude="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_resto, container, false);
        initialization(view);
        btnListener();
        switchListener();
        getData();
        return view;
    }

    private void initialization(View view){
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        snackbar = new SnackbarHandler(getActivity());
        spManager = new SPManager(getContext());

        btnSave = view.findViewById(R.id.btnSave);
        lytLatitude = view.findViewById(R.id.lytLatitude);
        lytLongitude = view.findViewById(R.id.lytLongitude);
        etLatitude = view.findViewById(R.id.etLatitude);
        etLongitude = view.findViewById(R.id.etLogitude);
        swActive = view.findViewById(R.id.swActive);
    }

    private void getData(){
        Bundle data = this.getArguments();
        id_restaurant = data.getString("id_restaurant","0");
        latitude = data.getString("latitude","");
        longitude = data.getString("longitude","");
        is_active = data.getString("is_active","0");

        etLatitude.setText(latitude);
        etLongitude.setText(longitude);

        if (is_active.equalsIgnoreCase("0")){
            swActive.setChecked(false);
        }else {
            swActive.setChecked(true);
        }
    }

    private void switchListener(){
        swActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    is_active ="1";
                }else {
                    is_active ="0";
                }
            }
        });
    }

    private void btnListener(){
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    updateRestaurant();
                }
            }
        });
    }


    private void updateRestaurant(){
        Call<BaseResponse> updateRestaurantAdmin = apiInterface.updateRestaurantAdmin(
                id_restaurant,
                etLatitude.getText().toString(),
                etLongitude.getText().toString(),
                is_active
        );

        updateRestaurantAdmin.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body().getSuccess()==1) {
                    Tools.removeAllFragment(getActivity(), new AllRestaurantFragment(), "resto");
                    snackbar.snackSuccess("Success");
                } else{
                    snackbar.snackError("Failed");
                }
            }
            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                snackbar.snackInfo("No Connection");
            }
        });
    }


    private Boolean validate(){
        Boolean valid = true;

        if (etLatitude.getText().toString().isEmpty()){
            snackbar.snackInfo("Latitude Cannot Be Empty");
            return false;
        }

        if (etLongitude.getText().toString().isEmpty()){
            snackbar.snackInfo("Longitude Cannot Be Empty");
            return false;
        }

        return valid;
    }
}