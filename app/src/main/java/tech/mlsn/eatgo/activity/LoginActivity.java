package tech.mlsn.eatgo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.karan.churi.PermissionManager.PermissionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.network.ApiClient;
import tech.mlsn.eatgo.network.ApiInterface;
import tech.mlsn.eatgo.response.login.LoginDataResponse;
import tech.mlsn.eatgo.response.login.LoginResponse;
import tech.mlsn.eatgo.tools.SPManager;
import tech.mlsn.eatgo.tools.SnackbarHandler;

public class LoginActivity extends AppCompatActivity {
    ImageView ivLogo;
    TextInputLayout lytUsername, lytPassword;
    TextInputEditText etUsername, etPassword;
    Button btnLogin, btnRegister;

    SPManager spManager;
    SnackbarHandler snackbar;
    ApiInterface apiInterface;

    PermissionManager permissionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialization();
        inputListener();
        btnListener();
    }

    private void initialization(){
        spManager = new SPManager(this);
        snackbar = new SnackbarHandler(this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        ivLogo = findViewById(R.id.ivLogo);
        lytUsername = findViewById(R.id.lytUsername);
        lytPassword = findViewById(R.id.lytPassword);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        permissionManager = new PermissionManager() {};
        permissionManager.checkAndRequestPermissions(this);

        Glide.with(this).load(R.drawable.logo_eatgo).into(ivLogo);
        if (spManager.getSpIsSigned()){
            startActivity(new Intent(this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    }

    private void btnListener(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    postLogin();
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void postLogin(){
        Call<LoginResponse> postSingnin = apiInterface.postLogin(
                etUsername.getText().toString(),
                etPassword.getText().toString()
        );

        postSingnin.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.body().getSuccess()==1) {
                    LoginDataResponse currentUser = response.body().getData();
                    spManager.saveSPString(spManager.SP_ID, currentUser.getIdUser());
                    spManager.saveSPString(spManager.SP_NAME, currentUser.getName());
                    spManager.saveSPString(spManager.SP_ADDRESS, currentUser.getAddress());
                    spManager.saveSPString(spManager.SP_PHONE, currentUser.getPhone());
                    spManager.saveSPString(spManager.SP_ROLE, currentUser.getRole());
                    spManager.saveSPString(spManager.SP_ID_RESTO, currentUser.getIdRestaurant());
                    spManager.saveSPString(spManager.SP_PASSWORD, currentUser.getPassword());
                    spManager.saveSPBoolean(spManager.SP_IS_SIGNED, true);
                    snackbar.snackSuccess("Success");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, 1000);
                } else{
                    snackbar.snackError("Wrong Password");
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                snackbar.snackInfo("No Connection");
            }
        });
    }

    private Boolean validate(){
        boolean valid = true;
        if (
                etUsername.getText().toString().isEmpty()
        ){
            lytUsername.setError("Cant Empty !");
            valid = false;
        }

        if (
                etPassword.getText().toString().isEmpty()
        ){
            lytPassword.setError("Cant Empty !");
            valid = false;
        }

        return valid;
    }

    private void inputListener(){
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
    }


}