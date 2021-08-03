package tech.mlsn.eatgo.fragment.orders;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.adapter.AdapterDetailOrder;
import tech.mlsn.eatgo.model.DetailOrderModel;
import tech.mlsn.eatgo.network.ApiClient;
import tech.mlsn.eatgo.network.ApiInterface;
import tech.mlsn.eatgo.response.BaseResponse;
import tech.mlsn.eatgo.response.DetailOrderResponse;
import tech.mlsn.eatgo.response.OrdersDataResponse;
import tech.mlsn.eatgo.tools.SPManager;
import tech.mlsn.eatgo.tools.SnackbarHandler;
import tech.mlsn.eatgo.tools.Tools;

public class DetailOrderUserFragment extends Fragment {
    TextView tvDate, tvName, tvPayment, tvTotal, name;
    RecyclerView rvDetail;
    Button btnAction;

    String id_order="";
    String status="";

    SnackbarHandler snackbar;
    ApiInterface apiInterface;
    SPManager spManager;

    AdapterDetailOrder adapter;
    ArrayList<DetailOrderModel>listDetail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_order, container, false);
        initialization(view);
        getData();
        btnListener();
        return view;
    }

    private void initialization(View view){
        snackbar = new SnackbarHandler(getActivity());
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        spManager = new SPManager(getContext());

        name = view.findViewById(R.id.name);
        name.setText("Restaurant");
        tvDate = view.findViewById(R.id.tvDate);
        tvName = view.findViewById(R.id.tvName);
        tvPayment = view.findViewById(R.id.tvPayment);
        tvTotal = view.findViewById(R.id.tvTotal);
        btnAction = view.findViewById(R.id.btnAction);

        rvDetail = view.findViewById(R.id.rvDetail);
        rvDetail.setLayoutManager(new LinearLayoutManager(getContext()));

        rvDetail.setHasFixedSize(true);
        listDetail = new ArrayList<>();
        adapter = new AdapterDetailOrder(getContext(), listDetail);
        rvDetail.setAdapter(adapter);
        btnAction.setVisibility(View.GONE);
    }

    private void getData(){
        Bundle data = this.getArguments();
        id_order = data.getString("id_order","0");
        getOrder(id_order);

    }

    private void getOrder(String id){
        Call<DetailOrderResponse> allOrders = apiInterface.getDetailOrderUser(
                id
        );

        allOrders.enqueue(new Callback<DetailOrderResponse>() {
            @Override
            public void onResponse(Call<DetailOrderResponse> call, Response<DetailOrderResponse> response) {
                if (response.body().getSuccess()==1) {
                    OrdersDataResponse data = response.body().getData();
                    status = data.getStatus();
                    if (status.equalsIgnoreCase("1")){
                        btnAction.setText("PAID");
                    }else if (status.equalsIgnoreCase("2")){
                        btnAction.setText("PROCESS");
                    }else if (status.equalsIgnoreCase("3")){
                        btnAction.setText("DONE");
                    }else if (status.equalsIgnoreCase("4")){
                        btnAction.setVisibility(View.GONE);
                    }
                    tvDate.setText(data.getDate());
                    tvName.setText(data.getUserName());
                    tvPayment.setText(data.getPayment());
                    tvTotal.setText("Rp. "+ data.getTotal());
                    for (int i =0 ; i<data.getMenuName().size();i++){
                        listDetail.add(new DetailOrderModel(
                                data.getMenuName().get(i),
                                data.getMenuPrice().get(i),
                                data.getMenuQty().get(i)
                        ));
                    }
                    adapter.notifyDataSetChanged();
                    snackbar.snackSuccess("Success");
                } else{
                    snackbar.snackError("Failed");
                }
            }
            @Override
            public void onFailure(Call<DetailOrderResponse> call, Throwable t) {
                snackbar.snackInfo("No Connection");
            }
        });
    }

    private void btnListener(){
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (status.equalsIgnoreCase("1")){
                   actionDialog("Paid","2");
               }else if (status.equalsIgnoreCase("2")){
                   actionDialog("Process","3");
               }else if (status.equalsIgnoreCase("3")){
                   actionDialog("Done","4");
               }else if (status.equalsIgnoreCase("4")){

               }
            }
        });
    }

    public void actionDialog(String status, String code){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle    ("Set" + status);
        builder.setMessage("Are you sure to set as "+status+" ?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setStatus(code);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setStatus(String code){
        Call<BaseResponse> allOrders = apiInterface.setStatusOrder(
                id_order,
                code
        );

        allOrders.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body().getSuccess()==1) {
                    snackbar.snackSuccess("Success");
                    Tools.removeAllFragment(getActivity(),new OrdersFragment(),"orders");
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