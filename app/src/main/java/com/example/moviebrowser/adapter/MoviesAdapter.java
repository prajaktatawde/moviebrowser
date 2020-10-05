package com.example.moviebrowser.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.moviebrowser.R;
import com.example.moviebrowser.constants.Constants;
import com.example.moviebrowser.model.MoviesModel;
import com.example.moviebrowser.view.MoviesDetailActivity;

import java.io.Serializable;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    Context context;
    List<MoviesModel.Result> moviesResult;

    public MoviesAdapter(Context context, List<MoviesModel.Result> moviesResult) {
        this.context = context;
        this.moviesResult = moviesResult;
    }

    @NonNull
    @Override
    public MoviesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapter.ViewHolder holder, int position) {
        final MoviesModel.Result movies_data = moviesResult.get(position);
        if (movies_data.getOriginalTitle() != null && !movies_data.getOriginalTitle().isEmpty() && !movies_data.getOriginalTitle().equals("")) {
            holder.text_title.setVisibility(View.VISIBLE);
            holder.text_title.setText(movies_data.getOriginalTitle());
        } else {
            holder.text_title.setVisibility(View.GONE);
        }

        if (movies_data.getPosterPath() != null && !movies_data.getPosterPath().isEmpty() && !movies_data.getPosterPath().equals("")) {
            holder.iv_Poster.setVisibility(View.VISIBLE);
            String poster_path = Constants.POSTER_PATH + movies_data.getPosterPath();
            Glide.with(context)
                    .load(poster_path)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(holder.iv_Poster);
        } else {
            holder.iv_Poster.setVisibility(View.GONE);
        }

        holder.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MoviesDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", movies_data);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesResult.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_Poster;
        TextView text_title;
        CardView main;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            text_title = itemView.findViewById(R.id.text_title);
            iv_Poster = itemView.findViewById(R.id.iv_Poster);
            text_title.setSelected(true);
            main = itemView.findViewById(R.id.main);
        }
    }
}
