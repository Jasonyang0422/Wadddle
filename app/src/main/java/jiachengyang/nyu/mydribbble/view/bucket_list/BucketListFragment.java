package jiachengyang.nyu.mydribbble.view.bucket_list;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import jiachengyang.nyu.mydribbble.R;
import jiachengyang.nyu.mydribbble.model.Bucket;
import jiachengyang.nyu.mydribbble.view.base.SpaceItemDecoration;

public class BucketListFragment extends Fragment {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    BucketListAdapter adapter;

    public static BucketListFragment newInstance() {
        return new BucketListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.from(getContext()).inflate(R.layout.fragment_recycler_view, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(
                getResources().getDimensionPixelSize(R.dimen.spacing_medium)));

        final Handler handler = new Handler();
        adapter = new BucketListAdapter(new ArrayList<Bucket>(), new BucketListAdapter.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            this.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter.append(fakeData(12));
                            }
                        });
                    }
                }.start();
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private List<Bucket> fakeData(int num) {
        List<Bucket> bucketList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < num; ++i) {
            Bucket bucket = new Bucket();
            bucket.name = "Bucket" + i;
            bucket.shots_count = random.nextInt(10);
            bucketList.add(bucket);
        }
        return bucketList;
    }
}
