package jiachengyang.nyu.mydribbble.view.shot_list;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import jiachengyang.nyu.mydribbble.R;
import jiachengyang.nyu.mydribbble.model.Shot;
import jiachengyang.nyu.mydribbble.model.User;
import jiachengyang.nyu.mydribbble.view.base.SpaceItemDecoration;

public class ShotListFragment extends Fragment {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private ShotListAdapter shotListAdapter;
    private final int COUNT_PER_PAGE = 20;

   public static ShotListFragment newInstance() {
       return new ShotListFragment();
   }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(
                getResources().getDimensionPixelSize(R.dimen.spacing_medium)));

        final Handler handler = new Handler();

        //adapter定义在global是因为onLoadMore要用到adapter, 所以必须定义成final, 但是final常量必须要初始化
        //所以定义在global不需要定义成final就能调用, 因此不需要初始化
        shotListAdapter = new ShotListAdapter(fakeData(0), new ShotListAdapter.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    List<Shot> moreShots = fakeData(shotListAdapter.getItemCount());
                                    shotListAdapter.append(moreShots);
                                    shotListAdapter.setShowLoading(moreShots.size() == COUNT_PER_PAGE);
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

        recyclerView.setAdapter(shotListAdapter);
    }

    private List<Shot> fakeData(int itemCount) {
        List<Shot> shotList = new ArrayList<>();
        Random random = new Random();
        int count = itemCount / COUNT_PER_PAGE < 2 ? COUNT_PER_PAGE : 10;

        for (int i = 0; i < count; ++i) {
            Shot shot = new Shot();
            shot.title = "shot" + i;
            shot.views_count = random.nextInt(10000);
            shot.likes_count = random.nextInt(200);
            shot.buckets_count = random.nextInt(50);
            shot.description = makeDescription();

            shot.user = new User();
            shot.user.name = shot.title + " author";

            shotList.add(shot);
        }
        return shotList;
    }

    private static final String[] words = {
            "bottle", "bowl", "brick", "building", "bunny", "cake", "car", "cat", "cup",
            "desk", "dog", "duck", "elephant", "engineer", "fork", "glass", "griffon", "hat", "key",
            "knife", "lawyer", "llama", "manual", "meat", "monitor", "mouse", "tangerine", "paper",
            "pear", "pen", "pencil", "phone", "physicist", "planet", "potato", "road", "salad",
            "shoe", "slipper", "soup", "spoon", "star", "steak", "table", "terminal", "treehouse",
            "truck", "watermelon", "window"
    };

    private static String makeDescription() {
        return TextUtils.join(" ", words);
    }
}
