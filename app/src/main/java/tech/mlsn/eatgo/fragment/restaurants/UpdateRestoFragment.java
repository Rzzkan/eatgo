package tech.mlsn.eatgo.fragment.restaurants;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import tech.mlsn.eatgo.R;
public class UpdateRestoFragment extends Fragment {
    Button btnSave;
    TextInputLayout lytLatitude, lytLongitude;
    TextInputEditText etLatitude, etLongitude;
    Switch swActive;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_resto, container, false);
        initialization(view);
        return view;
    }

    private void initialization(View view){
        btnSave = view.findViewById(R.id.btnSave);
        lytLatitude = view.findViewById(R.id.lytLatitude);
        lytLongitude = view.findViewById(R.id.lytLongitude);
        etLatitude = view.findViewById(R.id.etLatitude);
        etLongitude = view.findViewById(R.id.etLogitude);
        swActive = view.findViewById(R.id.swActive);
    }
}