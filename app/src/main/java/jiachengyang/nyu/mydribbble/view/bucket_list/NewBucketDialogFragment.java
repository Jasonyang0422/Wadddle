package jiachengyang.nyu.mydribbble.view.bucket_list;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import jiachengyang.nyu.mydribbble.R;

public class NewBucketDialogFragment extends DialogFragment {

    @BindView(R.id.new_bucket_name) EditText bucketName;
    @BindView(R.id.new_bucket_description) EditText bucketDescription;

    public static final String TAG = "NewBucketDialogFragment";

    public static final String KEY_BUCKET_NAME = "bucket_name";
    public static final String KEY_BUCKET_DESCRIPTION = "bucket_description";

    public static NewBucketDialogFragment newInstance() {
        return new NewBucketDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_dialog_new_bucket, null);
        ButterKnife.bind(this, view);

        return new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle(R.string.new_bucket_title)
                .setPositiveButton(R.string.new_bucket_create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putExtra(KEY_BUCKET_NAME, bucketName.getText().toString());
                        intent.putExtra(KEY_BUCKET_DESCRIPTION, bucketDescription.getText().toString());

                        // Manually call onActivityResult function from BucketListFragment instance
                        // Activity的传递中没有手动call，也许是因为finish()主动call了onActivityResult
                        // 再加上我们获取不到Activity的instance
                        getTargetFragment().onActivityResult(BucketListFragment.REQ_CODE_NEW_BUCKET,
                                                             Activity.RESULT_OK,
                                                             intent);
                        dismiss();
                    }
                })
                .setNegativeButton(R.string.new_bucket_cancel, null)
                .show();
    }

}
