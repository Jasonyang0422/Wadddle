package jiachengyang.nyu.mydribbble.view.shot_detail;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import jiachengyang.nyu.mydribbble.R;

public class ShotDetailImageViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.shot_image) SimpleDraweeView shotImage;

    public ShotDetailImageViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
