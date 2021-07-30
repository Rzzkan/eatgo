package tech.mlsn.eatgo.fragment.dashboard;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.adapter.MenuAdapter;
import tech.mlsn.eatgo.adapter.RestaurantAdapter;
import tech.mlsn.eatgo.adapter.SliderViewAdapter;
import tech.mlsn.eatgo.network.ApiClient;
import tech.mlsn.eatgo.network.ApiInterface;
import tech.mlsn.eatgo.response.BaseResponse;
import tech.mlsn.eatgo.response.dashboard.SliderDataResponse;
import tech.mlsn.eatgo.response.dashboard.SlidersResponse;
import tech.mlsn.eatgo.response.menu.AllMenuDataResponse;
import tech.mlsn.eatgo.tools.SPManager;
import tech.mlsn.eatgo.tools.SnackbarHandler;
import tech.mlsn.eatgo.tools.Tools;

public class AllSlidersFragment extends Fragment {
    RecyclerView rvSlider;
    Button btnAdd;

    SPManager spManager;
    SnackbarHandler snackbar;
    ApiInterface apiInterface;

    SliderViewAdapter adapter;
    ArrayList<SliderDataResponse> listSlider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_sliders, container, false);
        initialization(view);
        btnListener();
        getSlider();
        itemClickListener();
        return view;
    }

    private void initialization(View view){
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        snackbar = new SnackbarHandler(getActivity());
        spManager = new SPManager(getContext());

        btnAdd = view.findViewById(R.id.btnAdd);
        rvSlider = view.findViewById(R.id.rvSlider);

        rvSlider.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSlider.setHasFixedSize(true);
        listSlider = new ArrayList<>();
        adapter = new SliderViewAdapter(getContext(), listSlider);
        rvSlider.setAdapter(adapter);

    }
    private void btnListener(){
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Tools.addFragment(getActivity(), new AddSliderFragment(), null, "add-slider");
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
                        listSlider.add(new SliderDataResponse(
                                response.body().getData().get(i).getIdSlider(),
                                response.body().getData().get(i).getImage()
                        ));

                    }
                    adapter.notifyDataSetChanged();
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

    private void itemClickListener(){
        adapter.setOnDeleteClickListener(new SliderViewAdapter.OnDeleteClickListener() {
            @Override
            public void onItemClick(View view, SliderDataResponse obj, int position) {
                showDialogDelete(obj.getIdSlider());
            }
        });
    }

    private void deleteSlider(String id){
        Call<BaseResponse> getResto = apiInterface.deleteslider(
                id
        );

        getResto.enqueue(new Callback<BaseResponse>() {
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

    private void showDialogDelete(String id){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_delete_menu);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        final Button btnYes = dialog.findViewById(R.id.btnYes);
        final Button btnNo = dialog.findViewById(R.id.btnNo);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSlider(id);
                dialog.dismiss();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}