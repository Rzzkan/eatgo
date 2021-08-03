package tech.mlsn.eatgo.fragment.menus;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.fragment.orders.OrdersFragment;
import tech.mlsn.eatgo.model.CartModel;
import tech.mlsn.eatgo.network.ApiClient;
import tech.mlsn.eatgo.network.ApiInterface;
import tech.mlsn.eatgo.response.menu.AllMenuDataResponse;
import tech.mlsn.eatgo.response.menu.MenuResponse;
import tech.mlsn.eatgo.tools.SPManager;
import tech.mlsn.eatgo.tools.SQLiteDBHelper;
import tech.mlsn.eatgo.tools.SnackbarHandler;
import tech.mlsn.eatgo.tools.Tools;

public class DetailMenuFragment extends Fragment {
    TextView tvName, tvDescription, tvPrice, tvCategory;
    ImageView ivMenu;
    CardView lytBuy;
    Button btnListOrders, btnBuy;
    String name, desc, img, price;
    String id_restaurant ="", id_menu="";
    int counter = 0;
//    int id_cart=0;


    SQLiteDBHelper dbHelper;
    boolean insertData;

    SPManager spManager;
    SnackbarHandler snackbar;
    ApiInterface apiInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_menu, container, false);
        initialization(view);
        getData();
        btnListener();
        return view;
    }

    private void initialization(View view){
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        snackbar = new SnackbarHandler(getActivity());
        spManager = new SPManager(getContext());
        dbHelper = new SQLiteDBHelper(getActivity());

        ivMenu = view.findViewById(R.id.ivMenu);
        tvName = view.findViewById(R.id.tvName);
        tvDescription = view.findViewById(R.id.tvDescription);
        tvPrice = view.findViewById(R.id.tvPrice);
        tvCategory = view.findViewById(R.id.tvCategory);
        lytBuy = view.findViewById(R.id.lytBuy);

        if (!spManager.getSpRole().equalsIgnoreCase("user")){
            lytBuy.setVisibility(View.GONE);
        }

        btnListOrders = view.findViewById(R.id.btnListOrders);
        btnBuy = view.findViewById(R.id.btnBuy);


    }

    private void getData(){
        Bundle data = this.getArguments();
        if (data!=null){
            getMenu(data.getString("id_menu","0"));
//            id_cart = data.getInt("id_cart",0);
            id_menu = data.getString("id_menu","0");
            id_restaurant = data.getString("id_restaurant","0");
        }


    }

    private void getMenu(String id){
            Call<MenuResponse> getMenu = apiInterface.getMenu(
                    id
            );

            getMenu.enqueue(new Callback<MenuResponse>() {
                @Override
                public void onResponse(Call<MenuResponse> call, Response<MenuResponse> response) {
                    if (response.body().getSuccess()==1) {
                        snackbar.snackSuccess("Success");
                        AllMenuDataResponse data = response.body().getData();
                        tvName.setText(data.getName());
                        tvDescription.setText(data.getDescription());
                        tvPrice.setText(Tools.currency(data.getPrice()));
                        tvCategory.setText(data.getCategory());
                        Glide.with(getContext()).load(ApiClient.BASE_URL + data.getImage()).centerCrop().into(ivMenu);

                        price = data.getPrice();
                        name = data.getName();
                        desc = data.getDescription();
                        img = data.getImage();
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

    private void  btnListener(){
        btnListOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActionDialog(1);
            }
        });

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showActionDialog(2);
            }
        });
    }

    private void showSuccessCartDialog(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_order_success);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                Bundle data = new Bundle();
                data.putString("id_restaurant", id_restaurant);
                Tools.addFragment(getActivity(), new AllMenusFragment(), data, "all-menus");
            }
        },2000);
    }


    private void showActionDialog(int con){
        final BottomSheetDialog dialog = new BottomSheetDialog(getContext(),R.style.SheetDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_buy);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        ImageView imgProduct = dialog.findViewById(R.id.imgProduct);
        TextView tvProduct = dialog.findViewById(R.id.tvProductName);
        TextView tvPrice = dialog.findViewById(R.id.tvPrice);
        ImageButton btnAdd = dialog.findViewById(R.id.add);
        ImageButton btnSubs = dialog.findViewById(R.id.subs);
        EditText etCounter = dialog.findViewById(R.id.etTotal);
        Button btnAction = dialog.findViewById(R.id.btnAction);

        Glide.with(this).load(ApiClient.BASE_URL + img).centerCrop().into(imgProduct);
        tvProduct.setText(name);
        tvPrice.setText(String.valueOf(price));

        if (con==1){
            btnAction.setText("add to List Order");
        }else if (con==2){
            btnAction.setText("Buy Now");
        }

        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (counter<=0){
                    snackbar.snackInfo("Cant Order 0");
                }else {
                    if(con==1){
                        insertData = dbHelper.addData(new CartModel(Integer.valueOf(id_menu),id_restaurant, id_menu,name,counter,Integer.valueOf(price),Integer.valueOf(price)*counter,img,desc));
                        if (insertData){
                            showSuccessCartDialog();
                        }else {
                            dbHelper.updateCart(new CartModel(Integer.valueOf(id_menu),id_restaurant, id_menu,name,counter,Integer.valueOf(price),Integer.valueOf(price)*counter,img,desc));
                            showSuccessCartDialog();
                        }
                    }else if(con==2){
                        insertData = dbHelper.addData(new CartModel(Integer.valueOf(id_menu),id_restaurant, id_menu,name,counter,Integer.valueOf(price),Integer.valueOf(price)*counter,img,desc));
                        if (insertData){
                        }else {
                            dbHelper.updateCart(new CartModel(Integer.valueOf(id_menu),id_restaurant, id_menu,name,counter,Integer.valueOf(price),Integer.valueOf(price)*counter,img,desc));
                        }
                        Bundle data = new Bundle();
                        data.putString("id_restaurant", id_restaurant);
                        Tools.addFragment(getActivity(), new OrdersFragment(), data, "orders");
                    }
                }
                counter=0;
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                etCounter.setText(String.valueOf(counter));
            }
        });

        btnSubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter>0){
                    counter--;
                }
                etCounter.setText(String.valueOf(counter));
            }
        });
        dialog.show();
    }



}