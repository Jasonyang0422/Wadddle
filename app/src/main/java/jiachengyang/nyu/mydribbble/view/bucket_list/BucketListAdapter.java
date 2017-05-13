package jiachengyang.nyu.mydribbble.view.bucket_list;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import jiachengyang.nyu.mydribbble.R;
import jiachengyang.nyu.mydribbble.model.Bucket;

public class BucketListAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_BUCKET = 1;
    private static final int VIEW_TYPE_LOADING = 2;

    private List<Bucket> buckets;
    private boolean showLoading;
    private LoadMoreListener loadMoreListener;

    public BucketListAdapter(List<Bucket> buckets, LoadMoreListener loadMoreListener) {
        this.buckets = buckets;
        this.loadMoreListener = loadMoreListener;
        this.showLoading = true;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == VIEW_TYPE_LOADING) {
            loadMoreListener.onLoadMore();
        }else {
            BucketViewHolder bucketViewHolder = (BucketViewHolder) holder;
            bucketViewHolder.bucketName.setText(buckets.get(position).name);
            bucketViewHolder.bucketShotCount.setText(buckets.get(position).shots_count + "shots");
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

    public void append(List<Bucket> moreBuckets) {
        buckets.addAll(moreBuckets);
        notifyDataSetChanged();
    }

    public int getDataCount() {return buckets.size();}

    public interface LoadMoreListener {
        void onLoadMore();
    }
}
