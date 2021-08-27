package tech.mlsn.eatgo.fragment.orders;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.adapter.AdapterOrder;
import tech.mlsn.eatgo.network.ApiClient;
import tech.mlsn.eatgo.network.ApiInterface;
import tech.mlsn.eatgo.response.BaseResponse;
import tech.mlsn.eatgo.response.OrdersDataResponse;
import tech.mlsn.eatgo.response.OrdersResponse;
import tech.mlsn.eatgo.tools.SPManager;
import tech.mlsn.eatgo.tools.SnackbarHandler;
import tech.mlsn.eatgo.tools.Tools;

public class AllOrdersFragment extends Fragment {
    Button tabNewOrder,tabPaidOrder,tabProcessOrder,tabDoneOrder;
//    RecyclerView rvNew, rvPaid, rvProcess, rvDone;
    RecyclerView rvOrder;
    SnackbarHandler snackbar;
    ApiInterface apiInterface;
    SPManager spManager;

    String status = "1";

    AdapterOrder adapter;
    ArrayList<OrdersDataResponse> listOrder;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_all_orders, container, false);
        initialization(view);
        getData();
        clickListener();
        return view;
    }

    private void initialization(View view){
        snackbar = new SnackbarHandler(getActivity());
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        spManager = new SPManager(getContext());

        tabNewOrder = view.findViewById(R.id.tabNewOrder);
        tabPaidOrder = view.findViewById(R.id.tabPaidOrder);
        tabProcessOrder = view.findViewById(R.id.tabProcessOrder);
        tabDoneOrder = view.findViewById(R.id.tabDoneOrder);

        tabNewOrder.setOnClickListener(new tabListener(tabNewOrder));
        tabProcessOrder.setOnClickListener(new tabListener(tabProcessOrder));
        tabPaidOrder.setOnClickListener(new tabListener(tabPaidOrder));
        tabDoneOrder.setOnClickListener(new tabListener(tabDoneOrder));

//        rvNew = view.findViewById(R.id.rvNewOrder);
//        rvPaid = view.findViewById(R.id.rvPaidOrder);
//        rvProcess = view.findViewById(R.id.rvProcessOrder);
//        rvDone = view.findViewById(R.id.rvDoneOrder);
        rvOrder = view.findViewById(R.id.rvOrder);
        rvOrder.setLayoutManager(new LinearLayoutManager(getContext()));
        rvOrder.setHasFixedSize(true);
        listOrder = new ArrayList<>();
        adapter = new AdapterOrder(getContext(), listOrder);
        rvOrder.setAdapter(adapter);
    }

    private void getData(){
        Call<OrdersResponse> allOrders = apiInterface.getAllOrders();

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
//                    rvOrder.setAdapter(adapter);
                    adapter.getFilter().filter(status);
                    adapter.notifyDataSetChanged();
                    Log.d("cek order",String.valueOf(adapter.getItemCount()));
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

    private class tabListener implements View.OnClickListener{
        Button btn;

        public tabListener(Button btn) {
            this.btn = btn;
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tabNewOrder:
                    tabNewOrder.setBackground(getActivity().getDrawable(R.drawable.btn_rounded));
                    tabNewOrder.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.white));

                    tabPaidOrder.setBackground(getActivity().getDrawable(R.drawable.btn_rounded_outline));
                    tabPaidOrder.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.red_apple));

                    tabProcessOrder.setBackground(getActivity().getDrawable(R.drawable.btn_rounded_outline));
                    tabProcessOrder.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.red_apple));

                    tabDoneOrder.setBackground(getActivity().getDrawable(R.drawable.btn_rounded_outline));
                    tabDoneOrder.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.red_apple));
                    status = "1";
                    adapter.getFilter().filter(status);
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.tabPaidOrder:

                    tabNewOrder.setBackground(getActivity().getDrawable(R.drawable.btn_rounded_outline));
                    tabNewOrder.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.red_apple));

                    tabPaidOrder.setBackground(getActivity().getDrawable(R.drawable.btn_rounded));
                    tabPaidOrder.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.white));

                    tabProcessOrder.setBackground(getActivity().getDrawable(R.drawable.btn_rounded_outline));
                    tabProcessOrder.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.red_apple));

                    tabDoneOrder.setBackground(getActivity().getDrawable(R.drawable.btn_rounded_outline));
                    tabDoneOrder.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.red_apple));
                    status = "2";
                    adapter.getFilter().filter(status);
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.tabProcessOrder:
                    tabNewOrder.setBackground(getActivity().getDrawable(R.drawable.btn_rounded_outline));
                    tabNewOrder.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.red_apple));

                    tabPaidOrder.setBackground(getActivity().getDrawable(R.drawable.btn_rounded_outline));
                    tabPaidOrder.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.red_apple));

                    tabProcessOrder.setBackground(getActivity().getDrawable(R.drawable.btn_rounded));
                    tabProcessOrder.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.white));

                    tabDoneOrder.setBackground(getActivity().getDrawable(R.drawable.btn_rounded_outline));
                    tabDoneOrder.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.red_apple));
                    status = "3";
                    adapter.getFilter().filter(status);
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.tabDoneOrder:
                    tabNewOrder.setBackground(getActivity().getDrawable(R.drawable.btn_rounded_outline));
                    tabNewOrder.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.red_apple));

                    tabPaidOrder.setBackground(getActivity().getDrawable(R.drawable.btn_rounded_outline));
                    tabPaidOrder.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.red_apple));

                    tabProcessOrder.setBackground(getActivity().getDrawable(R.drawable.btn_rounded_outline));
                    tabProcessOrder.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.red_apple));

                    tabDoneOrder.setBackground(getActivity().getDrawable(R.drawable.btn_rounded));
                    tabDoneOrder.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.white));
                    status = "4";
                    Log.d("cek_status", status);
                    adapter.getFilter().filter(status);
//                    rvOrder.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;

            }
        }
    }

    private void clickListener(){
        adapter.setOnItemClickListener(new AdapterOrder.OnItemClickListener() {
            @Override
            public void onItemClick(View view, OrdersDataResponse obj, int position) {
                Bundle data = new Bundle();
                data.putString("id_order", obj.getIdOrder());
                Tools.addFragment(getActivity(), new DetailOrderFragment(), data, "detail");
            }
        });
    }

}