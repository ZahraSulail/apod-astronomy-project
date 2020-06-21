package com.barmej.apod.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.barmej.apod.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

/*
  A fragment that show information about the purpose of this app and displays Nasa Logo Nasa  ,
 */

public class AboutFragment extends DialogFragment {

    private TextView aboutTitleTextView;
    private TextView aboutTextTextView;
    private ImageView nasaIconImageView;

    /*
    Inflate the fragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_about, container, false );
        return view;
    }

    /*
      Initialize and set variables
     */

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );
        aboutTitleTextView = view.findViewById( R.id.txt_about_title );
        aboutTextTextView = view.findViewById( R.id.txt_about );
        nasaIconImageView = view.findViewById( R.id.icon_nasa_logo );
        aboutTitleTextView.setText( R.string.about_label );
        aboutTextTextView.setText( R.string.about_content );
        nasaIconImageView.setImageResource( R.drawable.ic_nasa_logo );

    }
}
