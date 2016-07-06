package ru.katkalov.android.yamobdev2016.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.katkalov.android.yamobdev2016.R;
import ru.katkalov.android.yamobdev2016.model.Artist;
import ru.katkalov.android.yamobdev2016.model.ArtistDatabaseHelper;
import ru.katkalov.android.yamobdev2016.model.RetrofitDBDownloader;
import ru.katkalov.android.yamobdev2016.ui.recyclerview.ArtistsAdapter;
import ru.katkalov.android.yamobdev2016.ui.recyclerview.EndlessRecyclerViewScrollListener;
import ru.katkalov.android.yamobdev2016.ui.view.ErrorView;

public class ArtistsListFragment extends Fragment {

    private static final int DOWNLOADS_NUMBER = 30;
    private View mLoadingView;
    private ErrorView mErrorView;
    private Picasso mPicasso;
    private RecyclerView mRecyclerView;
    private ArtistDatabaseHelper mDBHelper;
    private ArtistsAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_artist_list, container, false);
        mLoadingView = rootView.findViewById(R.id.loading_view);
        mErrorView = (ErrorView) rootView.findViewById(R.id.error_view);
        mErrorView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadDatabase();
            }
        });
        mDBHelper = ArtistDatabaseHelper.getInstance(getActivity());

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.artist_list);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new ArtistsAdapter(new ArrayList<Artist>());

        mAdapter.setOnErrorClickListener(new ArtistsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                downloadDatabase();
            }
        });

        mAdapter.setOnItemClickListener(new ArtistsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                getFragmentManager()
                        .beginTransaction()
//                        .setCustomAnimations(R.animator.slide_to_left,R.animator.slide_to_right)

                        .setCustomAnimations(
                                R.animator.slide_to_left, R.animator.slide_out_left,
                                R.animator.slide_to_right, R.animator.slide_out_right)
                        .replace(R.id.container, ArtistFragment.newInstance(mAdapter.getItem(position)))
                        .addToBackStack(null)
                        .commit();
            }
        });

        mPicasso = new Picasso.Builder(getActivity()).build();
        mAdapter.setImageLoader(mPicasso);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        // Add the scroll listener
        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                fetchArtistsFromDB();
            }
        });
        downloadDatabase();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ActionBar mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setTitle(R.string.artists);
            mActionBar.setSubtitle(null);
            mActionBar.setDisplayHomeAsUpEnabled(false);
        }

    }

    @Override
    public void onDestroyView() {
        if (mPicasso != null) {
            mPicasso.shutdown();
        }
        mPicasso = null;
        super.onDestroyView();
    }

    private void downloadDatabase() {
        mLoadingView.setVisibility(View.VISIBLE);
        //Проверяем наличие базы данных
        if (mDBHelper.isEmpty()) {
            // Проверяем подключение к интернету, если нет то показываем фрагмент с кнопкой перепопробовать
            // isOnline не работает в эмуляторе
            if (isNetworkAvailable()) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://download.cdn.yandex.net/")
//                        .baseUrl("http://download.cdn.yandex.net/mobilization-2016/artists.json/")
//                        .baseUrl("http://cache-default01f.cdn.yandex.net/download.cdn.yandex.net/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                Call<List<Artist>> repos = retrofit.create(RetrofitDBDownloader.class).downloadDB();
                repos.enqueue(new Callback<List<Artist>>() {
                    @Override
                    public void onResponse(Call<List<Artist>> call, Response<List<Artist>> response) {
                        mDBHelper.addArtist(response.body());
                        fetchArtistsFromDB();
                    }

                    @Override
                    public void onFailure(Call<List<Artist>> call, Throwable t) {
                        mErrorView.setVisibility(View.VISIBLE);
                        mLoadingView.setVisibility(View.GONE);
                        Snackbar.make(mRecyclerView, R.string.error_download, Snackbar.LENGTH_LONG).show();
                    }
                });
            } else {
                mErrorView.setVisibility(View.VISIBLE);
                mLoadingView.setVisibility(View.GONE);
                Snackbar.make(mRecyclerView, R.string.error_internet, Snackbar.LENGTH_LONG).show();
            }
        } else {
            //Работаем с существующей базой
            fetchArtistsFromDB();
        }
    }

    // Append more data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void fetchArtistsFromDB() {
        mErrorView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.GONE);
        mAdapter.addItems(mDBHelper.getArtists(DOWNLOADS_NUMBER, mAdapter.getItemCount()));
        // For efficiency purposes, notify the adapter of only the elements that got changed
        // curSize will equal to the index of the first element inserted because the list is 0-indexed
        int curSize = mAdapter.getItemCount();
        mAdapter.notifyItemRangeInserted(curSize, mAdapter.getItemCount() - 1);
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
