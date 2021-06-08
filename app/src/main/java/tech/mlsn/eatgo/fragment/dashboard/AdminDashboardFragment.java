package tech.mlsn.eatgo.fragment.dashboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.smarteist.autoimageslider.SliderView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.adapter.SliderAdapter;
import tech.mlsn.eatgo.network.ApiClient;
import tech.mlsn.eatgo.network.ApiInterface;
import tech.mlsn.eatgo.response.dashboard.SliderDataResponse;
import tech.mlsn.eatgo.response.dashboard.SlidersResponse;
import tech.mlsn.eatgo.tools.SPManager;
import tech.mlsn.eatgo.tools.SnackbarHandler;
import tech.mlsn.eatgo.tools.Tools;

public class AdminDashboardFragment extends Fragment {
    SliderView imgSlider;
    SliderAdapter sliderAdapter;

    Button btnCustomize;
    TextView tvUser, tvRestaurant, tvReviewers, tvMenus;

    SPManager spManager;
    SnackbarHandler snackbar;
    ApiInterface apiInterface;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);
        initialization(view);
        btnListener();
        getSlider();
        getAdminDashboard();
        return view;
    }

    private void initialization(View view){
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        snackbar = new SnackbarHandler(getActivity());
        spManager = new SPManager(getContext());

        imgSlider = view.findViewById(R.id.imageSlider);
        tvUser = view.findViewById(R.id.tvTotalUser);
        tvRestaurant = view.findViewById(R.id.tvTotalRestaurant);
        tvReviewers = view.findViewById(R.id.tvTotalReviews);
        tvMenus = view.findViewById(R.id.tvTotalMenus);

        sliderAdapter = new SliderAdapter(getContext());
        imgSlider.setSliderAdapter(sliderAdapter);
    }

    private void btnListener(){
        btnCustomize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.addFragment(getActivity(),new AllSlidersFragment(),null, "all-slider");
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

    private void getAdminDashboard(){

    }
}