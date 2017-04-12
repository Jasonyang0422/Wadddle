package jiachengyang.nyu.mydribbble.view.bucket_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import jiachengyang.nyu.mydribbble.R;
import jiachengyang.nyu.mydribbble.model.Bucket;

public class BuckListAdapter extends RecyclerView.Adapter {

    private List<Bucket> buckets;

    public BuckListAdapter(List<Bucket> buckets) {
        this.buckets = buckets;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bucket_list_item, parent, false);
        return new BucketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BucketViewHolder bucketViewHolder = (BucketViewHolder) holder;
        bucketViewHolder.bucketName.setText(buckets.get(position).name);
        bucketViewHolder.bucketShotCount.setText(buckets.get(position).shots_count + "shots");
    }

    @Override
    public int getItemCount() {
        return buckets.size();
    }
}
