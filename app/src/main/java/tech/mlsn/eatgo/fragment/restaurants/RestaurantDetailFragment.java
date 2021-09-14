package tech.mlsn.eatgo.fragment.restaurants;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.adapter.ReviewAdapter;
import tech.mlsn.eatgo.fragment.menus.AllMenusFragment;
import tech.mlsn.eatgo.network.ApiClient;
import tech.mlsn.eatgo.network.ApiInterface;
import tech.mlsn.eatgo.response.RestaurantInfoResponse;
import tech.mlsn.eatgo.response.review.ReviewDataResponse;
import tech.mlsn.eatgo.response.review.ReviewResponse;
import tech.mlsn.eatgo.tools.SPManager;
import tech.mlsn.eatgo.tools.SnackbarHandler;
import tech.mlsn.eatgo.tools.Tools;

import static android.content.Intent.ACTION_VIEW;
import static android.content.Intent.CATEGORY_BROWSABLE;

public class RestaurantDetailFragment extends Fragment {
    Button btnUpdate, btnAllMenu, btnCheckLink;
    ImageView ivBanner;
    TextView tvNameRestaurant, tvAddress;
    RecyclerView rvReview;

    SPManager spManager;
    SnackbarHandler snackbar;
    ApiInterface apiInterface;

    String id_restaurant="", latitude="", longitude="", is_active="", url="";
    ReviewAdapter adapter;
    ArrayList<ReviewDataResponse> listReview;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_detail, container, false);
        initialization(view);
        getData();
        btnListener();
        return view;
    }

    private void initialization(View view){
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        snackbar = new SnackbarHandler(getActivity());
        spManager = new SPManager(getContext());

        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnAllMenu = view.findViewById(R.id.btnAllMenu);
        btnCheckLink = view.findViewById(R.id.btnLink);
        ivBanner = view.findViewById(R.id.ivBanner);
        tvNameRestaurant = view.findViewById(R.id.tvNameRestaurant);
        tvAddress = view.findViewById(R.id.tvAddress);
        rvReview = view.findViewById(R.id.rvReview);

        rvReview = view.findViewById(R.id.rvReview);
        rvReview.setLayoutManager(new LinearLayoutManager(getContext()));
        rvReview.setHasFixedSize(true);
        listReview = new ArrayList<>();
        adapter = new ReviewAdapter(getContext(), listReview);
        rvReview.setAdapter(adapter);
    }

    private void btnListener(){
        btnCheckLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = Uri.encode(url, "UTF-8");
                Intent browserIntent = new Intent(CATEGORY_BROWSABLE, Uri.parse(Uri.decode(query)));
                browserIntent.setAction(ACTION_VIEW);
                startActivity(browserIntent);
            }
        });

        btnAllMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putString("id_restaurant",id_restaurant);
                Tools.addFragment(getActivity(), new AllMenusFragment(), data, "all-menu");
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putString("id_restaurant",id_restaurant);
                data.putString("latitude", latitude);
                data.putString("longitude", longitude);
                data.putString("is_active", is_active);
                Tools.addFragment(getActivity(), new UpdateRestoFragment(), data, "resto-update");
            }
        });
    }

    private void getData(){
        Bundle data = this.getArguments();
        id_restaurant = data.getString("id_restaurant","0");
        getRestoInfo(id_restaurant);
        getReview(id_restaurant);
    }

    private void getRestoInfo(String id){
        Call<RestaurantInfoResponse> getResto = apiInterface.getRestoInfo(
                id
        );

        getResto.enqueue(new Callback<RestaurantInfoResponse>() {
            @Override
            public void onResponse(Call<RestaurantInfoResponse> call, Response<RestaurantInfoResponse> response) {
                if (response.body().getSuccess()==1) {
                    snackbar.snackSuccess("Success");
                    Glide.with(getActivity()).load(ApiClient.BASE_URL+ response.body().getData().getImage()).centerCrop().into(ivBanner);
                    tvNameRestaurant.setText(response.body().getData().getName());
                    tvAddress.setText(response.body().getData().getAddress());
                    latitude = response.body().getData().getLatitude();
                    longitude = response.body().getData().getLongitude();
                    is_active = response.body().getData().getIsActive();
                    url = response.body().getData().getLink();

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

    private void getReview(String id){
        Call<ReviewResponse> getRestoReview = apiInterface.getRestoReview(
                id
        );

        getRestoReview.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                if (response.body().getSuccess()==1) {
                    snackbar.snackSuccess("Success");
                    for (int i=0; i<response.body().getData().size();i++){
                        ReviewDataResponse data = response.body().getData().get(i);
                        listReview.add(new ReviewDataResponse(
                                data.getIdRating(),
                                data.getIdRestaurant(),
                                data.getIdUser(),
                                data.getName(),
                                data.getImage(),
                                data.getRating(),
                                data.getReview()
                        ));
                    }
                    adapter.notifyDataSetChanged();
                } else{
                    snackbar.snackError("Failed");
                }
            }
            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                snackbar.snackInfo("No Connection");
            }
        });
    }
}