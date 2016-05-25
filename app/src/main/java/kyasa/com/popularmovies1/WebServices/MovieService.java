package kyasa.com.popularmovies1.WebServices;

import java.util.List;

import kyasa.com.popularmovies1.Model.Movie;
import kyasa.com.popularmovies1.Model.MoviesResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by kiran on 12/4/16.
 */
public interface MovieService {
    @GET("movie/{sort}")
    Call<MoviesResult> getMovies(@Path("sort") String sort,@Query("api_key") String apiKey, @Query("page") int pageNumber);

    @GET("movie/{id}")
    Call<Movie> getMovieDetails(@Path("id") int id,@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<List<Movie>> getHighestRatedMovies(@Query("api_key") String apiKey);
}
