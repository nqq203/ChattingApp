package com.main.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.group4.matchmingle.R;

public class ImageViewerFragment extends Fragment {
    private String imageUrl;

    public ImageViewerFragment() {
        // Required empty public constructor
    }

    public static ImageViewerFragment newInstance(String imageUrl) {
        ImageViewerFragment fragment = new ImageViewerFragment();
        Bundle args = new Bundle();
        args.putString("image_url", imageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageUrl = getArguments().getString("image_url");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_viewer_fragment, container, false);
        ImageView imageView = view.findViewById(R.id.image_fullscreen);
        ImageView closeButton = view.findViewById(R.id.button_close);

        Glide.with(this).load(imageUrl).into(imageView);

        closeButton.setOnClickListener(v -> {
            if (getParentFragmentManager() != null) {
                getParentFragmentManager().beginTransaction().remove(ImageViewerFragment.this).commit();
                getActivity().findViewById(R.id.image_viewer_container).setVisibility(View.GONE);
            }
        });

        return view;
    }
}

