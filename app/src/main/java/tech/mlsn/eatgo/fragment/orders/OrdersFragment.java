package tech.mlsn.eatgo.fragment.orders;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.fragment.dashboard.resto.RestaurantMenuFragment;
import tech.mlsn.eatgo.network.ApiClient;
import tech.mlsn.eatgo.network.ApiInterface;
import tech.mlsn.eatgo.tools.SPManager;
import tech.mlsn.eatgo.tools.SnackbarHandler;
import tech.mlsn.eatgo.tools.Tools;

public class OrdersFragment extends Fragment {
    TextView tvTotal;
    RecyclerView rvOrders;
    Button btnContinue, btnBuy;
    String id_restaurant="", price="";

    SPManager spManager;
    SnackbarHandler snackbar;
    ApiInterface apiInterface;

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

        tvTotal = view.findViewById(R.id.tvTotal);
        rvOrders = view.findViewById(R.id.rvOrders);
        btnContinue = view.findViewById(R.id.btnContinue);
        btnBuy = view.findViewById(R.id.btnBuy);
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
                Bundle data = new Bundle();
                data.putString("price", price);
                Tools.addFragment(getActivity(), new PaymentFragment(), data, "payment");
            }
        });
    }
}