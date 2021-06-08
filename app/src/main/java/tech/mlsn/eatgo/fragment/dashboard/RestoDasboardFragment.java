package tech.mlsn.eatgo.fragment.dashboard;

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
import tech.mlsn.eatgo.network.ApiClient;
import tech.mlsn.eatgo.network.ApiInterface;
import tech.mlsn.eatgo.response.RestaurantInfoResponse;
import tech.mlsn.eatgo.response.review.ReviewDataResponse;
import tech.mlsn.eatgo.response.review.ReviewResponse;
import tech.mlsn.eatgo.tools.SPManager;
import tech.mlsn.eatgo.tools.SnackbarHandler;

public class RestoDasboardFragment extends Fragment {
    ImageView ivBanner;
    TextView tvNameRestaurant, tvAddress;
    Button btnAllmenu, btnReview;
    RecyclerView rvReview;


    SPManager spManager;
    SnackbarHandler snackbar;
    ApiInterface apiInterface;

    ReviewAdapter adapter;
    ArrayList<ReviewDataResponse> listReview;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_resto_dasboard, container, false);
        initialization(view);
        return view;
    }

    private void initialization(View view){
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        snackbar = new SnackbarHandler(getActivity());
        spManager = new SPManager(getContext());

        ivBanner = view.findViewById(R.id.ivBanner);
        tvNameRestaurant = view.findViewById(R.id.tvNameRestaurant);
        tvAddress = view.findViewById(R.id.tvAddress);
        btnAllmenu = view.findViewById(R.id.btnAllMenu);
        btnReview = view.findViewById(R.id.btnReview);

        rvReview = view.findViewById(R.id.rvReview);
        rvReview.setLayoutManager(new LinearLayoutManager(getContext()));
        rvReview.setHasFixedSize(true);
        listReview = new ArrayList<>();
        adapter = new ReviewAdapter(getContext(), listReview);
        rvReview.setAdapter(adapter);

        getRestoInfo(spManager.getSpIdResto());
        getReview(spManager.getSpIdResto());
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