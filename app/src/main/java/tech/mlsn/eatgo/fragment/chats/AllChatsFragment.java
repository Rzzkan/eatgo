package tech.mlsn.eatgo.fragment.chats;

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
import tech.mlsn.eatgo.adapter.AllChatsAdapter;
import tech.mlsn.eatgo.adapter.UsersAdapter;
import tech.mlsn.eatgo.network.ApiClient;
import tech.mlsn.eatgo.network.ApiInterface;
import tech.mlsn.eatgo.response.chat.DetailChatDataResponse;
import tech.mlsn.eatgo.response.chat.DetailChatResponse;
import tech.mlsn.eatgo.response.user.UserDataResponse;
import tech.mlsn.eatgo.response.user.UsersResponse;
import tech.mlsn.eatgo.tools.SPManager;
import tech.mlsn.eatgo.tools.SnackbarHandler;
import tech.mlsn.eatgo.tools.Tools;

public class AllChatsFragment extends Fragment {
    EditText etSearch;
    Button btnSearch;
    RecyclerView rvChat;

    AllChatsAdapter adapter;
    ArrayList<DetailChatDataResponse> listAllChat;

    SPManager spManager;
    SnackbarHandler snackbar;
    ApiInterface apiInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_chats, container, false);
        initialization(view);
        getAllChat();
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
        rvChat = view.findViewById(R.id.rvChat);

        rvChat.setLayoutManager(new LinearLayoutManager(getContext()));
        rvChat.setHasFixedSize(true);
        listAllChat = new ArrayList<>();
        adapter = new AllChatsAdapter(getContext(), listAllChat);
        rvChat.setAdapter(adapter);
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

    private void  getAllChat(){
        Call<DetailChatResponse> getChat = apiInterface.getAllChat(
                spManager.getSpId()
        );
        getChat.enqueue(new Callback<DetailChatResponse>() {
            @Override
            public void onResponse(Call<DetailChatResponse> call, Response<DetailChatResponse> response) {
                if (response.body().getSuccess()==1) {
                    snackbar.snackSuccess("Success");
                    for (int i=0; i<response.body().getData().size();i++){
                        DetailChatDataResponse data = response.body().getData().get(i);
                        listAllChat.add(new DetailChatDataResponse(
                                data.getIdChat(),
                                data.getFrom(),
                                data.getTo(),
                                data.getToName(),
                                data.getToImage(),
                                data.getMessage(),
                                data.getDate()
                        ));
                    }
                    adapter.notifyDataSetChanged();
                } else{
                    snackbar.snackError("Failed");
                }
            }
            @Override
            public void onFailure(Call<DetailChatResponse> call, Throwable t) {
                snackbar.snackInfo("No Connection");
            }
        });
    }

    private void itemClickListener(){
        adapter.setOnItemClickListener(new AllChatsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, DetailChatDataResponse obj, int position) {
                Bundle data = new Bundle();
                data.putString("id_from", obj.getFrom());
                data.putString("id_to", obj.getTo());
                data.putString("to_name", obj.getToName());
                data.putString("to_image", obj.getToImage());
                Tools.addFragment(getActivity(), new ChatDetailFragment(), data, "detail-chat");
            }
        });
    }
}