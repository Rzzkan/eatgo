package tech.mlsn.eatgo.fragment.restaurants;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import tech.mlsn.eatgo.R;
public class RestaurantDetailFragment extends Fragment {
    Button btnUpdate;
    ImageView ivBanner;
    TextView tvNameRestaurant, tvAddress;
    ImageButton ivCall, ivChat, ivDirection;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_detail, container, false);
        initialization(view);
        return view;
    }

    private void initialization(View view){
        btnUpdate = view.findViewById(R.id.btnUpdate);
        ivBanner = view.findViewById(R.id.ivBanner);
        tvNameRestaurant = view.findViewById(R.id.tvNameRestaurant);
        tvAddress = view.findViewById(R.id.tvAddress);
        ivCall = view.findViewById(R.id.ivCall);
        ivChat = view.findViewById(R.id.ivChat);
        ivDirection = view.findViewById(R.id.ivDirection);
    }
}