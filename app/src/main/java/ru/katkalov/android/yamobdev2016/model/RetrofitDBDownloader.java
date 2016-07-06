package ru.katkalov.android.yamobdev2016.model;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitDBDownloader {
        @GET("mobilization-2016/artists.json")
        Call<List<Artist>> downloadDB();
}
