package tech.mlsn.eatgo.fragment.dashboard.resto;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import tech.mlsn.eatgo.R;
public class RestaurantProfileFragment extends Fragment {
    ImageView ivBanner;
    TextView tvNameRestaurant, tvAddress;
    Button btnAllmenu;
    ImageButton ibCall, ibChat, ibDirection;
    RecyclerView rvReview;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_profile, container, false);
        initialization(view);
        return view;
    }

    private void initialization(View view){
        ivBanner = view.findViewById(R.id.ivBanner);
        tvNameRestaurant = view.findViewById(R.id.tvNameRestaurant);
        tvAddress = view.findViewById(R.id.tvAddress);
        btnAllmenu = view.findViewById(R.id.btnAllMenu);
        ibCall = view.findViewById(R.id.ivCall);
        ibChat = view.findViewById(R.id.ivChat);
        ibDirection = view.findViewById(R.id.ivDirection);

        rvReview = view.findViewById(R.id.rvReview);
    }
}