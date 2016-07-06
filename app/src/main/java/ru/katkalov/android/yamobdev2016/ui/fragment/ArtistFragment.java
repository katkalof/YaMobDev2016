package ru.katkalov.android.yamobdev2016.ui.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ru.katkalov.android.yamobdev2016.R;
import ru.katkalov.android.yamobdev2016.model.Artist;

public class ArtistFragment extends Fragment {
    private static final String ARG_ARTIST = "ARG_ARTIST";
    private Artist mArtist;
    private Picasso mPicasso;

    // Required empty public constructor
    public ArtistFragment() {}

    public static ArtistFragment newInstance(Artist artist) {
        ArtistFragment fragment = new ArtistFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_ARTIST, artist);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mArtist = getArguments().getParcelable(ARG_ARTIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(actionBar!=null){
            actionBar.setTitle(mArtist.getName());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        View rootView = inflater.inflate(R.layout.fragment_artist, container, false);
        mPicasso = new Picasso.Builder(getActivity()).build();
        mPicasso.load(mArtist.getBigCover())
                .placeholder(R.drawable.ic_music_note_black_48dp)
                .fit().centerCrop()
                .into((ImageView) rootView.findViewById(R.id.artist_image));
        ((TextView) rootView.findViewById(R.id.artist_subtitle)).setText(mArtist.getGenres().toString());
        StringBuilder sb = new StringBuilder();
        sb.append(getResources()
                .getQuantityString(R.plurals.numberOfSongs, mArtist.getCountTracks(), mArtist.getCountTracks()));
        sb.append(getString(R.string.dot));
        sb.append(getResources()
                .getQuantityString(R.plurals.numberOfAlbums, mArtist.getCountAlbums(), mArtist.getCountAlbums()));
        ((TextView) rootView.findViewById(R.id.artist_stat)).setText(sb);
        ((TextView) rootView.findViewById(R.id.artist_biography)).setText(mArtist.getDescription());
        return rootView;
    }


    @Override
    public void onDestroyView() {
        mArtist = null;
        if ( mPicasso != null ) {
            mPicasso.shutdown();
        }
        mPicasso = null;
        super.onDestroyView();
    }

}
