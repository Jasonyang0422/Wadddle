package jiachengyang.nyu.mydribbble.view.shot_list;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import jiachengyang.nyu.mydribbble.R;
import jiachengyang.nyu.mydribbble.dribbble.Dribbble;
import jiachengyang.nyu.mydribbble.model.Shot;
import jiachengyang.nyu.mydribbble.model.User;
import jiachengyang.nyu.mydribbble.view.base.SpaceItemDecoration;

public class ShotListFragment extends Fragment {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private ShotListAdapter shotListAdapter;
    private final int COUNT_PER_PAGE = 12;

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

        //adapter定义在global是因为onLoadMore要用到adapter, 所以必须定义成final, 但是final常量必须要初始化
        //所以定义在global不需要定义成final就能调用, 因此不需要初始化
        shotListAdapter = new ShotListAdapter(new ArrayList<Shot>(), new ShotListAdapter.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                new LoadShotTask(shotListAdapter.getItemCount() / COUNT_PER_PAGE + 1).execute();
            }
        });

        recyclerView.setAdapter(shotListAdapter);
    }

    private class LoadShotTask extends AsyncTask<Void, Void, List<Shot>> {

        private int page;

        public LoadShotTask(int page) {
            this.page = page;
        }

        @Override
        protected List<Shot> doInBackground(Void... params) {
            try {
                return Dribbble.getShots(page);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Shot> shots) {
            // this method is executed on UI thread!
            if(shots != null) {
                shotListAdapter.append(shots);
            }else {
                Snackbar.make(getView(), "Loading Shot Error!", Snackbar.LENGTH_LONG).show();
            }
        }
    }
}
