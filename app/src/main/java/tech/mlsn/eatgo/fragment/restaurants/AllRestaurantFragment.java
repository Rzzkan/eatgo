package tech.mlsn.eatgo.fragment.restaurants;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.adapter.RestaurantAdapter;
import tech.mlsn.eatgo.adapter.ReviewAdapter;
import tech.mlsn.eatgo.network.ApiClient;
import tech.mlsn.eatgo.network.ApiInterface;
import tech.mlsn.eatgo.response.RestaurantInfoResponse;
import tech.mlsn.eatgo.response.restaurant.RestaurantDataResponse;
import tech.mlsn.eatgo.response.restaurant.RestaurantsResponse;
import tech.mlsn.eatgo.response.review.ReviewDataResponse;
import tech.mlsn.eatgo.tools.SPManager;
import tech.mlsn.eatgo.tools.SnackbarHandler;
import tech.mlsn.eatgo.tools.Tools;

public class AllRestaurantFragment extends Fragment {
    EditText etSearch;
    Button btnSearch;
    RecyclerView rvRestaurant;

    RestaurantAdapter adapter;
    ArrayList<RestaurantDataResponse> listRestaurant;

    SPManager spManager;
    SnackbarHandler snackbar;
    ApiInterface apiInterface;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_restaurant, container, false);
        initialization(view);
        getALlRestaurant();
        inputListener();
        itemClickListener();
        clickListener();
        return view;
    }

    private void initialization(View view){
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        snackbar = new SnackbarHandler(getActivity());
        spManager = new SPManager(getContext());

        etSearch = view.findViewById(R.id.etSearch);
        btnSearch = view.findViewById(R.id.btnSearch);
        rvRestaurant = view.findViewById(R.id.rvRestaurant);

        rvRestaurant.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRestaurant.setHasFixedSize(true);
        listRestaurant = new ArrayList<>();
        adapter = new RestaurantAdapter(getContext(), listRestaurant);
        rvRestaurant.setAdapter(adapter);
    }

    private void inputListener(){
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void clickListener(){
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.getFilter().filter(etSearch.getText().toString());
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void getALlRestaurant(){
        Call<RestaurantsResponse> getResto = apiInterface.getAllRestaurants();

        getResto.enqueue(new Callback<RestaurantsResponse>() {
            @Override
            public void onResponse(Call<RestaurantsResponse> call, Response<RestaurantsResponse> response) {
                if (response.body().getSuccess()==1) {
                    snackbar.snackSuccess("Success");
                    for (int i=0; i<response.body().getData().size();i++){
                        RestaurantDataResponse data = response.body().getData().get(i);
                        listRestaurant.add(new RestaurantDataResponse(
                                data.getIdRestaurant(),
                                data.getName(),
                                data.getImage(),
                                data.getPhone(),
                                data.getAddress(),
                                data.getLink(),
                                data.getLatitude(),
                                data.getLongitude(),
                                data.getIsActive()
                        ));
                    }
                    adapter.notifyDataSetChanged();
                } else{
                    snackbar.snackError("Failed");
                }
            }
            @Override
            public void onFailure(Call<RestaurantsResponse> call, Throwable t) {
                snackbar.snackInfo("No Connection");
            }
        });
    }

    private void itemClickListener(){
        adapter.setOnItemClickListener(new RestaurantAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RestaurantDataResponse obj, int position) {
                Bundle data = new Bundle();
                data.putString("id_restaurant",obj.getIdRestaurant());
                Tools.addFragment(getActivity(), new RestaurantDetailFragment(),data,"detailrestaurant");
            }
        });
    }
}