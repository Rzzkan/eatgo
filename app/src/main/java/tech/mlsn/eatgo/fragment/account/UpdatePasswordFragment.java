package tech.mlsn.eatgo.fragment.account;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.network.ApiClient;
import tech.mlsn.eatgo.network.ApiInterface;
import tech.mlsn.eatgo.response.BaseResponse;
import tech.mlsn.eatgo.tools.SPManager;
import tech.mlsn.eatgo.tools.SnackbarHandler;
import tech.mlsn.eatgo.tools.Tools;

public class UpdatePasswordFragment extends Fragment {
    Button btnSave;
    TextInputLayout lytOldPassword, lytNewPassword;
    TextInputEditText etOldPassword, etNewPassword;

    SPManager spManager;
    SnackbarHandler snackbar;
    ApiInterface apiInterface;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_password, container, false);
        initialization(view);
        return view;
    }

    private void initialization(View view){
        spManager = new SPManager(getContext());
        snackbar = new SnackbarHandler(getActivity());
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        btnSave = view.findViewById(R.id.btnSave);
        lytOldPassword = view.findViewById(R.id.lytOldPassword);
        lytNewPassword = view.findViewById(R.id.lytNewPassword);
        etOldPassword = view.findViewById(R.id.etOldPassword);
        etNewPassword = view.findViewById(R.id.etNewPassword);

    }

    private void clickListener(){
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etOldPassword.getText().toString().isEmpty()&&!etNewPassword.getText().toString().isEmpty()){
                    if(etOldPassword.getText().toString().equals(spManager.getSpPassword())){
                        updatePassword();
                    }else {
                        snackbar.snackInfo("Sandi Lama Tidak Sesuai");
                    }
                }else {
                    snackbar.snackInfo("Tidak Boleh Kosong");
                }
            }
        });
    }

    private void updatePassword(){
        Call<BaseResponse> postDashboard = apiInterface.postUpdatePw(
                spManager.getSpId(),
                etNewPassword.getText().toString()
        );

        postDashboard.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body().getSuccess()==1) {
                    spManager.saveSPString(SPManager.SP_PASSWORD,etNewPassword.getText().toString());
                    snackbar.snackSuccess("Success");
                    Tools.removeAllFragment(getActivity(), new ProfileFragment(),"profile");
                } else{
                    snackbar.snackError("Failed");
                }
            }
            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                snackbar.snackInfo("No Connection");
            }
        });
    }
}