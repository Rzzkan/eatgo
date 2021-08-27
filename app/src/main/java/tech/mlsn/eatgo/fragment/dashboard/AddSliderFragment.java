package tech.mlsn.eatgo.fragment.dashboard;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.network.ApiClient;
import tech.mlsn.eatgo.network.ApiInterface;
import tech.mlsn.eatgo.response.BaseResponse;
import tech.mlsn.eatgo.response.BaseResponse;
import tech.mlsn.eatgo.tools.SPManager;
import tech.mlsn.eatgo.tools.SnackbarHandler;
import tech.mlsn.eatgo.tools.Tools;

import static tech.mlsn.eatgo.tools.Base64Helper.encodeTobase64;

public class AddSliderFragment extends Fragment {
    Button btnSave, btnPick;
    ImageView ivPreview;

    SPManager spManager;
    SnackbarHandler snackbar;
    ApiInterface apiInterface;
    String img;


    private static final int GALLERY_IMAGE_REQ_CODE = 102;
    private static final int CAMERA_IMAGE_REQ_CODE = 103;
    String currentDateandTime ="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_slider, container, false);
        initialization(view);
        btnListener();
        return view;
    }

    private void initialization(View view){
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        snackbar = new SnackbarHandler(getActivity());
        spManager = new SPManager(getContext());

        btnPick = view.findViewById(R.id.btnPick);
        btnSave = view.findViewById(R.id.btnSave);
        ivPreview = view.findViewById(R.id.ivPreview);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        currentDateandTime = sdf.format(new Date());
    }

    private void btnListener(){
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateImgDatabase(img);
            }
        });

        btnPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    public void updateImgDatabase(String img){
        Call<BaseResponse> postUpdateImgUser = apiInterface.addSlider(
                currentDateandTime,
                img
        );

        postUpdateImgUser.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body().getSuccess()==1) {
                    snackbar.snackSuccess("Success");
                    Tools.removeAllFragment(getActivity(), new AdminDashboardFragment(),"admin dashboard");
                }else {
                    snackbar.snackError("Failed");
                }
            }
            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            // Uri object will not be null for RESULT_OK
            Uri uri = data.getData();
            Toast.makeText(getContext(),uri.toString(),Toast.LENGTH_SHORT).show();

            try {
                // You can update this bitmap to your server
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                Glide.with(getActivity()).load(bitmap).into(ivPreview);
                img = encodeTobase64(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(getActivity(), ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
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