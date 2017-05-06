package jiachengyang.nyu.mydribbble.view.shot_detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import jiachengyang.nyu.mydribbble.R;
import jiachengyang.nyu.mydribbble.model.Shot;
import jiachengyang.nyu.mydribbble.utils.ModelUtils;

public class ShotDetailFragment extends Fragment {

    public static final String KEY_SHOT_DETAIL = "shot_detail";

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    public static ShotDetailFragment newInstance(Bundle args) {
        ShotDetailFragment shotDetailFragment = new ShotDetailFragment();
        shotDetailFragment.setArguments(args);
        return shotDetailFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Shot shot = ModelUtils
                .toObject(getArguments().getString(KEY_SHOT_DETAIL), new TypeToken<Shot>(){});

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ShotDetailAdapter(shot));
    }

}
