package kyasa.com.popularmovies1.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kyasa.com.popularmovies1.Model.Movie;
import kyasa.com.popularmovies1.R;

/**
 * Created by kiran on 12/4/16.
 */
public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.MovieItemViewHolder> {

    Context mContext;

    public OnItemclickListener getmListener() {
        return mListener;
    }

    public void setmListener(OnItemclickListener mListener) {
        this.mListener = mListener;
    }

    OnItemclickListener mListener;
    ArrayList<Movie> mMoviesList;

    public MoviesListAdapter(Context context,OnItemclickListener listener) {
        mContext =context;
        mListener = listener;
    }

    @Override
    public MovieItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_movies, parent, false);

        return new MovieItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MovieItemViewHolder holder, int position) {
        Movie mMovie = mMoviesList.get(position);
        holder.bind(mMovie,mListener);
    }

    @Override
    public int getItemCount() {
        if(mMoviesList != null){
            return mMoviesList.size();
        } else {
            return 0;
        }
    }

    public class MovieItemViewHolder extends RecyclerView.ViewHolder {
        ImageView movie_poster_iv;
        TextView movie_title_tv;
        public MovieItemViewHolder(View itemView) {
            super(itemView);
            movie_poster_iv = (ImageView) itemView.findViewById(R.id.movie_poster_iv);
            movie_title_tv = (TextView) itemView.findViewById(R.id.movie_title_tv);
        }

        public void bind(final Movie item, final OnItemclickListener listener) {
            if(item != null){
                final String image_base_url = "http://image.tmdb.org/t/p/w185"+item.getPosterPath();
                movie_title_tv.setText(item.getTitle());
                Picasso.with(mContext).
                        load(image_base_url)
                        .into(movie_poster_iv);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(item.getId());
                    }
                });
            }
        }
    }

    public interface OnItemclickListener {
        void onItemClick(int movieId);
    }

    public ArrayList<Movie> getmMoviesList() {
        return mMoviesList;
    }

    public void setmMoviesList(ArrayList<Movie> mMoviesList) {
        this.mMoviesList = mMoviesList;
    }
}
