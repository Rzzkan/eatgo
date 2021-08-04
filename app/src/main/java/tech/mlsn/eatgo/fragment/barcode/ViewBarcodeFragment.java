package tech.mlsn.eatgo.fragment.barcode;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.network.ApiClient;
import tech.mlsn.eatgo.network.ApiInterface;
import tech.mlsn.eatgo.tools.SPManager;
import tech.mlsn.eatgo.tools.SnackbarHandler;

public class ViewBarcodeFragment extends Fragment {
    Button btnShare;
    ImageView ivBarcode;

    SPManager spManager;
    SnackbarHandler snackbar;
    ApiInterface apiInterface;
    Bitmap bmp = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_barcode, container, false);
        initialization(view);
        generateBarcode();
        btnListener();
        return view;
    }

    private void initialization(View view){
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        snackbar = new SnackbarHandler(getActivity());
        spManager = new SPManager(getContext());

        btnShare = view.findViewById(R.id.btnShare);
        ivBarcode = view.findViewById(R.id.ivBarcode);
    }

    public void generateBarcode(){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(spManager.getSpIdResto(), BarcodeFormat.QR_CODE, 300, 300);
            Bitmap bitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.RGB_565);
            for (int i = 0; i<300; i++){
                for (int j = 0; j<300; j++){
                    bitmap.setPixel(i,j,bitMatrix.get(i,j)? Color.BLACK:Color.WHITE);
                }
            }
            bmp = bitmap;
            ivBarcode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void btnListener(){
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareBarcode();
            }
        });
    }

    private void shareBarcode(){
        saveImage(bmp);
        share(bmp);
    }

    private String saveImage(Bitmap bitmap) {

        // Get the destination folder for saved images defined in strings.xml
        String dstFolder = "Eatgo Barcode";

        // Create Destination folder in external storage. This will require EXTERNAL STORAGE permission
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        File imgDir = new File(externalStorageDirectory.getAbsolutePath(), dstFolder);
        imgDir.mkdirs();

        // Generate a random file name for image
        String imageName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpeg";
        File localFile = new File(imgDir, imageName);
        localFile.renameTo(localFile);
        String path = "file://" + externalStorageDirectory.getAbsolutePath() + "/" + dstFolder;

        try {
            FileOutputStream fos = new FileOutputStream(localFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            getActivity().sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(new File(path))));
        } catch (Exception e)  {
            e.printStackTrace();
        }

        // Local path to be shown to User to tell where the Image has been saved.
        return path;
    }

    private void share(Bitmap bitmap) {
        try {
            String text ="Order food at "+spManager.getSpNameResto()+ " only at eatgo";
            Uri shareUri = getImageUri(getContext(), bitmap);
            Intent localIntent = new Intent();
            localIntent.setAction("android.intent.action.SEND");
            localIntent.putExtra("android.intent.extra.STREAM", shareUri);
            localIntent.putExtra(Intent.EXTRA_TEXT, text);
            localIntent.setType("image/jpg");
            localIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            this.startActivity(localIntent);
        } catch (Exception e) {
            snackbar.snackInfo(e.toString());;
        }
    }

    private Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }


}