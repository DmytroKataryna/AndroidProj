package kat.android.com.movielist.rest;

import kat.android.com.movielist.rest.pojo.moviedetails.MovieDetails;
import kat.android.com.movielist.rest.pojo.MovieResponse;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

//All Http requests
public interface API {

    public static final String API_KEY = "?api_key=f82b824321e6aa9a03e8f4d037f40955";

    //HTTP GET Json from http://api.themoviedb.org/3/movie/popular?api_key=f82b824321e6aa9a03e8f4d037f40955
    @GET("/movie/popular" + API_KEY)
    void getPopularMovies(@Query("page") int page, Callback<MovieResponse> callback);

    @GET("/movie/upcoming" + API_KEY)
    void getUpcomingMovies(@Query("page") int page, Callback<MovieResponse> callback);

    @GET("/movie/top_rated" + API_KEY)
    void getTopRatedMovies(@Query("page") int page, Callback<MovieResponse> callback);

    //SEARCH GET  http://api.themoviedb.org/3/search/movie?api_key=XXXXXX&query=MOVIE_NAME
    @GET("/search/movie" + API_KEY)
    void searchMovies(@Query("query") String title, Callback<MovieResponse> callback);

    @GET("/movie/{id}" + API_KEY)
    void getMovie(@Path("id") int id, Callback<MovieDetails> callback);
}
