package tech.mlsn.eatgo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import java.util.ArrayList;

import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.response.OrdersDataResponse;
import tech.mlsn.eatgo.tools.Tools;

public class AdapterOrder extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private ArrayList<OrdersDataResponse> items = new ArrayList<>();
    private ArrayList<OrdersDataResponse> itemsFiltered = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;


    public interface OnItemClickListener {
        void onItemClick(View view, OrdersDataResponse obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterOrder(Context context, ArrayList<OrdersDataResponse> items) {
        this.items = items;
        this.itemsFiltered = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        CircularImageView imgProfile;
        TextView tvName, tvDate, tvInvoice;
        CardView lytCard;
        LinearLayout lytParent;
        View lyt_Ripple;

        public OriginalViewHolder(View v) {
            super(v);
            imgProfile = v.findViewById(R.id.imgProfile);
            tvName = v.findViewById(R.id.tvName);
            tvDate = v.findViewById(R.id.tvDate);
            tvInvoice = v.findViewById(R.id.tvInvoice);
            lytCard = v.findViewById(R.id.lytCard);
            lytParent = v.findViewById(R.id.lyt_parent);
            lyt_Ripple = v.findViewById(R.id.lyt_ripple);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            OrdersDataResponse p = itemsFiltered.get(position);
            view.tvInvoice.setText("Rp." + p.getTotal());
            view.tvDate.setText(Tools.dateParserChat(p.getDate()));
            view.tvName.setText(p.getUserName());
            view.lyt_Ripple.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, itemsFiltered.get(position), position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return itemsFiltered.size();
    }

    public void insertItem(OrdersDataResponse item) {
        this.items.add(item);
        notifyItemInserted(getItemCount());
        if (getItemCount() > 1) notifyItemChanged(getItemCount() - 2);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    itemsFiltered = items;
                }else if (charString.equalsIgnoreCase("1")){
                    ArrayList<OrdersDataResponse> filteredList = new ArrayList<>();
                    for (OrdersDataResponse data : items) {
                        String status = data.getStatus();
                        if(status.equalsIgnoreCase("1")){
                            filteredList.add(data);
                        }
                    }
                    itemsFiltered = filteredList;
                }else if (charString.equalsIgnoreCase("2"))  {
                    ArrayList<OrdersDataResponse> filteredList = new ArrayList<>();
                    for (OrdersDataResponse data : items) {
                        String  status = data.getStatus();
                        if(status.equalsIgnoreCase("2")){
                            filteredList.add(data);
                        }
                    }
                    itemsFiltered = filteredList;
                }else if (charString.equalsIgnoreCase("3"))  {
                    ArrayList<OrdersDataResponse> filteredList = new ArrayList<>();
                    for (OrdersDataResponse data : items) {
                        String  status = data.getStatus();
                        if(status.equalsIgnoreCase("3")){
                            filteredList.add(data);
                        }
                    }
                    itemsFiltered = filteredList;
                } else if (charString.equalsIgnoreCase("4"))  {
                    ArrayList<OrdersDataResponse> filteredList = new ArrayList<>();
                    for (OrdersDataResponse data : items) {
                        String  status = data.getStatus();
                        if(status.equalsIgnoreCase("3")){
                            filteredList.add(data);
                        }
                    }
                    itemsFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = itemsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                itemsFiltered = (ArrayList<OrdersDataResponse>) results.values;
                notifyDataSetChanged();
            }
        };

    }

}