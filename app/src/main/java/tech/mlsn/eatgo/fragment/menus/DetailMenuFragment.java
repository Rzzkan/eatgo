package tech.mlsn.eatgo.fragment.menus;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.network.ApiClient;
import tech.mlsn.eatgo.network.ApiInterface;
import tech.mlsn.eatgo.response.menu.AllMenuDataResponse;
import tech.mlsn.eatgo.response.menu.MenuResponse;
import tech.mlsn.eatgo.tools.SPManager;
import tech.mlsn.eatgo.tools.SnackbarHandler;

public class DetailMenuFragment extends Fragment {
    TextView tvName, tvDescription, tvPrice, tvCategory;
    ImageView ivMenu;

    SPManager spManager;
    SnackbarHandler snackbar;
    ApiInterface apiInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_menu, container, false);
        initialization(view);
        getData();
        return view;
    }

    private void initialization(View view){
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        snackbar = new SnackbarHandler(getActivity());
        spManager = new SPManager(getContext());

        ivMenu = view.findViewById(R.id.ivMenu);
        tvName = view.findViewById(R.id.tvName);
        tvDescription = view.findViewById(R.id.tvDescription);
        tvPrice = view.findViewById(R.id.tvPrice);
        tvCategory = view.findViewById(R.id.tvCategory);
    }

    private void getData(){
        Bundle data = this.getArguments();
        getMenu(data.getString("id_menu","0"));
    }

    private void getMenu(String id){
            Call<MenuResponse> getMenu = apiInterface.getMenu(
                    id
            );

            getMenu.enqueue(new Callback<MenuResponse>() {
                @Override
                public void onResponse(Call<MenuResponse> call, Response<MenuResponse> response) {
                    if (response.body().getSuccess()==1) {
                        snackbar.snackSuccess("Success");
                        AllMenuDataResponse data = response.body().getData();
                        tvName.setText(data.getName());
                        tvDescription.setText(data.getDescription());
                        tvPrice.setText(data.getPrice());
                        tvCategory.setText(data.getCategory());
                    } else{
                        snackbar.snackError("Failed");
                    }
                }
                @Override
                public void onFailure(Call<MenuResponse> call, Throwable t) {
                    snackbar.snackInfo("No Connection");
                }
            });

    }

}