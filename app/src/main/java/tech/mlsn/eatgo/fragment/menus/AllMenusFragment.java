package tech.mlsn.eatgo.fragment.menus;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.adapter.MenuAdapter;
import tech.mlsn.eatgo.adapter.RestaurantAdapter;
import tech.mlsn.eatgo.network.ApiClient;
import tech.mlsn.eatgo.network.ApiInterface;
import tech.mlsn.eatgo.response.BaseResponse;
import tech.mlsn.eatgo.response.menu.AllMenuDataResponse;
import tech.mlsn.eatgo.response.menu.AllMenuResponse;
import tech.mlsn.eatgo.response.restaurant.RestaurantDataResponse;
import tech.mlsn.eatgo.response.restaurant.RestaurantsResponse;
import tech.mlsn.eatgo.tools.SPManager;
import tech.mlsn.eatgo.tools.SnackbarHandler;
import tech.mlsn.eatgo.tools.Tools;

public class AllMenusFragment extends Fragment {
    EditText etSearch;
    Button btnSearch, btnAddMenu;
    RecyclerView rvMenu;

    MenuAdapter adapter;
    ArrayList<AllMenuDataResponse> listMenu;

    SPManager spManager;
    SnackbarHandler snackbar;
    ApiInterface apiInterface;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_menus, container, false);
        initialization(view);
        getData();
        inputListener();
        clickListener();
        itemClickListener();
        return view;
    }

    private void initialization(View view){
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        snackbar = new SnackbarHandler(getActivity());
        spManager = new SPManager(getContext());

        etSearch  = view.findViewById(R.id.etSearch);
        btnSearch = view.findViewById(R.id.btnSearch);
        btnAddMenu = view.findViewById(R.id.btnAddMenu);
        rvMenu = view.findViewById(R.id.rvMenu);

        rvMenu.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMenu.setHasFixedSize(true);
        listMenu = new ArrayList<>();
        adapter = new MenuAdapter(getContext(), listMenu);
        rvMenu.setAdapter(adapter);
    }

    private void getData(){
        Bundle data = this.getArguments();
        if (data.getBoolean("active")){
            getALlMenuActive(data.getString("id_restaurant"));
            btnAddMenu.setVisibility(View.GONE);
        }else {
            getALlMenu(data.getString("id_restaurant"));
        }

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

        btnAddMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.addFragment(getActivity(), new AddMenuFragment(), null,"add-menu");
            }
        });
    }

    private void itemClickListener(){
        adapter.setOnItemClickListener(new MenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, AllMenuDataResponse obj, int position) {
                Bundle data = new Bundle();
                Tools.addFragment(getActivity(), new DetailMenuFragment(), data, "detail" );
            }
        });

        adapter.setOnEditClickListener(new MenuAdapter.OnEditClickListener() {
            @Override
            public void onItemClick(View view, AllMenuDataResponse obj, int position) {
                Bundle data = new Bundle();
                Tools.addFragment(getActivity(), new UpdateMenuFragment(), data, "update-menu");
            }
        });

        adapter.setOnDeleteClickListener(new MenuAdapter.OnDeleteClickListener() {
            @Override
            public void onItemClick(View view, AllMenuDataResponse obj, int position) {
                showDialogDelete(obj.getIdMenu());
            }
        });
    }

    private void getALlMenu(String id_restaurant){
        Call<AllMenuResponse> getResto = apiInterface.allMenus(
                id_restaurant
        );

        getResto.enqueue(new Callback<AllMenuResponse>() {
            @Override
            public void onResponse(Call<AllMenuResponse> call, Response<AllMenuResponse> response) {
                if (response.body().getSuccess()==1) {
                    snackbar.snackSuccess("Success");
                    for (int i=0; i<response.body().getData().size();i++){
                        AllMenuDataResponse data = response.body().getData().get(i);
                        listMenu.add(new AllMenuDataResponse(
                           data.getIdMenu(),
                           data.getIdRestaurant(),
                           data.getName(),
                           data.getDescription(),
                           data.getCategory(),
                           data.getImage(),
                           data.getPrice(),
                           data.getIsActive()
                        ));
                    }
                    adapter.notifyDataSetChanged();
                } else{
                    snackbar.snackError("Failed");
                }
            }
            @Override
            public void onFailure(Call<AllMenuResponse> call, Throwable t) {
                snackbar.snackInfo("No Connection");
            }
        });
    }

    private void getALlMenuActive(String id_restaurant){
        Call<AllMenuResponse> getResto = apiInterface.allMenusActived(
                id_restaurant
        );

        getResto.enqueue(new Callback<AllMenuResponse>() {
            @Override
            public void onResponse(Call<AllMenuResponse> call, Response<AllMenuResponse> response) {
                if (response.body().getSuccess()==1) {
                    snackbar.snackSuccess("Success");
                    for (int i=0; i<response.body().getData().size();i++){
                        AllMenuDataResponse data = response.body().getData().get(i);
                        listMenu.add(new AllMenuDataResponse(
                                data.getIdMenu(),
                                data.getIdRestaurant(),
                                data.getName(),
                                data.getDescription(),
                                data.getCategory(),
                                data.getImage(),
                                data.getPrice(),
                                data.getIsActive()
                        ));
                    }
                    adapter.notifyDataSetChanged();
                } else{
                    snackbar.snackError("Failed");
                }
            }
            @Override
            public void onFailure(Call<AllMenuResponse> call, Throwable t) {
                snackbar.snackInfo("No Connection");
            }
        });
    }

    private void deleteMenu(String id){
        Call<BaseResponse> getResto = apiInterface.deleteMenu(
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
                deleteMenu(id);
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