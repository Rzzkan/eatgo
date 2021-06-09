package tech.mlsn.eatgo.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.network.ApiClient;
import tech.mlsn.eatgo.response.menu.AllMenuDataResponse;
import tech.mlsn.eatgo.response.user.UserDataResponse;
import tech.mlsn.eatgo.tools.Tools;

public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<AllMenuDataResponse> items;
    private List<AllMenuDataResponse> itemsFiltered;

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private OnEditClickListener mOnEditClickListener;
    private OnDeleteClickListener mOnDeleteClickListener;

    private String role;


    public interface OnItemClickListener {
        void onItemClick(View view, AllMenuDataResponse obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public interface OnEditClickListener{
        void onItemClick(View view, AllMenuDataResponse obj, int position);
    }

    public void setOnEditClickListener(final OnEditClickListener mEditClickListener) {
        this.mOnEditClickListener = mEditClickListener;
    }

    public interface OnDeleteClickListener{
        void onItemClick(View view, AllMenuDataResponse obj, int position);
    }

    public void setOnDeleteClickListener(final OnDeleteClickListener mDeleteClickListener) {
        this.mOnDeleteClickListener = mDeleteClickListener;
    }

    public MenuAdapter(Context context, List<AllMenuDataResponse> items, String role) {
        this.items = items;
        this.itemsFiltered = items;
        this.role = role;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName,tvDesc, tvPrice, tvStatus;
        public ImageView ivMenu, ivMore;
        public RelativeLayout lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tvName);
            tvDesc = v.findViewById(R.id.tvDesc);
            tvPrice = v.findViewById(R.id.tvPrice);
            tvStatus = v.findViewById(R.id.tvStatus);
            ivMenu  = v.findViewById(R.id.ivMenu);
            ivMore = v.findViewById(R.id.ivMore);
            lyt_parent = v.findViewById(R.id.lytParent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
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
            if (!role.equalsIgnoreCase("resto")){
                view.ivMore.setVisibility(View.GONE);
            }
            if (x.getIsActive().toString().equalsIgnoreCase("0")){
                view.tvStatus.setText("Not Active");
            }else {
                view.tvStatus.setVisibility(View.GONE);
            }

            Glide.with(ctx).load(ApiClient.BASE_URL+x.getImage()).into(view.ivMenu);

            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, itemsFiltered.get(position), position);
                    }
                }
            });

            view.ivMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(ctx,((OriginalViewHolder) holder).ivMore);
                    popupMenu.inflate(R.menu.popup_menu);
                    MenuItem item = popupMenu.getMenu().getItem(1);
                    SpannableString s = new SpannableString("DELETE MENU");
                    s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(ctx, R.color.red)), 0, s.length(), 0);
                    item.setTitle(s);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.menu_edit:
                                    if (mOnEditClickListener != null) {
                                        mOnEditClickListener.onItemClick(v, items.get(position), position);
                                    }
                                    return true;
                                case R.id.menu_delete:
                                    if (mOnDeleteClickListener != null) {
                                        mOnDeleteClickListener.onItemClick(v, items.get(position), position);
                                    }
                                    return true;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
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