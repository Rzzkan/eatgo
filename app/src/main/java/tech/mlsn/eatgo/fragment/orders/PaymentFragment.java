package tech.mlsn.eatgo.fragment.orders;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aagito.imageradiobutton.RadioImageButton;
import com.aagito.imageradiobutton.RadioImageGroup;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Text;

import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.network.ApiClient;
import tech.mlsn.eatgo.network.ApiInterface;
import tech.mlsn.eatgo.tools.SPManager;
import tech.mlsn.eatgo.tools.SnackbarHandler;
import tech.mlsn.eatgo.tools.Tools;

public class PaymentFragment extends Fragment {
    LinearLayout lytEG, lytTransfer, lytWallet;
    RadioImageGroup rgPayment;
    RadioGroup rgPaymentMethod;
    RadioImageButton rbTF, rbOvo, rbGp;
    TextView tvPrice;
    TextView tvEG;
    Button btnConfirm;

    SPManager spManager;
    SnackbarHandler snackbar;
    ApiInterface apiInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        initialization(view);
        radioListener();
        btnListener();
        getData();
        getPoint();
        return view;
    }

    private void initialization(View view){
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        snackbar = new SnackbarHandler(getActivity());
        spManager = new SPManager(getContext());

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
        btnConfirm = view.findViewById(R.id.btnConfirm);
    }


    private void getPoint(){

    }

    private void  getData(){
        Bundle data = this.getArguments();
        if (data!=null){

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
                        break;
                    case R.id.rbOvo:
                        lytTransfer.setVisibility(View.GONE);
                        lytWallet.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rbGopay:
                        lytTransfer.setVisibility(View.GONE);
                        lytWallet.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    private void btnListener(){
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.removeAllFragment(getActivity(), new HistoryOrderFragment(), "history");
            }
        });
    }
}