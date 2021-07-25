package tech.mlsn.eatgo.fragment.orders;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.network.ApiClient;
import tech.mlsn.eatgo.network.ApiInterface;
import tech.mlsn.eatgo.tools.SPManager;
import tech.mlsn.eatgo.tools.SnackbarHandler;

public class AllOrdersFragment extends Fragment {
    Button tabNewOrder,tabPaidOrder,tabProcessOrder,tabDoneOrder;
    RecyclerView rvNew, rvPaid, rvProcess, rvDone;
    SnackbarHandler snackbar;
    ApiInterface apiInterface;
    SPManager spManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_all_orders, container, false);
        initialization(view);
        getData();
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

        rvNew = view.findViewById(R.id.rvNewOrder);
        rvPaid = view.findViewById(R.id.rvPaidOrder);
        rvProcess = view.findViewById(R.id.rvProcessOrder);
        rvDone = view.findViewById(R.id.rvDoneOrder);
    }

    private void getData(){

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
                    break;

            }
        }
    }

}