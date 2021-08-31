package tech.mlsn.eatgo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.network.ApiClient;
import tech.mlsn.eatgo.network.ApiInterface;
import tech.mlsn.eatgo.response.BaseResponse;
import tech.mlsn.eatgo.response.login.LoginDataResponse;
import tech.mlsn.eatgo.response.login.LoginResponse;
import tech.mlsn.eatgo.tools.SPManager;
import tech.mlsn.eatgo.tools.SnackbarHandler;

public class RegisterActivity extends AppCompatActivity {
    TextInputLayout lytUsername, lytName, lytPhone, lytPassword, lytAddress;
    TextInputEditText etUsername, etName, etPhone, etPassword, etAddress;
    RadioGroup rgRole;
    RadioButton rbUser, rbResto;
    Button btnRegister;

    String role ="user";

    SPManager spManager;
    SnackbarHandler snackbar;
    ApiInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initialization();
        btnListener();
        radioListener();
        inputListener();
    }

    private void initialization(){
        spManager = new SPManager(this);
        snackbar = new SnackbarHandler(this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        lytUsername = findViewById(R.id.lytUsername);
        lytName = findViewById(R.id.lytName);
        lytPhone = findViewById(R.id.lytPhone);
        lytPassword = findViewById(R.id.lytPassword);
        etUsername = findViewById(R.id.etUsername);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        lytAddress = findViewById(R.id.lytAddress);
        etAddress = findViewById(R.id.etAddress);
        rgRole = findViewById(R.id.rgRole);
        rbUser = findViewById(R.id.rbUser);
        rbResto = findViewById(R.id.rbResto);
        btnRegister = findViewById(R.id.btnRegister);
        rgRole.check(R.id.rbUser);
    }

    private void btnListener(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    postRegister();
                }
            }
        });
    }

    private void radioListener(){
        rgRole.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rbUser:
                        role = "user";
                        break;
                    case R.id.rbResto:
                        role = "resto";
                        break;
                }
            }
        });
    }


    private void postRegister(){
        Call<BaseResponse> postRegister = apiInterface.postRegister(
                etName.getText().toString(),
                etUsername.getText().toString(),
                etAddress.getText().toString(),
                etPhone.getText().toString(),
                etPassword.getText().toString(),
                role
        );

        postRegister.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body().getSuccess()==1) {
                    snackbar.snackSuccess("Success");
                    finish();
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

    private void checkUsername(){
        Call<BaseResponse> postRegister = apiInterface.isUsernameExist(
                etUsername.getText().toString()
        );

        postRegister.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body().getSuccess()==1) {
                    lytUsername.setError("Username Already Taken");
                }
            }
            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                snackbar.snackInfo("No Connection");
            }
        });
    }

    private void inputListener(){
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (lytPhone.isErrorEnabled()){
                    lytPhone.setErrorEnabled(false);

                }
                if (!s.toString().matches("^[1-9][0-9]*$")){
                    lytPhone.setError("Numbers Must not start with 0");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (lytUsername.isErrorEnabled()){
                    lytUsername.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()){
                    checkUsername();
                }
            }
        });

        etAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (lytAddress.isErrorEnabled()){
                    lytAddress.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

       etPassword.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               if (lytPassword.isErrorEnabled()){
                   lytPassword.setErrorEnabled(false);
               }
           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });

       etAddress.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               if (lytAddress.isErrorEnabled()){
                   lytAddress.setErrorEnabled(false);
               }
           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });

    }

    private Boolean validate(){
        boolean valid = true;

        if (etName.getText().toString().isEmpty()
        ){
            lytName.setError("Cant Empty !");
            valid = false;
        }

        if (etUsername.getText().toString().isEmpty()
        ){
            lytUsername.setError("Cant Empty !");
            valid = false;
        }

        if (etAddress.getText().toString().isEmpty()
        ){
            lytAddress.setError("Cant Empty !");
            valid = false;
        }

        if (etPassword.getText().toString().isEmpty()
        ){
            lytPassword.setError("Cant Empty !");
            valid = false;
        }

        if (etAddress.getText().toString().isEmpty()
        ){
            lytAddress.setError("Cant Empty !");
            valid = false;
        }

        if (etPhone.getText().toString().isEmpty()
        ){
            lytPhone.setError("Cant Empty !");
            valid = false;
        }

//        if (etName.getText().toString().isEmpty()||
//                etAddress.getText().toString().isEmpty()||
//                etPhone.getText().toString().isEmpty()||
//                etPassword.getText().toString().isEmpty()||
//                etAddress.getText().toString().isEmpty()||
//                etUsername.getText().toString().isEmpty()
//        ){
//            snackbar.snackInfo("Please make sure if there are no empty field");
//            valid = false;
//        }

        if (role.equalsIgnoreCase("")){
            valid = false;
        }

        if (!etPhone.getText().toString().matches("^[1-9][0-9]*$")){
            lytPhone.setError("Numbers Must not start with 0 !");
            valid = false;
        }
        return valid;
    }


}