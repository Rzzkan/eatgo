package tech.mlsn.eatgo.fragment.dashboard.resto;

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

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.adapter.MenuAdapter;
import tech.mlsn.eatgo.adapter.MenuUserAdapter;
import tech.mlsn.eatgo.fragment.menus.DetailMenuFragment;
import tech.mlsn.eatgo.fragment.orders.OrdersFragment;
import tech.mlsn.eatgo.network.ApiClient;
import tech.mlsn.eatgo.network.ApiInterface;
import tech.mlsn.eatgo.response.menu.AllMenuDataResponse;
import tech.mlsn.eatgo.response.menu.AllMenuResponse;
import tech.mlsn.eatgo.tools.SPManager;
import tech.mlsn.eatgo.tools.SnackbarHandler;
import tech.mlsn.eatgo.tools.Tools;


public class RestaurantMenuFragment extends Fragment {
    Button btnSearch, btnOrder;
    EditText etSearch ;
    RecyclerView rvMenu;
    MenuAdapter adapter;
    ArrayList<AllMenuDataResponse> list;

    SPManager spManager;
    SnackbarHandler snackbar;
    ApiInterface apiInterface;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_restaurant_menu, container, false);
        initialization(view);
        getData();
        btnListener();
        itemClickListener();
        return view;
    }

    private void initialization(View view){
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        snackbar = new SnackbarHandler(getActivity());
        spManager = new SPManager(getContext());

        btnSearch = view.findViewById(R.id.btnSearch);
        btnOrder = view.findViewById(R.id.btnOrder);
        etSearch = view.findViewById(R.id.etSearch);

        rvMenu = view.findViewById(R.id.rvMenu);
        rvMenu.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMenu.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new MenuAdapter(getContext(), list, spManager.getSpRole());
        rvMenu.setAdapter(adapter);

    }

    private void getData(){
        Bundle data = this.getArguments();
        if (data!=null){
            getALlMenuActive(data.getString("id_restaurant"));
        }
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
                        list.add(new AllMenuDataResponse(
                                data.getIdMenu(),
                                data.getIdRestaurant(),
                                data.getName(),
                                data.getDescription(),
                                data.getCategory(),
                                data.getImage(),
                                data.getPrice(),
                                data.getPointEarned(),
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

    private void btnListener(){
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.getFilter().filter(etSearch.getText().toString());
                adapter.notifyDataSetChanged();
            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.addFragment(getActivity(), new OrdersFragment(), null, "order");
            }
        });

    }

    private void itemClickListener(){
        adapter.setOnItemClickListener(new MenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, AllMenuDataResponse obj, int position) {
                Bundle data = new Bundle();
                data.putString("id_menu", obj.getIdMenu());
                Tools.addFragment(getActivity(), new DetailMenuFragment(), data, "detail" );
            }
        });
    }

}