package tech.mlsn.eatgo.fragment.account;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.activity.ImagePickerActivity;
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

    public static final int REQUEST_IMAGE = 100;
    public String img;

    SPManager spManager;
    SnackbarHandler snackbar;
    ApiInterface apiInterface;

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
                Dexter.withActivity(getActivity())
                        .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {
                                    showImagePickerOptions();
                                }

                                if (report.isAnyPermissionPermanentlyDenied()) {
                                    showSettingsDialog();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
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

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(getContext(), new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    img = encodeTobase64(bitmap);
                    updateImgDatabase();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showSettingsDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    public void updateImgDatabase(){
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


}