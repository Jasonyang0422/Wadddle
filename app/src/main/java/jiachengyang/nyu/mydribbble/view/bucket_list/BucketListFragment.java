package jiachengyang.nyu.mydribbble.view.bucket_list;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import jiachengyang.nyu.mydribbble.R;
import jiachengyang.nyu.mydribbble.dribbble.Dribbble;
import jiachengyang.nyu.mydribbble.model.Bucket;
import jiachengyang.nyu.mydribbble.view.base.SpaceItemDecoration;

public class BucketListFragment extends Fragment {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.fab) FloatingActionButton floatingActionButton;

    public static final int REQ_CODE_NEW_BUCKET = 100;
    final private int COUNT_PER_PAGE = 12;
    public static final String KEY_CHOOSING_MODE = "choose_mode";
    public static final String KEY_CHOSEN_BUCKET_IDS = "chosen_bucket_ids";

    private BucketListAdapter adapter;
    private boolean isChoosingMode;
    private List<String> chosenBucketIds;


    public static BucketListFragment newInstance(boolean isChoosingMode, ArrayList<String> chosenBucketIds) {
        Bundle args = new Bundle();
        args.putBoolean(KEY_CHOOSING_MODE, isChoosingMode);
        args.putStringArrayList(KEY_CHOSEN_BUCKET_IDS, chosenBucketIds);

        BucketListFragment fragment = new BucketListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(isChoosingMode) {
            inflater.inflate(R.menu.bucket_list_choose_mode_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.save) {
            ArrayList<String> chosenBucketIds = adapter.getSelectedBucketIds();

            Intent intent = new Intent();
            intent.putStringArrayListExtra(KEY_CHOSEN_BUCKET_IDS, chosenBucketIds);
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.from(getContext()).inflate(R.layout.fragment_fab_recycler_view, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        isChoosingMode = getArguments().getBoolean(KEY_CHOOSING_MODE);
        if(isChoosingMode) {
            chosenBucketIds = getArguments().getStringArrayList(KEY_CHOSEN_BUCKET_IDS);
            if(chosenBucketIds == null) {
                chosenBucketIds = new ArrayList<>();
            }
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(
                getResources().getDimensionPixelSize(R.dimen.spacing_medium)));

        adapter = new BucketListAdapter(new ArrayList<Bucket>(), new BucketListAdapter.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                AsyncTaskCompat.executeParallel(
                        new LoadBucketTask(adapter.getDataCount() / COUNT_PER_PAGE + 1));
            }
        }, isChoosingMode);

        recyclerView.setAdapter(adapter);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //https://guides.codepath.com/android/Using-DialogFragment
                //Passing data to parent fragment
                NewBucketDialogFragment newBucketDialogFragment = NewBucketDialogFragment.newInstance();
                newBucketDialogFragment.setTargetFragment(BucketListFragment.this, REQ_CODE_NEW_BUCKET);
                newBucketDialogFragment.show(getFragmentManager(), NewBucketDialogFragment.TAG);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQ_CODE_NEW_BUCKET && resultCode == Activity.RESULT_OK) {
            String bucketName = data.getStringExtra(NewBucketDialogFragment.KEY_BUCKET_NAME);
            String bucketDescription = data.getStringExtra(NewBucketDialogFragment.KEY_BUCKET_DESCRIPTION);
            if (!TextUtils.isEmpty(bucketName)) {
                AsyncTaskCompat.executeParallel(new CreateBucketTask(bucketName, bucketDescription));
            }
        }
    }

    private class LoadBucketTask extends AsyncTask<Void, Void, List<Bucket>> {

        private int page;

        public LoadBucketTask(int page) {
            this.page = page;
        }

        @Override
        protected List<Bucket> doInBackground(Void... params) {
            try {
                return Dribbble.getUserBuckets(page);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Bucket> buckets) {
            if(buckets != null) {
                if(isChoosingMode) {
                    for(Bucket bucket : buckets) {
                        if (chosenBucketIds.contains(bucket.id)) {
                            bucket.isChoosing = true;
                        }
                    }
                }
                adapter.append(buckets);
                //if size is less than count_per_page, no more buckets can be retrieved
                adapter.setShowLoading(buckets.size() == COUNT_PER_PAGE);
            } else {
                Snackbar.make(getView(), "Error!", Snackbar.LENGTH_LONG).show();
            }

        }
    }

    private class CreateBucketTask extends AsyncTask<Void, Void, Bucket> {

        private String name;
        private String description;

        public CreateBucketTask(String name, String description) {
            this.name = name;
            this.description =description;
        }

        @Override
        protected Bucket doInBackground(Void... params) {
            try {
                return Dribbble.createBucket(name, description);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bucket bucket) {
            // this method is executed on UI thread!!!!
            if(bucket != null) {
                adapter.prepend(Collections.singletonList(bucket));
            }else {
                Snackbar.make(getView(), "Error!", Snackbar.LENGTH_LONG).show();
            }
        }
    }

}
