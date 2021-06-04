package tech.mlsn.eatgo.fragment.menus;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import tech.mlsn.eatgo.R;
public class AllMenusFragment extends Fragment {
    EditText etSearch;
    Button btnSearch, btnAddMenu;
    RecyclerView rvMenu;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_menus, container, false);
        initialization(view);
        return view;
    }

    private void initialization(View view){
        etSearch  = view.findViewById(R.id.etSearch);
        btnSearch = view.findViewById(R.id.btnSearch);
        btnAddMenu = view.findViewById(R.id.btnAddMenu);
        rvMenu = view.findViewById(R.id.rvMenu);
    }
}