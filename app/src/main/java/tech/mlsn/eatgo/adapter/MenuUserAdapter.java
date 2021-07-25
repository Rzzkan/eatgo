package tech.mlsn.eatgo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.network.ApiClient;
import tech.mlsn.eatgo.response.menu.AllMenuDataResponse;
import tech.mlsn.eatgo.tools.Tools;

public class MenuUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<AllMenuDataResponse> items;
    private List<AllMenuDataResponse> itemsFiltered;

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private OnAddClickListener mOnAddClickListener;
    private OnSubsClickListener mOnSubsClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, AllMenuDataResponse obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public interface OnAddClickListener{
        void onItemClick(View view, AllMenuDataResponse obj, int position);
    }

    public void setOnAddClickListener(final OnAddClickListener mAddClickListener) {
        this.mOnAddClickListener = mAddClickListener;
    }

    public interface OnSubsClickListener{
        void onItemClick(View view, AllMenuDataResponse obj, int position);
    }

    public void setOnSubsClickListener(final OnSubsClickListener mSubsClickListener) {
        this.mOnSubsClickListener = mSubsClickListener;
    }

    public MenuUserAdapter(Context context, List<AllMenuDataResponse> items) {
        this.items = items;
        this.itemsFiltered = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName,tvDesc, tvPrice;
        public ImageView ivMenu;
        public ImageButton btnSubs, btnAdd;
        public EditText etCounter;
        public RelativeLayout lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tvProductName);
            tvDesc = v.findViewById(R.id.tvDesc);
            tvPrice = v.findViewById(R.id.tvPrice);
            ivMenu  = v.findViewById(R.id.imgProduct);
            btnSubs = v.findViewById(R.id.subs);
            btnAdd = v.findViewById(R.id.add);
            etCounter = v.findViewById(R.id.etTotal);
            lyt_parent = v.findViewById(R.id.lytParent);
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
            AllMenuDataResponse x = itemsFiltered.get(position);
            view.tvName.setText(x.getName());
            view.tvDesc.setText(x.getDescription());
            view.tvPrice.setText(Tools.currency(x.getPrice()));
            Glide.with(ctx).load(ApiClient.BASE_URL+x.getImage()).into(view.ivMenu);

            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, itemsFiltered.get(position), position);
                    }
                }
            });

        }
    }

    public Filter getFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                Log.d("Filter", "performFiltering: "+charString);
                if (charString.isEmpty()) {
                    itemsFiltered = items;
                } else {
                    List<AllMenuDataResponse> filteredList = new ArrayList<>();
                    for (AllMenuDataResponse data : items) {
                        String name = data.getName().toLowerCase().trim();
                        if(name.contains(charString.toLowerCase().trim())){
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
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                itemsFiltered = (ArrayList<AllMenuDataResponse>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    @Override
    public int getItemCount() {
        return itemsFiltered.size();
    }
}