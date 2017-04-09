package jiachengyang.nyu.mydribbble.view.shot_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import jiachengyang.nyu.mydribbble.R;

public class ShotViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.shot_image) ImageView shotImage;
    @BindView(R.id.shot_viewed_count) TextView shotViewedCount;
    @BindView(R.id.shot_like_count) TextView shotLikeCount;
    @BindView(R.id.shot_bucket_count) TextView shotBucketCount;


    public ShotViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}
