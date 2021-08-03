package tech.mlsn.eatgo.fragment.orders;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.adapter.AdapterOrder;
import tech.mlsn.eatgo.network.ApiClient;
import tech.mlsn.eatgo.network.ApiInterface;
import tech.mlsn.eatgo.response.OrdersDataResponse;
import tech.mlsn.eatgo.response.OrdersResponse;
import tech.mlsn.eatgo.tools.SPManager;
import tech.mlsn.eatgo.tools.SnackbarHandler;
import tech.mlsn.eatgo.tools.Tools;

public class HistoryOrderFragment extends Fragment {
    RecyclerView rvHistory;
    AdapterOrder adapter;
    ArrayList<OrdersDataResponse> listOrder;

    SnackbarHandler snackbar;
    ApiInterface apiInterface;
    SPManager spManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_order, container, false);
        initialization(view);
        getData();
        clickListener();
        return view;
    }

    private void initialization(View view){
        snackbar = new SnackbarHandler(getActivity());
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        spManager = new SPManager(getContext());

        rvHistory = view.findViewById(R.id.rvHistory);
        rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        rvHistory.setHasFixedSize(true);
        listOrder = new ArrayList<>();
        adapter = new AdapterOrder(getContext(), listOrder);
        rvHistory.setAdapter(adapter);
    }

    private void  getData(){
        Call<OrdersResponse> allOrders = apiInterface.getOrderUser(
                spManager.getSpId()
        );

        allOrders.enqueue(new Callback<OrdersResponse>() {
            @Override
            public void onResponse(Call<OrdersResponse> call, Response<OrdersResponse> response) {
                if (response.body().getSuccess()==1) {
                    for (int i=0; i<response.body().getData().size();i++){
                        OrdersDataResponse data = response.body().getData().get(i);
                        listOrder.add(new OrdersDataResponse(
                                data.getIdOrder(),
                                data.getIdRestaurant(),
                                data.getIdUser(),
                                data.getUserName(),
                                data.getUserPhone(),
                                data.getMenuName(),
                                data.getMenuPrice(),
                                data.getMenuQty(),
                                data.getTotal(),
                                data.getStatus(),
                                data.getPayment(),
                                data.getDate()
                        ));
                    }
                    adapter.notifyDataSetChanged();
                    snackbar.snackSuccess("Success");
                } else{
                    snackbar.snackError("Failed");
                }
            }
            @Override
            public void onFailure(Call<OrdersResponse> call, Throwable t) {
                snackbar.snackInfo("No Connection");
            }
        });
    }

    private void clickListener(){
        adapter.setOnItemClickListener(new AdapterOrder.OnItemClickListener() {
            @Override
            public void onItemClick(View view, OrdersDataResponse obj, int position) {
                Bundle data = new Bundle();
                data.putString("id_order", obj.getIdOrder());
                Tools.addFragment(getActivity(), new DetailOrderUserFragment(), data, "detail");
            }
        });
    }
}