package jiachengyang.nyu.mydribbble.view.shot_list;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import jiachengyang.nyu.mydribbble.R;
import jiachengyang.nyu.mydribbble.model.Shot;
import jiachengyang.nyu.mydribbble.utils.ModelUtils;
import jiachengyang.nyu.mydribbble.view.shot_detail.ShotDetailActivity;
import jiachengyang.nyu.mydribbble.view.shot_detail.ShotDetailFragment;

public class ShotListAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_SHOT = 1;
    private static final int VIEW_TYPE_LOADING = 2;

    private List<Shot> shots;
    private LoadMoreListener loadMoreListener;
    private boolean showLoading;

    public ShotListAdapter(List<Shot> shots, LoadMoreListener loadMoreListener) {
        this.shots = shots;
        this.loadMoreListener = loadMoreListener;
        this.showLoading = true;
    }

    @Override
    public int getItemViewType(int position) {
        return position < shots.size() ? VIEW_TYPE_SHOT : VIEW_TYPE_LOADING;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_SHOT) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.shot_list_item, parent, false);
            return new ShotViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_loading, parent, false);
            return new RecyclerView.ViewHolder(view) {};
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if(viewType == VIEW_TYPE_LOADING) {
            loadMoreListener.onLoadMore();
        }else {
            final Shot shot = shots.get(position);
            ShotViewHolder shotViewHolder = (ShotViewHolder) holder;

            shotViewHolder.shotViewedCount.setText(shot.views_count + "");
            shotViewHolder.shotLikeCount.setText(shot.likes_count + "");
            shotViewHolder.shotBucketCount.setText(shot.buckets_count + "");

            // play gif or animations automatically
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(Uri.parse(shot.getImageUrl()))
                    .setAutoPlayAnimations(true)
                    .build();
            shotViewHolder.shotImage.setController(controller);

            shotViewHolder.cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ShotDetailActivity.class);
                    intent.putExtra(ShotDetailFragment.KEY_SHOT_DETAIL,
                            ModelUtils.toString(shot, new TypeToken<Shot>(){}));
                    intent.putExtra(ShotDetailActivity.KEY_SHOT_TITLE, shot.title);
                    context.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return showLoading ? shots.size() + 1 : shots.size();
    }

    public void append(List<Shot> moreShots) {
        shots.addAll(moreShots);
        notifyDataSetChanged();
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
        notifyDataSetChanged();
    }

    public interface LoadMoreListener {
        void onLoadMore();
    }
}
