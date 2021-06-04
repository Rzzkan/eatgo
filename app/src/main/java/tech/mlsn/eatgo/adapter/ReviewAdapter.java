package tech.mlsn.eatgo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import tech.mlsn.eatgo.R;
import tech.mlsn.eatgo.network.ApiClient;
import tech.mlsn.eatgo.response.restaurant.RestaurantDataResponse;
import tech.mlsn.eatgo.response.review.ReviewDataResponse;

public class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ReviewDataResponse> items;
    private List<ReviewDataResponse> itemsFiltered;

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, ReviewDataResponse obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public ReviewAdapter(Context context, List<ReviewDataResponse> items) {
        this.items = items;
        this.itemsFiltered = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvRating, tvReview;
        public CircularImageView ivImage;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            tvName =  v.findViewById(R.id.tvName);
            ivImage = v.findViewById(R.id.ivPhoto);
            tvRating = v.findViewById(R.id.tvRating);
            tvReview = v.findViewById(R.id.tvReview);
            lyt_parent = v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
            ReviewDataResponse x = itemsFiltered.get(position);
            view.tvName.setText(x.getName());
            view.tvRating.setText(x.getRating().toString());
            view.tvReview.setText(x.getReview());
            Glide.with(ctx).load(ApiClient.BASE_URL+ x.getImage()).into(view.ivImage);

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
                    List<ReviewDataResponse> filteredList = new ArrayList<>();
                    for (ReviewDataResponse data : items) {
                        String name = data.getReview().toLowerCase().trim();
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
                itemsFiltered = (ArrayList<ReviewDataResponse>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    @Override
    public int getItemCount() {
        return itemsFiltered.size();
    }
}