package tech.mlsn.eatgo.fragment.orders;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aagito.imageradiobutton.RadioImageButton;
import com.aagito.imageradiobutton.RadioImageGroup;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.model.CartModel;
import tech.mlsn.eatgo.network.ApiClient;
import tech.mlsn.eatgo.network.ApiInterface;
import tech.mlsn.eatgo.response.BaseResponse;
import tech.mlsn.eatgo.response.menu.AllMenuDataResponse;
import tech.mlsn.eatgo.response.menu.AllMenuResponse;
import tech.mlsn.eatgo.tools.SPManager;
import tech.mlsn.eatgo.tools.SQLiteDBHelper;
import tech.mlsn.eatgo.tools.SnackbarHandler;
import tech.mlsn.eatgo.tools.Tools;

public class PaymentFragment extends Fragment {
    LinearLayout lytEG, lytTransfer, lytWallet;
    RadioImageGroup rgPayment;
    RadioGroup rgPaymentMethod;
    RadioImageButton rbTF, rbOvo, rbGp;
    RadioButton rbOther, rbCash;
    TextView tvPrice;
    TextView tvEG;
    Button btnConfirm;
    String id_restaurant="";
    String payment ="Cash";
    ArrayList<String> listMenu;
    ArrayList<String> listPrice;
    ArrayList<String> listQty;

    ArrayList<CartModel> items;

    SPManager spManager;
    SnackbarHandler snackbar;
    ApiInterface apiInterface;
    SQLiteDBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        initialization(view);
        radioListener();
        btnListener();
        getData();
        return view;
    }

    private void initialization(View view){
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        snackbar = new SnackbarHandler(getActivity());
        spManager = new SPManager(getContext());
        dbHelper = new SQLiteDBHelper(getActivity());

        lytEG = view.findViewById(R.id.lytEG);
        lytTransfer = view.findViewById(R.id.lytTransfer);
        lytWallet = view.findViewById(R.id.lytWallet);
        rgPayment = view.findViewById(R.id.rgPayment);
        rgPaymentMethod = view.findViewById(R.id.rgPaymentMethod);
        tvPrice = view.findViewById(R.id.tvPrice);
        tvEG = view.findViewById(R.id.tvEG);
        rbGp = view.findViewById(R.id.rbGopay);
        rbOvo = view.findViewById(R.id.rbOvo);
        rbTF = view.findViewById(R.id.rbTransfer);
        rbCash = view.findViewById(R.id.rbCash);
        rbOther = view.findViewById(R.id.rbOther);
        btnConfirm = view.findViewById(R.id.btnConfirm);
        tvPrice.setText(Tools.currency(String.valueOf(dbHelper.countTotal())));
        items = new ArrayList<>();
        items = dbHelper.getCartData();
        rbCash.setChecked(true);
    }

    private void  getData(){
        Bundle data = this.getArguments();
        if (data!=null){
            id_restaurant = data.getString("id_restaurant","0");
        }
    }

    private void radioListener(){
        rgPaymentMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rbCash:
                        rgPayment.setVisibility(View.GONE);
                        lytEG.setVisibility(View.GONE);
                        payment = "Cash";
                        break;
                    case R.id.rbPoint:
                        rgPayment.setVisibility(View.GONE);
                        lytEG.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rbOther:
                        rgPayment.setVisibility(View.VISIBLE);
                        lytEG.setVisibility(View.GONE);
                        break;
                }
            }
        });


        rgPayment.setOnCheckedChangeListener(new RadioImageGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NotNull View view, @Nullable View view1, boolean b, int i) {
                switch (i){
                    case R.id.rbTransfer:
                        lytTransfer.setVisibility(View.VISIBLE);
                        lytWallet.setVisibility(View.GONE);
                        payment ="Transfer";
                        break;
                    case R.id.rbOvo:
                        lytTransfer.setVisibility(View.GONE);
                        lytWallet.setVisibility(View.VISIBLE);
                        payment ="OVO";
                        break;
                    case R.id.rbGopay:
                        lytTransfer.setVisibility(View.GONE);
                        lytWallet.setVisibility(View.VISIBLE);
                        payment ="GOPAY";
                        break;
                }
            }
        });
    }

    private void btnListener(){
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               cartToList();
            }
        });
    }

    private void postOrder(){
        Call<BaseResponse> postOrder = apiInterface.addOrder(
                spManager.getSpId(),
                id_restaurant,
                listMenu,
                listPrice,
                listQty,
                String.valueOf(dbHelper.countTotal()),
                "0",
                "0",
                "1",
                payment,
                getDate()
        );

        postOrder.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body().getSuccess()==1) {
                    snackbar.snackSuccess("Success");
                    dbHelper.deleteDatabase();
                    Tools.removeAllFragment(getActivity(), new HistoryOrderFragment(),"history");
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

    private void cartToList(){
        listMenu = new ArrayList<>();
        listPrice = new ArrayList<>();
        listQty = new ArrayList<>();
        for (int i=0 ;i<items.size();i++){
            listMenu.add(items.get(i).getName());
            listPrice.add(String.valueOf(items.get(i).getPrice()));
            listQty.add(String.valueOf(items.get(i).getQty()));
        }
       postOrder();
    }

    private String getDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date getCurrentDate = Calendar.getInstance().getTime();
        String currentDate = sdf.format(getCurrentDate);
        return currentDate;
    }

}