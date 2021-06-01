package tech.mlsn.eatgo.fragment.dashboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.smarteist.autoimageslider.SliderView;

import tech.mlsn.eatgo.R;
public class UserDashboardFragment extends Fragment {
    EditText etSearch;
    Button btnSearch;
    SliderView imgSlider;
    RecyclerView rvDashboard;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_dashboard, container, false);
        initialization(view);
        return view;
    }

    private void initialization(View view){
        etSearch = view.findViewById(R.id.etSearch);
        btnSearch = view.findViewById(R.id.btnSearch);
        imgSlider = view.findViewById(R.id.imageSlider);
        rvDashboard = view.findViewById(R.id.rvDashboard);
    }
}