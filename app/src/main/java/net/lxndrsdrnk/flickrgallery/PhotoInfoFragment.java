package net.lxndrsdrnk.flickrgallery;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PhotoInfoFragment extends Fragment {
    private static final String ARG_PHOTO_URL = "arg_photo_url";

    private String mPhotoUrl;

    @BindView(R.id.photoUrlText)
    TextView mPhotoUrlTextView;

    public PhotoInfoFragment() {
        // Required empty public constructor
    }


    public static PhotoInfoFragment newInstance(String photoUrl) {
        PhotoInfoFragment fragment = new PhotoInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PHOTO_URL, photoUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPhotoUrl = getArguments().getString(ARG_PHOTO_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photo_info, container, false);

        ButterKnife.bind(this, view);

//        mPhotoUrlTextView.setText(Html.fromHtml("<u>" + mPhotoUrl + "</u>"));
        mPhotoUrlTextView.setText(mPhotoUrl);
        mPhotoUrlTextView.setMovementMethod(LinkMovementMethod.getInstance());

        return view;
    }

}
