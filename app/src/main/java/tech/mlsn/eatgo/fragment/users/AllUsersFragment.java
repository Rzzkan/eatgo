package tech.mlsn.eatgo.fragment.users;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.adapter.RestaurantAdapter;
import tech.mlsn.eatgo.adapter.UsersAdapter;
import tech.mlsn.eatgo.fragment.restaurants.RestaurantDetailFragment;
import tech.mlsn.eatgo.network.ApiClient;
import tech.mlsn.eatgo.network.ApiInterface;
import tech.mlsn.eatgo.response.restaurant.RestaurantDataResponse;
import tech.mlsn.eatgo.response.restaurant.RestaurantsResponse;
import tech.mlsn.eatgo.response.user.UserDataResponse;
import tech.mlsn.eatgo.response.user.UsersResponse;
import tech.mlsn.eatgo.tools.SPManager;
import tech.mlsn.eatgo.tools.SnackbarHandler;
import tech.mlsn.eatgo.tools.Tools;

public class AllUsersFragment extends Fragment {
    EditText etSearch;
    Button btnSearch;
    RecyclerView rvUser;

    UsersAdapter adapter;
    ArrayList<UserDataResponse> listUser;

    SPManager spManager;
    SnackbarHandler snackbar;
    ApiInterface apiInterface;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_users, container, false);
        initialization(view);
        getAllUsers();
        inputListener();
        clickListener();
        itemClickListener();
        return view;
    }

    private void initialization(View view){
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        snackbar = new SnackbarHandler(getActivity());
        spManager = new SPManager(getContext());

        etSearch = view.findViewById(R.id.etSearch);
        btnSearch = view.findViewById(R.id.btnSearch);
        rvUser = view.findViewById(R.id.rvUser);

        rvUser.setLayoutManager(new LinearLayoutManager(getContext()));
        rvUser.setHasFixedSize(true);
        listUser = new ArrayList<>();
        adapter = new UsersAdapter(getContext(), listUser);
        rvUser.setAdapter(adapter);
    }

    private void inputListener(){
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void clickListener(){
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.getFilter().filter(etSearch.getText().toString());
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void  getAllUsers(){
        Call<UsersResponse> getUsers = apiInterface.getAllUsers();

        getUsers.enqueue(new Callback<UsersResponse>() {
            @Override
            public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {
                if (response.body().getSuccess()==1) {
                    snackbar.snackSuccess("Success");
                    for (int i=0; i<response.body().getData().size();i++){
                        UserDataResponse data = response.body().getData().get(i);
                        listUser.add(new UserDataResponse(
                                data.getIdUser(),
                                data.getIdRestaurant(),
                                data.getName(),
                                data.getUsername(),
                                data.getPhone(),
                                data.getPassword(),
                                data.getImage(),
                                data.getRole()
                        ));

                    }
                    adapter.notifyDataSetChanged();
                } else{
                    snackbar.snackError("Failed");
                }
            }
            @Override
            public void onFailure(Call<UsersResponse> call, Throwable t) {
                snackbar.snackInfo("No Connection");
            }
        });
    }

    private void itemClickListener(){
        adapter.setOnItemClickListener(new UsersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, UserDataResponse obj, int position) {

            }
        });
    }
}