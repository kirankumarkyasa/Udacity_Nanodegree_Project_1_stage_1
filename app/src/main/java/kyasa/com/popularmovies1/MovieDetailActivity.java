package kyasa.com.popularmovies1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import kyasa.com.popularmovies1.APIManager.RetroManager;
import kyasa.com.popularmovies1.Model.Movie;
import kyasa.com.popularmovies1.WebServices.MovieService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kiran on 12/4/16.
 */
public class MovieDetailActivity extends AppCompatActivity{

    Movie mMovieDetails;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        final TextView titleTv,plotTv,ratingTv,releaseDateTv;
        final ImageView posterIv;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        titleTv = (TextView) findViewById(R.id.original_title_tv);
        plotTv = (TextView) findViewById(R.id.plot_tv);
        ratingTv = (TextView) findViewById(R.id.rating_tv);
        releaseDateTv = (TextView) findViewById(R.id.release_date_tv);
        posterIv = (ImageView) findViewById(R.id.movie_poster_iv);

        Call<Movie> moviedetails =  new RetroManager().getRetrofit()
                .create(MovieService.class)
                .getMovieDetails(getIntent().getIntExtra("movie_id",10),RetroManager.API_KEY);
        moviedetails.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                mMovieDetails = response.body();
                titleTv.setText(mMovieDetails.getOriginalTitle());
                plotTv.setText(mMovieDetails.getOverview());
                ratingTv.setText(mMovieDetails.getVoteAverage()+"");
                releaseDateTv.setText(mMovieDetails.getReleaseDate());
                final String image_base_url = "http://image.tmdb.org/t/p/w342"+mMovieDetails.getPosterPath();
                Picasso.with(getApplicationContext()).load(image_base_url).into(posterIv);
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
