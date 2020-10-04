package com.example.moviebrowser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moviebrowser.R;
import com.example.moviebrowser.model.MoviesModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<MoviesModel> moviesResult;
    private static final int LOADING = 0;
    private static final int ITEM = 1;
    private boolean isLoadingAdded = false;

    public MoviesAdapter(Context context, List<MoviesModel> moviesResult) {
        this.context = context;
        this.moviesResult = moviesResult;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.row_layout, parent, false);
                vh = new MovieViewHolder(viewItem);
                break;
          /*  case LOADING:
                View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                vh = new LoadingViewHolder(viewLoading);
                break;*/
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MoviesModel movie = moviesResult.get(position);
        switch (getItemViewType(position)) {
            case ITEM:
                MovieViewHolder movieViewHolder = (MovieViewHolder) holder;
              //  movieViewHolder.movieTitle.setText(movie.getTitle());
               // Glide.with(context).load(movie.getImageUrl()).apply(RequestOptions.centerCropTransform()).into(movieViewHolder.movieImage);
                break;

            case LOADING:
                //LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                //loadingViewHolder.progressBar.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return moviesResult == null ? 0 : moviesResult.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == moviesResult.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new MoviesModel());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = moviesResult.size() - 1;
        MoviesModel result = getItem(position);

        if (result != null) {
            moviesResult.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void add(MoviesModel movie) {
        moviesResult.add(movie);
        notifyItemInserted(moviesResult.size() - 1);
    }

    public void addAll(List<MoviesModel> moveResults) {
        for (MoviesModel result : moveResults) {
            add(result);
        }
    }

    public MoviesModel getItem(int position) {
        return moviesResult.get(position);
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        private TextView movie_title;
        private ImageView movie_poster;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            movie_title = itemView.findViewById(R.id.movie_title);
            movie_poster = itemView.findViewById(R.id.movie_poster);
        }
    }

}
