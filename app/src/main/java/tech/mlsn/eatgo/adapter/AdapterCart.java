package tech.mlsn.eatgo.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.model.CartModel;
import tech.mlsn.eatgo.network.ApiClient;
import tech.mlsn.eatgo.tools.SnackbarHandler;


public class AdapterCart extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<CartModel> items;

    private Context ctx;
    private Activity activity;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, CartModel obj, int position);
        void onSubsClick(View view, CartModel obj,int total , int position);
        void onAddClick(View view, CartModel obj,int total, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterCart(Activity activity, Context context, ArrayList<CartModel> items) {
        this.activity = activity;
        this.items = items;
        this.ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public SnackbarHandler snackbar;
        public ImageView imgProduct;
        public TextView tvName, tvPrice, tvDesc;
        public ImageButton btSubs, btAdd;
        public EditText etTotal;
        public int total =0;
        public CartModel cartItem;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            snackbar = new SnackbarHandler(activity);
            imgProduct = v.findViewById(R.id.imgProduct);
            tvName = v.findViewById(R.id.tvProductName);
            tvPrice = v.findViewById(R.id.tvPrice);
            tvDesc = v.findViewById(R.id.tvDesc);
            btAdd = v.findViewById(R.id.add);
            btSubs = v.findViewById(R.id.subs);
            etTotal = v.findViewById(R.id.etTotal);
            lyt_parent =  v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_counter, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            CartModel cart = items.get(position);
            view.total = cart.getQty();
            view.cartItem = new CartModel(
                    cart.getId_cart(),
                    cart.getId_restaurant(),
                    cart.getId_menu(),
                    cart.getName(),
                    cart.getQty(),
                    cart.getPrice(),
                    cart.getTotal(),
                    cart.getImg(),
                    cart.getDesc()
            );
            Glide.with(ctx).load(ApiClient.BASE_URL + cart.getImg()).centerCrop().into(view.imgProduct);
            view.tvName.setText(cart.getName());
            view.tvPrice.setText(String.valueOf(cart.getPrice()));
            view.etTotal.setText(String.valueOf(cart.getQty()));
            view.tvDesc.setText(String.valueOf(cart.getDesc()));

            view.etTotal.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            view.btAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnItemClickListener!=null){
                        view.total++;
                        view.etTotal.setText(String.valueOf(view.total));
                        mOnItemClickListener.onAddClick(v, items.get(position),view.total,position);
                    }

                }
            });
            view.btSubs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   if(mOnItemClickListener!=null){
                       if (view.total>0){
                           view.total--;
                           view.etTotal.setText(String.valueOf(view.total));
                           mOnItemClickListener.onSubsClick(v,items.get(position),view.total,position);
                       }
                   }

                }
            });
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}