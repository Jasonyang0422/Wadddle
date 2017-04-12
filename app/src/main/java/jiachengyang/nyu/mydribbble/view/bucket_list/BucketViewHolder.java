package jiachengyang.nyu.mydribbble.view.bucket_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import jiachengyang.nyu.mydribbble.R;

public class BucketViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.bucket_name) TextView bucketName;
    @BindView(R.id.bucket_shot_count) TextView bucketShotCount;
    @BindView(R.id.bucket_check_box) ImageView bucketCheckBox;

    public BucketViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
