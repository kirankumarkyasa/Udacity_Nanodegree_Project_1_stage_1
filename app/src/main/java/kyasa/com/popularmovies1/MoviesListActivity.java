package kyasa.com.popularmovies1;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kyasa.com.popularmovies1.APIManager.RetroManager;
import kyasa.com.popularmovies1.Adapters.MoviesListAdapter;
import kyasa.com.popularmovies1.Interface.OnLoadMoreListener;
import kyasa.com.popularmovies1.Model.Movie;
import kyasa.com.popularmovies1.Model.MoviesResult;
import kyasa.com.popularmovies1.Utils.Utils;
import kyasa.com.popularmovies1.WebServices.MovieService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesListActivity extends AppCompatActivity {

    RecyclerView mMoviesListRv;
    ArrayList<Movie> mMoviesList = new ArrayList<>();
    MoviesListAdapter moviesListAdapter;
    boolean isLoading;
    private int pageNumber=1;
    private GridLayoutManager gridLayoutManager;
    private boolean isPopular = true;
    private Context mContext;
    private TextView mNoInternetTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);
        mContext = MoviesListActivity.this;
        getSupportActionBar().setTitle(R.string.popular_movies_title);
        mMoviesListRv = (RecyclerView) findViewById(R.id.movies_list_rv);
        mMoviesListRv.setVisibility(View.GONE);
        mNoInternetTv = (TextView) findViewById(R.id.no_internet_tv);
        gridLayoutManager = new GridLayoutManager(MoviesListActivity.this,2);
        mMoviesListRv.setLayoutManager(gridLayoutManager);
        mMoviesListRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleThreshold = 5;
                int totalItemCount = gridLayoutManager.getItemCount();
                int lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        pageNumber++;
                        onLoadMoreListener.onLoadMore(pageNumber);
                    }
                    isLoading = true;
                }
            }
        });
        if(isPopular){
            getSupportActionBar().setTitle(R.string.popular_movies_title);

        } else {
            getSupportActionBar().setTitle(R.string.top_rated_title);
        }
        moviesListAdapter=new MoviesListAdapter(MoviesListActivity.this,
                new MoviesListAdapter.OnItemclickListener() {
                    @Override
                    public void onItemClick(int movieId) {
                        Intent i = new Intent(MoviesListActivity.this,MovieDetailActivity.class);
                        i.putExtra("movie_id",movieId);
                        startActivity(i);
                    }
                });
        mMoviesListRv.setAdapter(moviesListAdapter);
        if(Utils.isNetworkAvailable(MoviesListActivity.this)){
            getMoviesData(pageNumber);
        } else {
            Toast.makeText(mContext,"No Internet Connectivity",Toast.LENGTH_SHORT).show();
            mMoviesListRv.setVisibility(View.GONE);
            mNoInternetTv.setVisibility(View.VISIBLE);
        }
    }

    OnLoadMoreListener onLoadMoreListener = new OnLoadMoreListener() {
        @Override
        public void onLoadMore(int pageNumber) {
           new Handler().post(new Runnable() {
                @Override
                public void run() {
                    if (mMoviesList.size() > 0) {
                        mMoviesList.add(null);
                        moviesListAdapter.notifyItemInserted(mMoviesList.size() - 1);
                    }
                }
            });
            if(Utils.isNetworkAvailable(MoviesListActivity.this)){
                getMoviesData(pageNumber);
            } else {
                Toast.makeText(mContext,"No Internet Connectivity",Toast.LENGTH_SHORT).show();
                mMoviesListRv.setVisibility(View.GONE);
                mNoInternetTv.setVisibility(View.VISIBLE);
            }

        }
    };

    public void getMoviesData(int page) {
        mNoInternetTv.setVisibility(View.GONE);
        mMoviesListRv.setVisibility(View.VISIBLE);
        String sort= "popular";
        if(isPopular) {
            sort= "popular";
        } else {
            sort = "top_rated";
        }
        final Call<MoviesResult> moviesList =  new RetroManager().getRetrofit().create(MovieService.class)
                .getMovies(sort,RetroManager.API_KEY,page);
        Utils.showProgressDialog(this);
        moviesList.enqueue(new Callback<MoviesResult>() {
            @Override
            public void onResponse(Call<MoviesResult> call, Response<MoviesResult> response) {
                mMoviesListRv.setVisibility(View.VISIBLE);
                MoviesResult mr = response.body();
                if (mMoviesList.size() > 0) {
                    mMoviesList.remove(mMoviesList.size() - 1);
                    moviesListAdapter.notifyItemRemoved(mMoviesList.size());
                }
                mMoviesList.addAll(mr.getResults());
                moviesListAdapter.setmMoviesList(mMoviesList);
                moviesListAdapter.notifyDataSetChanged();
                isLoading = false;
                Utils.disMissProgressDialog();
            }

            @Override
            public void onFailure(Call<MoviesResult> call, Throwable t) {
                mMoviesListRv.setVisibility(View.GONE);
                mNoInternetTv.setVisibility(View.VISIBLE);
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_toggle:
                if(Utils.isNetworkAvailable(MoviesListActivity.this)){
                    if(isPopular) {
                        item.setTitle(getString(R.string.sort_by_popular));
                        getSupportActionBar().setTitle(R.string.top_rated_title);
                        isPopular = false;
                    } else {
                        item.setTitle(getString(R.string.sort_by_top_rated));
                        getSupportActionBar().setTitle(R.string.popular_movies_title);
                        isPopular = true;
                    }
                    getMoviesData(1);
                    mMoviesList.clear();
                } else {
                    Toast.makeText(mContext,"No Internet Connectivity",Toast.LENGTH_SHORT).show();
                    mMoviesListRv.setVisibility(View.GONE);
                    mNoInternetTv.setVisibility(View.VISIBLE);
                }
                return true;
            default:
                return false;
        }
    }
}
