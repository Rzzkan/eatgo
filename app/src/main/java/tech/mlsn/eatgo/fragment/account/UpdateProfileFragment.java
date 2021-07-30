package tech.mlsn.eatgo.fragment.account;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.github.dhaval2404.imagepicker.ImagePickerActivity;
import com.github.dhaval2404.imagepicker.constant.ImageProvider;
import com.github.dhaval2404.imagepicker.util.IntentUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.io.IOException;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.network.ApiClient;
import tech.mlsn.eatgo.network.ApiInterface;
import tech.mlsn.eatgo.response.BaseResponse;
import tech.mlsn.eatgo.response.profile.UpdateImageResponse;
import tech.mlsn.eatgo.tools.SPManager;
import tech.mlsn.eatgo.tools.SnackbarHandler;
import tech.mlsn.eatgo.tools.Tools;

import static tech.mlsn.eatgo.tools.Base64Helper.encodeTobase64;


public class UpdateProfileFragment extends Fragment {
    CircularImageView imgProfile;
    Button btnChangePhoto, btnSave;
    TextInputLayout lytName, lytPhone, lytAddress;
    TextInputEditText etName, etPhone, etAddress;

    SPManager spManager;
    SnackbarHandler snackbar;
    ApiInterface apiInterface;

    private static final int GALLERY_IMAGE_REQ_CODE = 102;
    private static final int CAMERA_IMAGE_REQ_CODE = 103;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_profile, container, false);
        initialization(view);
        btnListener();
        inputListener();
        return view;
    }

    private void initialization(View view){
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        snackbar = new SnackbarHandler(getActivity());
        spManager = new SPManager(getContext());

        imgProfile = view.findViewById(R.id.imgProfile);
        btnChangePhoto = view.findViewById(R.id.btnChangePhoto);
        btnSave = view.findViewById(R.id.btnSave);
        lytName = view.findViewById(R.id.lytName);
        lytPhone = view.findViewById(R.id.lytPhone);
        lytAddress = view.findViewById(R.id.lytAddress);
        etName = view.findViewById(R.id.etName);
        etPhone = view.findViewById(R.id.etPhone);
        etAddress = view.findViewById(R.id.etAddress);

        etName.setText(spManager.getSpName());
        etPhone.setText(spManager.getSpPhone());
        etAddress.setText(spManager.getSpAddress());

        if (spManager.getSpImg().equalsIgnoreCase("null")){
            Glide.with(getContext())
                    .load(R.drawable.ic_baseline_person_24)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(imgProfile);
        }else{
            Glide.with(getContext())
                    .load(ApiClient.BASE_URL+spManager.getSpImg())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(imgProfile);
        }
    }

    private void btnListener(){
        btnChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    postUpdate();
                }
            }
        });
    }

//Instead of onActivityResult() method use this one


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            // Uri object will not be null for RESULT_OK
            Uri uri = data.getData();
            Toast.makeText(getContext(),uri.toString(),Toast.LENGTH_SHORT).show();

            try {
                // You can update this bitmap to your server
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                Glide.with(getActivity()).load(bitmap).into(imgProfile);
                updateImgDatabase(encodeTobase64(bitmap));
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(getActivity(), ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }


    public void updateImgDatabase(String img){
        Call<UpdateImageResponse> postUpdateImgUser = apiInterface.postUpdateImgUser(
                spManager.getSpId(),
                img
        );

        postUpdateImgUser.enqueue(new Callback<UpdateImageResponse>() {
            @Override
            public void onResponse(Call<UpdateImageResponse> call, Response<UpdateImageResponse> response) {
                if (response.body().getSuccess()==1) {
                    spManager.saveSPString(SPManager.SP_IMG, response.body().getUrl());
                    loadProfile(spManager.getSpImg());
                } else{
                    Toast.makeText(getContext(),"Gagal",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<UpdateImageResponse> call, Throwable t) {

            }
        });
    }

    private void loadProfile(String url) {
        Glide.with(this)
                .load(ApiClient.BASE_URL+url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imgProfile);
        imgProfile.setColorFilter(ContextCompat.getColor(getContext(), android.R.color.transparent));
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

    }

    private Boolean validate(){
        boolean valid = true;
        if (etName.getText().toString().isEmpty()||
                etAddress.getText().toString().isEmpty()||
                etPhone.getText().toString().isEmpty()||
                etAddress.getText().toString().isEmpty()
        ){
            snackbar.snackInfo("Please make sure if there are no empty field");
            valid = false;
        }

        if (!etPhone.getText().toString().matches("^[1-9][0-9]*$")){
            lytPhone.setError("Numbers Must not start with 0");
            valid = false;
        }
        return valid;
    }

    private void postUpdate(){
        Call<BaseResponse> postRegister = apiInterface.updateUser(
                spManager.getSpId(),
                etName.getText().toString(),
                etAddress.getText().toString(),
                etPhone.getText().toString()
        );

        postRegister.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body().getSuccess()==1) {
                    snackbar.snackSuccess("Success");
                    spManager.saveSPString(SPManager.SP_NAME,etName.getText().toString());
                    spManager.saveSPString(SPManager.SP_ADDRESS,etAddress.getText().toString());
                    spManager.saveSPString(SPManager.SP_PHONE,etPhone.getText().toString());
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

    private void fromGallery(){
        ImagePicker.Companion.with(this)
                .crop()
                .galleryOnly()
                .galleryMimeTypes(new String[]{"image/png",
                        "image/jpg",
                        "image/jpeg"
                })
                .start(GALLERY_IMAGE_REQ_CODE);
    }

    private void fromCamera(){
        ImagePicker.Companion.with(this)
                .crop()
                .cameraOnly()
                .saveDir(getActivity().getExternalFilesDir(Environment.DIRECTORY_DCIM))
                .start(CAMERA_IMAGE_REQ_CODE);
    }

    private void showDialog(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_picker);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        final Button btnCamera = dialog.findViewById(R.id.btnCamera);
        final Button btnGallery = dialog.findViewById(R.id.btnGalery);


        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromCamera();
                dialog.dismiss();
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromGallery();
                dialog.dismiss();
            }
        });
        dialog.show();
    }


}