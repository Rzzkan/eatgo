package tech.mlsn.eatgo.fragment.orders;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.adapter.AdapterCart;
import tech.mlsn.eatgo.fragment.dashboard.resto.RestaurantMenuFragment;
import tech.mlsn.eatgo.model.CartModel;
import tech.mlsn.eatgo.network.ApiClient;
import tech.mlsn.eatgo.network.ApiInterface;
import tech.mlsn.eatgo.tools.SPManager;
import tech.mlsn.eatgo.tools.SQLiteDBHelper;
import tech.mlsn.eatgo.tools.SnackbarHandler;
import tech.mlsn.eatgo.tools.Tools;

public class OrdersFragment extends Fragment {
    TextView tvTotal;
    RecyclerView rvOrders;
    Button btnContinue, btnBuy;
    String id_restaurant="", price="";

    AdapterCart adapter;
    ArrayList<CartModel> list;

    SPManager spManager;
    SnackbarHandler snackbar;
    ApiInterface apiInterface;
    SQLiteDBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        initialization(view);
        getData();
        btnListener();
        return view;
    }

    private void  initialization(View view){
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        snackbar = new SnackbarHandler(getActivity());
        spManager = new SPManager(getContext());
        dbHelper = new SQLiteDBHelper(getActivity());

        tvTotal = view.findViewById(R.id.tvTotal);
        rvOrders = view.findViewById(R.id.rvOrders);
        btnContinue = view.findViewById(R.id.btnContinue);
        btnBuy = view.findViewById(R.id.btnBuy);

        list = new ArrayList<>();
        list = dbHelper.getCartData();
        adapter = new AdapterCart(getActivity(),getContext(), list);
        rvOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        rvOrders.setHasFixedSize(true);
        rvOrders.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Log.d("cek data", String.valueOf(list.size()));
        countInvoice();
    }

    private void getData(){
        Bundle data = this.getArguments();
        if (data!=null){
            id_restaurant = data.getString("id_restaurant");
        }
    }

    private void btnListener(){
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putString("id_restaurant", id_restaurant);
                Tools.addFragment(getActivity(), new RestaurantMenuFragment(),data,"menu");
            }
        });

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dbHelper.countTotal()!=0){
                    Bundle data = new Bundle();
                    data.putString("id_restaurant", id_restaurant);
                    Tools.addFragment(getActivity(), new PaymentFragment(), data, "payment");
                }else {
                    snackbar.snackInfo("Have to choose menu !");
                }

            }
        });

        adapter.setOnItemClickListener(new AdapterCart.OnItemClickListener() {
            @Override
            public void onItemClick(View view, CartModel obj, int position) {

            }

            @Override
            public void onSubsClick(View view, CartModel obj, int total, int position) {
                if (total==0){
                    deleteDialog(obj);
                }else{
                    dbHelper.updateCart(new CartModel(
                            obj.getId_cart(),
                            obj.getId_restaurant(),
                            obj.getId_menu(),
                            obj.getName(),
                            total,
                            obj.getPrice(),
                            obj.getPrice()*total,
                            obj.getImg(),
                            obj.getDesc()
                    ));
                    countInvoice();
                }
            }

            @Override
            public void onAddClick(View view, CartModel obj, int total, int position) {
                dbHelper.updateCart(new CartModel(
                        obj.getId_cart(),
                        obj.getId_restaurant(),
                        obj.getId_menu(),
                        obj.getName(),
                        total,
                        obj.getPrice(),
                        obj.getPrice()*total,
                        obj.getImg(),
                        obj.getDesc()
                ));
                countInvoice();
            }
        });

    }

    private void countInvoice(){
        tvTotal.setText(String.valueOf(dbHelper.countTotal()));
    }

    public void deleteDialog(CartModel cart){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle    ("Confirmation");
        builder.setCancelable(false);
        builder.setMessage("Order deleted !");
//        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dbHelper.deleteCart(cart);
                Bundle data = new Bundle();
                data.putString("id_restaurant", id_restaurant);
                Tools.addFragment(getActivity(), new RestaurantMenuFragment(),data, "cart");
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}