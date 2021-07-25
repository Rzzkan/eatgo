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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.adapter.AdapterChat;
import tech.mlsn.eatgo.model.ChatModel;
import tech.mlsn.eatgo.network.ApiClient;
import tech.mlsn.eatgo.network.ApiInterface;
import tech.mlsn.eatgo.response.BaseResponse;
import tech.mlsn.eatgo.response.chat.DetailChatDataResponse;
import tech.mlsn.eatgo.response.chat.DetailChatResponse;
import tech.mlsn.eatgo.tools.SPManager;
import tech.mlsn.eatgo.tools.SnackbarHandler;
import tech.mlsn.eatgo.tools.Tools;

public class ChatDetailFragment extends Fragment {

    TextView tvName, tvStatus, tvDate;
    CircularImageView imgProfile;
    EditText etMessage;
    ImageView imgSent;
    RecyclerView rvChat;
    AdapterChat adapter;
    ArrayList<ChatModel> items;
    String name, img, date, id_from, id_to;

    SnackbarHandler snackbar;
    ApiInterface apiInterface;
    SPManager spManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_detail, container, false);
        initialization(view);
        getBundle();
        getChatData();
        clickListener();
        return view;
    }

    private void initialization(View view){
        snackbar = new SnackbarHandler(getActivity());
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        spManager = new SPManager(getContext());
        items = new ArrayList<>();
        tvName = view.findViewById(R.id.tvName);
        tvStatus = view.findViewById(R.id.tvStatus);
        imgProfile = view.findViewById(R.id.imgProfile);
        etMessage = view.findViewById(R.id.text_content);

        imgSent = view.findViewById(R.id.imgSent);
        rvChat = view.findViewById(R.id.rvChat);
        tvDate = view.findViewById(R.id.tvDate);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvChat.setLayoutManager(layoutManager);
        rvChat.setHasFixedSize(true);
        adapter = new AdapterChat(getContext());
        rvChat.setAdapter(adapter);
    }

    private void getBundle(){
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            name = bundle.getString("to_name","name");
            img = bundle.getString("to_image","");
            id_from = bundle.getString("id_from","1");
            id_to = bundle.getString("id_to","2");

            tvName.setText(name);
            if (img.equalsIgnoreCase("")){
                Glide.with(getActivity()).load(R.drawable.logo_eatgo).centerInside().into(imgProfile);
            }else{
                Glide.with(getContext()).load(ApiClient.BASE_URL+img).into(imgProfile);
            }
        }
    }

    private void clickListener(){
        imgSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendChat();
            }
        });
    }

    private TextWatcher contentWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable etd) {

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }
    };

    private void sendChat() {
        final String msg = etMessage.getText().toString();
        if(msg.isEmpty()) return;
        postChatData(msg);
        adapter.insertItem(new ChatModel(adapter.getItemCount(), msg, true, adapter.getItemCount() % 5 == 0, date));
        etMessage.setText("");
        rvChat.scrollToPosition(adapter.getItemCount() - 1);
    }

    private void getChatData(){
        Call<DetailChatResponse> getChat = apiInterface.getDetailChat(
                id_from,
                id_to
        );

        getChat.enqueue(new Callback<DetailChatResponse>() {
            @Override
            public void onResponse(Call<DetailChatResponse> call, Response<DetailChatResponse> response) {
                if (response.body().getSuccess()==1) {
                    for (int i=0; i<response.body().getData().size();i++){
                        DetailChatDataResponse data = response.body().getData().get(i);
                        if (data.getFrom().equalsIgnoreCase(spManager.getSpId())){
                            adapter.insertItem(new ChatModel(
                                    Long.valueOf(data.getIdChat()),
                                    data.getMessage(),
                                    true,
                                    adapter.getItemCount() % 5 == 0,
                                    Tools.dateParserChat(data.getDate())
                            ));
                        }else{
                            adapter.insertItem(new ChatModel(
                                    Long.valueOf(data.getIdChat()),
                                    data.getMessage(),
                                    false,
                                    adapter.getItemCount() % 5 == 0,
                                    Tools.dateParserChat(data.getDate())
                            ));
                            tvDate.setText(Tools.dateParserChat(response.body().getData().get(response.body().getData().size()-1).getDate()));
                        }
                    }
                    adapter.notifyDataSetChanged();
//                    snackbar.snackSuccess("Berhasil Memuat Data");
                } else{
                    snackbar.snackError("Failed");
                }
            }
            @Override
            public void onFailure(Call<DetailChatResponse> call, Throwable t) {
                snackbar.snackError("Get Chat Data : "+t.toString());
            }
        });
    }

    private void postChatData(String message){
        Call<BaseResponse> getChat = apiInterface.addChat(
                id_from,
                id_to,
                message,
                ""
        );

        getChat.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body().getSuccess()==1) {
//                    snackbar.snackSuccess("Berhasil Memuat Data");
                } else{
                    snackbar.snackError("Failed");
                }
            }
            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                snackbar.snackError("Post Chat Data : "+t.toString());
            }
        });
    }
}