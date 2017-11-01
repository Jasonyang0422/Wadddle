package jiachengyang.nyu.mydribbble.view.shot_detail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;

import java.util.ArrayList;

import jiachengyang.nyu.mydribbble.R;
import jiachengyang.nyu.mydribbble.model.Shot;
import jiachengyang.nyu.mydribbble.view.bucket_list.BucketListFragment;
import jiachengyang.nyu.mydribbble.view.bucket_list.ChooseBucketActivity;

public class ShotDetailAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_SHOT_DETAIL_IMAGE = 0;
    private static final int VIEW_TYPE_SHOT_DETAIL_INFO = 1;

    private ShotDetailFragment shotDetailFragment;
    private Shot shot;

    private ArrayList<String> collectedBucketIds;

    public ShotDetailAdapter(Shot shot,
                             ShotDetailFragment shotDetailFragment) {
        this.shot = shot;
        this.shotDetailFragment = shotDetailFragment;
        this.collectedBucketIds = null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_SHOT_DETAIL_IMAGE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.shot_detail_image, parent, false);
                return new ShotDetailImageViewHolder(view);
            case VIEW_TYPE_SHOT_DETAIL_INFO:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.shot_detail_info, parent, false);
                return new ShotDetailInfoViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case VIEW_TYPE_SHOT_DETAIL_IMAGE:
                // play gif automatically
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setUri(Uri.parse(shot.getImageUrl()))
                        .setAutoPlayAnimations(true)
                        .build();
                ((ShotDetailImageViewHolder) holder).shotImage.setController(controller);
                break;
            case VIEW_TYPE_SHOT_DETAIL_INFO:
                ShotDetailInfoViewHolder shotDetailInfoViewHolder = (ShotDetailInfoViewHolder) holder;
                shotDetailInfoViewHolder.title.setText(shot.title);
                shotDetailInfoViewHolder.authorName.setText(shot.user.name);
                shotDetailInfoViewHolder.description.setText(shot.description);
                shotDetailInfoViewHolder.authorPicture.setImageURI(Uri.parse(shot.user.avatar_url));

                shotDetailInfoViewHolder.likeCount.setText(String.valueOf(shot.likes_count));
                shotDetailInfoViewHolder.bucketCount.setText(String.valueOf(shot.buckets_count));
                shotDetailInfoViewHolder.viewCount.setText(String.valueOf(shot.views_count));

                shotDetailInfoViewHolder.shareButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        share(v.getContext());
                    }
                });

                shotDetailInfoViewHolder.bucketButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bucket(v.getContext());
                    }
                });
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) {
            return VIEW_TYPE_SHOT_DETAIL_IMAGE;
        }else {
            return VIEW_TYPE_SHOT_DETAIL_INFO;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    private void share(Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, shot.title + " " + shot.html_url);
        intent.setType("text/plain");
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_shot)));
    }

    private void bucket(Context context) {
        if(collectedBucketIds != null) {
            Intent intent = new Intent(context, ChooseBucketActivity.class);
            intent.putStringArrayListExtra(BucketListFragment.KEY_CHOSEN_BUCKET_IDS, collectedBucketIds);
            shotDetailFragment.startActivityForResult(intent, shotDetailFragment.REQ_CODE_BUCKET);
        }
    }
}
