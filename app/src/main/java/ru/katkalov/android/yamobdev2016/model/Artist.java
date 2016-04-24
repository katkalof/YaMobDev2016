package ru.katkalov.android.yamobdev2016.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Artist implements Parcelable {
    private Integer id;
    private String name;
    private String genres;
    private Integer countTracks;
    private Integer countAlbums;
    private String link;
    private String description;
    private String smallCover;
    private String bigCover;


    public Artist(int id, String name, String genres, int countTracks, int countAlbums, String link, String description, String smallCover, String bigCover) {
        this.id = id;
        this.name = name;
        this.genres = genres;
        this.countTracks = countTracks;
        this.countAlbums = countAlbums;
        this.link = link;
        this.description = description;
        this.smallCover = smallCover;
        this.bigCover = bigCover;
    }


    private Artist() {
    }

    public String getGenres() {
        return genres;
    }

    public String getSmallCover() {
        return smallCover;
    }

    public String getBigCover() {
        return bigCover;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCountTracks() {
        return countTracks;
    }

    public int getCountAlbums() {
        return countAlbums;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    @Nullable
    public static Artist fromJsonObject(JSONObject jsonObject) {
        Artist artist = new Artist();
        // Deserialize json into object fields
        try {
            artist.id = jsonObject.getInt("id");
            artist.name = jsonObject.getString("name");
            JSONArray jsonArrayGenres = jsonObject.getJSONArray("genres");
            if (jsonArrayGenres.length() == 0) {
                artist.genres = "NONE";
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < jsonArrayGenres.length() - 1; i++) {
                    sb.append(jsonArrayGenres.getString(i));
                    sb.append(", ");
                }
                sb.append(jsonArrayGenres.getString(jsonArrayGenres.length() - 1));
                artist.genres = sb.toString();
            }
            artist.countTracks = jsonObject.getInt("tracks");
            artist.countAlbums = jsonObject.getInt("albums");
            artist.link = jsonObject.getString("link");
            artist.description = jsonObject.getString("description");
            artist.smallCover = jsonObject.getJSONObject("cover").getString("small");
            artist.bigCover = jsonObject.getJSONObject("cover").getString("big");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return artist;
    }

    public static List<Artist> fromJsonArray(JSONArray jsonArray) {
        JSONObject artistJson;
        ArrayList<Artist> artists = new ArrayList<>(jsonArray.length());
        // Process each result in json array, decode and convert to business object
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                artistJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Artist artist = Artist.fromJsonObject(artistJson);
            if (artist != null) {
                artists.add(artist);
            }
        }
        return artists;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeString(this.genres);
        dest.writeValue(this.countTracks);
        dest.writeValue(this.countAlbums);
        dest.writeString(this.link);
        dest.writeString(this.description);
        dest.writeString(this.smallCover);
        dest.writeString(this.bigCover);
    }

    protected Artist(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.genres = in.readString();
        this.countTracks = (Integer) in.readValue(Integer.class.getClassLoader());
        this.countAlbums = (Integer) in.readValue(Integer.class.getClassLoader());
        this.link = in.readString();
        this.description = in.readString();
        this.smallCover = in.readString();
        this.bigCover = in.readString();
    }

    public static final Parcelable.Creator<Artist> CREATOR = new Parcelable.Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel source) {
            return new Artist(source);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };
}
