package com.uykazdal.gezginapp;

import android.support.v4.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProfileFragment extends Fragment {
    private void openInstagram() {
        Uri instagramUri = Uri.parse("https://www.twitter.com/UgurYasarKazdal");
        Intent instagramIntent = new Intent(Intent.ACTION_VIEW, instagramUri);

        instagramIntent.setPackage("com.twitter.android");

        try {
            startActivity(instagramIntent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.instagram.com/gezlist")));
        }

    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view,
                              Bundle savedInstanceState) {

        view.findViewById(R.id.img_profile_insta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInstagram();
            }
        });

    }
}
