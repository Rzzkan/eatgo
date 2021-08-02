package tech.mlsn.eatgo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.model.DetailOrderModel;

import java.util.ArrayList;


public class AdapterDetailOrder extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<DetailOrderModel> items;

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, DetailOrderModel obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterDetailOrder(Context context, ArrayList<DetailOrderModel> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public View lyt_parent;
        public TextView tvItemCount, tvItemName, tvTotalItemPrice;

        public OriginalViewHolder(View v) {
            super(v);
            tvItemCount = v.findViewById(R.id.tvItemCount);
            tvItemName = v.findViewById(R.id.tvItemName);
            tvTotalItemPrice = v.findViewById(R.id.tvTotalItemPrice);
            lyt_parent =  v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_order, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            DetailOrderModel data = items.get(position);
            view.tvItemCount.setText(data.getQty().toString()+"x");
            view.tvTotalItemPrice.setText("Rp. "+ Integer.valueOf(data.getQty())*Integer.valueOf(data.getPrice()));
            view.tvItemName.setText(data.getMenu());
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