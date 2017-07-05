package jiachengyang.nyu.mydribbble.view.bucket_list;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import jiachengyang.nyu.mydribbble.R;
import jiachengyang.nyu.mydribbble.model.Bucket;

public class BucketListAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_BUCKET = 1;
    private static final int VIEW_TYPE_LOADING = 2;

    private List<Bucket> buckets;
    private boolean showLoading;
    private LoadMoreListener loadMoreListener;
    private boolean isChoosingMode;

    public BucketListAdapter(List<Bucket> buckets, LoadMoreListener loadMoreListener, boolean isChoosingMode) {
        this.buckets = buckets;
        this.loadMoreListener = loadMoreListener;
        this.showLoading = true;
        this.isChoosingMode = isChoosingMode;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_BUCKET) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.bucket_list_item, parent, false);
            return new BucketViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_loading, parent, false);
            return new RecyclerView.ViewHolder(view){};
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(getItemViewType(position) == VIEW_TYPE_LOADING) {
            loadMoreListener.onLoadMore();
        }else {
            final Bucket bucket = buckets.get(position);
            BucketViewHolder bucketViewHolder = (BucketViewHolder) holder;

            Context context = holder.itemView.getContext();

            bucketViewHolder.bucketName.setText(bucket.name);
            bucketViewHolder.bucketShotCount.setText(bucket.shots_count + "shots");

            if(isChoosingMode) {
                bucketViewHolder.bucketCheckBox.setVisibility(View.VISIBLE);
                bucketViewHolder.bucketCheckBox.setImageDrawable(
                        bucket.isChoosing
                                ? ContextCompat.getDrawable(context, R.drawable.ic_check_box_black_24px)
                                : ContextCompat.getDrawable(context, R.drawable.ic_check_box_outline_blank_black_24px));
                bucketViewHolder.bucketLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bucket.isChoosing = !bucket.isChoosing;
                        notifyItemChanged(position);
                    }
                });

            } else {
                bucketViewHolder.bucketCheckBox.setVisibility(View.GONE);
                bucketViewHolder.bucketLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return showLoading ? buckets.size() + 1 : buckets.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position < buckets.size() ? VIEW_TYPE_BUCKET : VIEW_TYPE_LOADING;
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
        notifyDataSetChanged();
    }

    public ArrayList<String> getSelectedBucketIds() {
        ArrayList<String> selectedBucketIds = new ArrayList<>();
        for(Bucket bucket : buckets) {
            if(bucket.isChoosing) {
                selectedBucketIds.add(bucket.id);
            }
        }
        return selectedBucketIds;
    }

    public void append(List<Bucket> moreBuckets) {
        buckets.addAll(moreBuckets);
        notifyDataSetChanged();
    }

    public void prepend(List<Bucket> moreBuckets) {
        buckets.addAll(0, moreBuckets);
        notifyDataSetChanged();
    }

    public int getDataCount() {return buckets.size();}

    public interface LoadMoreListener {
        void onLoadMore();
    }
}
