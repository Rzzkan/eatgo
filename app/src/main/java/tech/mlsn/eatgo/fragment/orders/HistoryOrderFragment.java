package tech.mlsn.eatgo.fragment.orders;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tech.mlsn.eatgo.R;
public class HistoryOrderFragment extends Fragment {
    RecyclerView rvHistory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_order, container, false);
        initialization(view);
        return view;
    }

    private void initialization(View view){

    }
}