package tech.mlsn.eatgo.fragment.dashboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.adapter.ReviewAdapter;
import tech.mlsn.eatgo.fragment.barcode.ViewBarcodeFragment;
import tech.mlsn.eatgo.fragment.menus.AllMenusFragment;
import tech.mlsn.eatgo.network.ApiClient;
import tech.mlsn.eatgo.network.ApiInterface;
import tech.mlsn.eatgo.response.BaseResponse;
import tech.mlsn.eatgo.response.RestaurantInfoResponse;
import tech.mlsn.eatgo.response.review.ReviewDataResponse;
import tech.mlsn.eatgo.response.review.ReviewResponse;
import tech.mlsn.eatgo.tools.SPManager;
import tech.mlsn.eatgo.tools.SnackbarHandler;
import tech.mlsn.eatgo.tools.Tools;

public class RestoDasboardFragment extends Fragment {
    ImageView ivBanner;
    TextView tvNameRestaurant, tvAddress;
    Button btnAllmenu, btnReview, btnBarcode;
    RecyclerView rvReview;
    Switch swOpen;


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
        btnListener();
        swListener();
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
        btnBarcode = view.findViewById(R.id.btnBarcode);
        swOpen = view.findViewById(R.id.swOpen);

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
                    tvNameRestaurant.setText(response.body().getData().getName());
                    tvAddress.setText(response.body().getData().getAddress());
                    if (response.body().getData().getIsActive().equalsIgnoreCase("1")){
                        swOpen.setText("Close Restaurant");
                        swOpen.setChecked(true);
                    }else {
                        swOpen.setText("Open Restaurant");
                        swOpen.setChecked(false);
                    }
                    if(getActivity()!=null){
                        Glide.with(getActivity()).load(ApiClient.BASE_URL+ spManager.getSpImgResto()).centerCrop().into(ivBanner);
                    }
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

    private void btnListener(){
        btnAllmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putString("id_restaurant",spManager.getSpIdResto());
                Tools.addFragment(getActivity(),new AllMenusFragment(), data, "all-menu");
            }
        });

        btnBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.addFragment(getActivity(), new ViewBarcodeFragment(), null, "barcode");
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

    private void swListener(){
        swOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if (isChecked){
                   setOpen("1");
                   swOpen.setText("Close Restaurant");
               }else {
                   swOpen.setText("Open Restaurant");
                   setOpen("0");
               }
            }
        });
    }


    private void setOpen(String status){
        Call<BaseResponse> setOpenResto = apiInterface.setActive(
                spManager.getSpIdResto(),
                status
        );

        setOpenResto.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body().getSuccess()==1) {
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
}