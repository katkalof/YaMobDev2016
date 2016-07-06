package ru.katkalov.android.yamobdev2016.ui.recyclerview;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ru.katkalov.android.yamobdev2016.R;
import ru.katkalov.android.yamobdev2016.model.Artist;

public class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.ViewHolder> {

    public static final int ITEM_VIEW_TYPE = 1;
    public static final int PROGRESS_VIEW_TYPE = 2;
    public static final int ERROR_VIEW_TYPE = 3;

    private Picasso mImageLoader;

    protected boolean mIsLoading = false;

    // Store a member variable for the contacts
    private List<Artist> mArtists;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    // Define listener member variable
    private static OnItemClickListener errorListener;
    private static OnItemClickListener itemListener;
    // Define the listener interface


    // Pass in the contact array into the constructor
    public ArtistsAdapter(List<Artist> artists) {
        mArtists = artists;
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        itemListener = listener;
    }

    public void setOnErrorClickListener(OnItemClickListener listener) {
        errorListener = listener;
    }

    public void setImageLoader(Picasso imageLoader) {
        this.mImageLoader = imageLoader;
    }

    @Override
    public int getItemCount() {
//        return !(mIsLoading || mHasError) ? mArtists.size() : (mArtists.size() + 1);
        return !(mIsLoading) ? mArtists.size() : (mArtists.size() + 1);
    }

    public void setIsLoading(boolean loading) {
        if (mIsLoading == loading) {
            return;
        }

        mIsLoading = loading;

        if (mIsLoading) {
//            if (mHasError) {
//                mHasError = false;
//                notifyItemRemoved( mItems.size() );
//            }
            notifyItemInserted(mArtists.size());
        } else {
            notifyItemRemoved(mArtists.size());
        }
    }

    // Adds new items to the collection
    public void addItems(List<Artist> items) {
        int position = mArtists.size();
        mArtists.addAll(items);
        notifyItemRangeInserted(position, items.size());
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (holder instanceof ItemHolder) {

            //Фото
            String photo = mArtists.get(position).getSmallCover();
            this.mImageLoader.load(photo)
                    .placeholder(R.drawable.ic_music_note_black_48dp)
                    .fit().centerCrop()
                    .into(((ItemHolder) holder).photoView);
            //Имя
            ((ItemHolder) holder).titleView.setText(mArtists.get(position).getName());
            //Жанры
            ((ItemHolder) holder).subTitleView.setText(mArtists.get(position).getGenres().toString());
            //Песни, Альбомы
            ((ItemHolder) holder).statView.setText(new StringBuilder()
                    .append(((ItemHolder) holder).itemView.getResources()
                            .getQuantityString(R.plurals.numberOfSongs,
                                    mArtists.get(position).getCountTracks(),
                                    mArtists.get(position).getCountTracks()))
                    .append(", ")
                    .append(((ItemHolder) holder).itemView.getResources()
                            .getQuantityString(R.plurals.numberOfAlbums,
                                    mArtists.get(position).getCountAlbums(),
                                    mArtists.get(position).getCountAlbums()))
            );
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = null;
        View view;

        switch (viewType) {
            case ITEM_VIEW_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist_list, parent, false);
                holder = new ItemHolder(view);
                break;

            case PROGRESS_VIEW_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_spinner, parent, false);
                holder = new ViewHolder(view);
                break;

            case ERROR_VIEW_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_error, parent, false);
                holder = new ErrorHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
//        if (mHasError && mArtists.size() == position) {
//            return ERROR_VIEW_TYPE;
//        }
        return mIsLoading && mArtists.size() == position ? PROGRESS_VIEW_TYPE : ITEM_VIEW_TYPE;
    }

    public Artist getItem(int position) {
        if (position >= 0 && position < mArtists.size()) {
            return mArtists.get(position);
        }
        return null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class ItemHolder extends ArtistsAdapter.ViewHolder {
        public ImageView photoView;
        public TextView titleView;
        public TextView subTitleView;
        public TextView statView;

        public ItemHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (itemListener != null)
                        itemListener.onItemClick(itemView, getLayoutPosition());
                }
            });
            photoView = (ImageView) itemView.findViewById(R.id.item_image);
            titleView = (TextView) itemView.findViewById(R.id.item_title);
            subTitleView = (TextView) itemView.findViewById(R.id.item_subtitle);
            statView = (TextView) itemView.findViewById(R.id.item_stat);
        }
    }

    public static class ErrorHolder extends ArtistsAdapter.ViewHolder {

        public ErrorHolder(final View itemView) {
            super(itemView);
            View loadMoreBtn = itemView.findViewById(R.id.load_more);
            loadMoreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (errorListener != null)
                        errorListener.onItemClick(itemView, getLayoutPosition());
                }
            });
        }
    }
}
