package tech.mlsn.eatgo.fragment.dashboard.resto;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.adapter.ReviewAdapter;
import tech.mlsn.eatgo.adapter.UserDashboardAdapter;
import tech.mlsn.eatgo.fragment.chats.ChatDetailFragment;
import tech.mlsn.eatgo.fragment.dashboard.UserDashboardFragment;
import tech.mlsn.eatgo.fragment.menus.AllMenusFragment;
import tech.mlsn.eatgo.network.ApiClient;
import tech.mlsn.eatgo.network.ApiInterface;
import tech.mlsn.eatgo.response.BaseResponse;
import tech.mlsn.eatgo.response.RestaurantInfoResponse;
import tech.mlsn.eatgo.response.restaurant.UserRestaurantDataResponse;
import tech.mlsn.eatgo.response.review.ReviewDataResponse;
import tech.mlsn.eatgo.response.review.ReviewResponse;
import tech.mlsn.eatgo.tools.SPManager;
import tech.mlsn.eatgo.tools.SnackbarHandler;
import tech.mlsn.eatgo.tools.Tools;

public class RestaurantProfileFragment extends Fragment {
    ImageView ivBanner;
    TextView tvNameRestaurant, tvAddress;
    Button btnAllmenu, btnReview;
    ImageButton ibCall, ibChat, ibDirection;
    RecyclerView rvReview;


    SPManager spManager;
    SnackbarHandler snackbar;
    ApiInterface apiInterface;

    String id_restaurant="", phone="";
    String id_user_restaurant ="";
    String  latitude="0", longitude="0";
    String rate="0";

    ReviewAdapter adapter;
    ArrayList<ReviewDataResponse> listReview;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_profile, container, false);
        initialization(view);
        getData();
        btnListener();
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
        ibCall = view.findViewById(R.id.ivCall);
        ibChat = view.findViewById(R.id.ivChat);
        ibDirection = view.findViewById(R.id.ivDirection);
        btnReview = view.findViewById(R.id.btnReview);

        rvReview = view.findViewById(R.id.rvReview);
        rvReview.setLayoutManager(new LinearLayoutManager(getContext()));
        rvReview.setHasFixedSize(true);
        listReview = new ArrayList<>();
        adapter = new ReviewAdapter(getContext(), listReview);
        rvReview.setAdapter(adapter);

    }

    private void getData(){
        Bundle data = getArguments();
        id_restaurant = data.getString("id_restaurant","0");
        getRestoInfo(id_restaurant);
        getReview(id_restaurant);
    }

    private void btnListener(){
        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogReview();
            }
        });

        btnAllmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putString("id_restaurant",id_restaurant);
                Tools.addFragment(getActivity(), new RestaurantMenuFragment(),data,"menus");
            }
        });

        ibDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strUri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude;
                Intent in = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
                in.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(in);
            }
        });

        ibChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putString("id_restaurant",id_restaurant);
                data.putString("id_from",spManager.getSpId());
                data.putString("id_to",id_user_restaurant);
                data.putBoolean("condition", true);
                Tools.addFragment(getActivity(), new ChatDetailFragment(),data,"chat");
            }
        });

        ibCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "0"+phone));
                startActivity(intent);
            }
        });
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
                    phone = response.body().getData().getPhone();
                    latitude = response.body().getData().getLatitude();
                    longitude = response.body().getData().getLongitude();
                    id_user_restaurant = response.body().getData().getIdUser();
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

    private void postReview(String id_user, String id_restaurant ,String rating, String review){
        Call<BaseResponse> postRestoReview = apiInterface.postAddReview(
                id_user,
                id_restaurant,
                rating,
                review
        );

        postRestoReview.enqueue(new Callback<BaseResponse>() {
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

    private void showDialogReview(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_review);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        final RatingBar rating = dialog.findViewById(R.id.ratingBar);
        final EditText etReview = dialog.findViewById(R.id.etReview);
        final Button btnAction = dialog.findViewById(R.id.btnAction);
        final ImageButton btnClose = dialog.findViewById(R.id.btnClose);

        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rate = String.valueOf(rating);
            }
        });

        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rate.equalsIgnoreCase("0")&&!etReview.getText().toString().isEmpty()){
                    postReview(spManager.getSpId(),id_restaurant,rate,etReview.getText().toString());
                    dialog.dismiss();
                    Tools.removeAllFragment(getActivity(),new UserDashboardFragment(), "dashboard");
                }else {
                    snackbar.snackError("Cant Empty");
                }
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}