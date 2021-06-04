package tech.mlsn.eatgo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.model.UserModel;
import tech.mlsn.eatgo.response.restaurant.UserRestaurantDataResponse;
import tech.mlsn.eatgo.tools.Tools;

public class UserDashboardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<UserRestaurantDataResponse> items;
    private List<UserRestaurantDataResponse> itemsFiltered;

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, UserRestaurantDataResponse obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public UserDashboardAdapter(Context context, List<UserRestaurantDataResponse> items) {
        this.items = items;
        this.itemsFiltered = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView name, address, phone, distance;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            name =  v.findViewById(R.id.tvName);
            address =  v.findViewById(R.id.tvAddress);
            distance = v.findViewById(R.id.tvDistance);
            phone = v.findViewById(R.id.tvPhone);
            lyt_parent = v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_dashboard, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            UserRestaurantDataResponse x = itemsFiltered.get(position);
            view.name.setText(x.getName());
            view.address.setText(x.getAddress());
            view.phone.setText("+62"+x.getPhone());
            view.distance.setText(Tools.distanceFormat(x.getDistance().toString()) +" km");

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
                    List<UserRestaurantDataResponse> filteredList = new ArrayList<>();
                    for (UserRestaurantDataResponse data : items) {
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
                itemsFiltered = (ArrayList<UserRestaurantDataResponse>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    @Override
    public int getItemCount() {
        return itemsFiltered.size();
    }
}