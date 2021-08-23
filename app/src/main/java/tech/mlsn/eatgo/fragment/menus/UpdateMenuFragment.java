package tech.mlsn.eatgo.fragment.menus;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.fragment.account.ProfileFragment;
import tech.mlsn.eatgo.network.ApiClient;
import tech.mlsn.eatgo.network.ApiInterface;
import tech.mlsn.eatgo.response.BaseResponse;
import tech.mlsn.eatgo.response.menu.AllMenuDataResponse;
import tech.mlsn.eatgo.response.menu.MenuResponse;
import tech.mlsn.eatgo.tools.SPManager;
import tech.mlsn.eatgo.tools.SnackbarHandler;
import tech.mlsn.eatgo.tools.Tools;

import static tech.mlsn.eatgo.tools.Base64Helper.encodeTobase64;

public class UpdateMenuFragment extends Fragment {
    LinearLayout lytFoodView, lytFood;
    TextInputLayout lytName, lytDesc, lytPrice;
    TextInputEditText etName, etDesc, etPrice;

    Button btnSave;
    Switch swActive;
    ImageView ivFood;
    TextView tvNameFood;
    Button btnFood, btnReuploadFood;
    SmartMaterialSpinner spinCategory;

    private static final int GALLERY_IMAGE_REQ_CODE = 102;
    private static final int CAMERA_IMAGE_REQ_CODE = 103;
    String img="", category="";
    String id_menu="0";

    String active="0";

    ArrayList<String> listCategory;

    SPManager spManager;
    SnackbarHandler snackbar;
    ApiInterface apiInterface;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_menu, container, false);
        initialization(view);
        getData();
        clickListener();
        spinnerListener();
        swListener();
        return view;
    }

    private void initialization(View view){
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        snackbar = new SnackbarHandler(getActivity());
        spManager = new SPManager(getContext());

        lytFood = view.findViewById(R.id.lytFood);
        lytFoodView = view.findViewById(R.id.lytFoodView);
        lytName = view.findViewById(R.id.lytName);
        lytDesc = view.findViewById(R.id.lytDesc);
        lytPrice = view.findViewById(R.id.lytPrice);
        etName = view.findViewById(R.id.etName);
        etDesc = view.findViewById(R.id.etDesc);
        etPrice = view.findViewById(R.id.etPrice);
        btnSave = view.findViewById(R.id.btnSave);
        swActive = view.findViewById(R.id.swActive);
        ivFood = view.findViewById(R.id.ivFood);
        tvNameFood = view.findViewById(R.id.tvNameFood);
        btnFood = view.findViewById(R.id.btnFood);
        btnReuploadFood = view.findViewById(R.id.btnReuploadFood);
        spinCategory = view.findViewById(R.id.spinType);
    }

    private void getData(){
        Bundle data = this.getArguments();
        id_menu = data.getString("id_menu","0");
        getDataMenu(id_menu);
    }

    private void getDataMenu(String id){
        Call<MenuResponse> getMenu = apiInterface.getMenu(
                id
        );

        getMenu.enqueue(new Callback<MenuResponse>() {
            @Override
            public void onResponse(Call<MenuResponse> call, Response<MenuResponse> response) {
                if (response.body().getSuccess()==1) {
                    snackbar.snackSuccess("Success");
                    AllMenuDataResponse data = response.body().getData();
                    etName.setText(data.getName());
                    etDesc.setText(data.getDescription());
                    etPrice.setText(data.getPrice());
                    if (data.getIsActive().equalsIgnoreCase("1")){
                        swActive.setChecked(true);
                    }else {
                        swActive.setChecked(false);
                    }
                } else{
                    snackbar.snackError("Failed");
                }
            }
            @Override
            public void onFailure(Call<MenuResponse> call, Throwable t) {
                snackbar.snackInfo("No Connection");
            }
        });
    }

    private void clickListener(){
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    postUpdateMenu();
                }
            }
        });

        btnFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lytFood.setVisibility(View.GONE);
                lytFoodView.setVisibility(View.VISIBLE);
                showDialog();
            }
        });

        btnReuploadFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showDialog();
            }
        });
    }

    private void postUpdateMenu(){
        Call<BaseResponse> postRegister = apiInterface.updateMenu(
                id_menu,
                etName.getText().toString(),
                etDesc.getText().toString(),
                category,
                etPrice.getText().toString(),
                "",
                img,
                active
        );

        postRegister.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body().getSuccess()==1) {
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

    private void spinnerListener(){
        listCategory = new ArrayList<>();
        listCategory.add("Food");
        listCategory.add("Beverage");
        spinCategory.setItem(listCategory);

        spinCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = spinCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private Boolean validate(){
        Boolean valid = true;

        if (etName.getText().toString().isEmpty()){
            snackbar.snackInfo("Menu Cannot Be Empty");
            return false;
        }

        if (etDesc.getText().toString().isEmpty()){
            snackbar.snackInfo("Description Cannot Be Empty");
            return false;
        }

        if (etPrice.getText().toString().isEmpty()){
            snackbar.snackInfo("Price Cannot Be Empty");
            return false;
        }

        if (img.equalsIgnoreCase("")){
            snackbar.snackInfo("Image Cannot Be Empty");
            return false;
        }

        if (category.equalsIgnoreCase("")){
            snackbar.snackInfo("Category Cannot Be Empty");
            return false;
        }


        return valid;
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            // Uri object will not be null for RESULT_OK
            Uri uri = data.getData();
            Toast.makeText(getContext(),uri.toString(),Toast.LENGTH_SHORT).show();

            try {
                // You can update this bitmap to your server
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                Glide.with(getActivity()).load(bitmap).into(ivFood);
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


    private void swListener(){
        swActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    active="1";
                }else {
                    active="0";
                }
            }
        });
    }

}