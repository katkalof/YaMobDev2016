package ru.katkalov.android.yamobdev2016.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ru.katkalov.android.yamobdev2016.R;

public class AboutFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.fragment_about);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        Button sendButton = (Button) rootView.findViewById(R.id.send_email_to_developer);
        sendButton.setOnClickListener(this);
        return rootView;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_email_to_developer:
                Intent i = new Intent(Intent.ACTION_SENDTO);
                i.setType("text/plain");
                i.setData(Uri.parse("mailto:"));
                Resources res = getResources();
                i.putExtra(Intent.EXTRA_EMAIL, res.getStringArray(R.array.emails));
                i.putExtra(Intent.EXTRA_SUBJECT, res.getString(R.string.email_theme));
                if (i.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(i);
                }
                break;
        }
    }
}
