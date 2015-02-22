package kat.android.com.movielist.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import kat.android.com.movielist.DetailActivity;
import kat.android.com.movielist.R;
import kat.android.com.movielist.rest.RestClient;
import kat.android.com.movielist.rest.pojo.moviedetails.MovieDetails;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

//Detailed profile information
public class MovieDetailsFragment extends Fragment {

    private int id;
    private ImageView mImage;
    private MovieDetails data;
    private TextView mTitle, mGenres, mRelease, mRuntime, mBudget;
    private TextView mAvgRate, mCount, mDescription, mHomePage;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get user Id from bundle
        id = getArguments().getInt(DetailActivity.ID_KEY);
        loadMovieInformation();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.details_movie_layout, container, false);

        mImage = (ImageView) v.findViewById(R.id.posterImageView);
        mTitle = (TextView) v.findViewById(R.id.titleTextView);
        mGenres = (TextView) v.findViewById(R.id.genresTxtView);
        mRelease = (TextView) v.findViewById(R.id.releaseTextView);
        mRuntime = (TextView) v.findViewById(R.id.runtimeTextView);
        mBudget = (TextView) v.findViewById(R.id.budgetTextView);
        mAvgRate = (TextView) v.findViewById(R.id.detailsAvgView);
        mCount = (TextView) v.findViewById(R.id.countView);
        mDescription = (TextView) v.findViewById(R.id.detailsDescriptionView);
        mHomePage = (TextView) v.findViewById(R.id.detailsHomePageView);

        return v;
    }

    private void loadMovieInformation() {

        RestClient.get().getMovie(id, new Callback<MovieDetails>() {
            @Override
            public void success(MovieDetails movieDetails, Response response) {
                data = movieDetails;
                Picasso.with(getActivity()).load("https://image.tmdb.org/t/p/w185" + data.getPoster_path()).into(mImage);
                mTitle.setText(data.getTitle());
                mGenres.setText(getGenres());
                mRelease.setText(data.getRelease_date());
                mRuntime.setText(data.getRuntime() + " min");
                mBudget.setText(data.getBudget() + "$");
                mAvgRate.setText(data.getVote_average() + "");
                mCount.setText(data.getVote_count() + "");
                mDescription.setText(data.getOverview());
                //Some movies doesn't have homepage , so if it have i add this text ( also coloring some part)
                if (data.getHomepage() != null && data.getHomepage().length() != 0) {
                    mHomePage.setText(Html.fromHtml("<font color=#FB8C00>Homepage :</font>" + (" <br/>") + data.getHomepage()));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(DetailActivity.TAG, "An error occurred while downloading movie info.");
            }
        });
    }

    //Movie may contains a lot of genres. I get just three of them
    private String getGenres() {
        StringBuilder builder = new StringBuilder();
        int n = data.getGenres().size();
        if (n == 0) return "";
        else if (n > 3) n = 3;
        for (int i = 0; i < n; i++) {
            builder.append(data.getGenres().get(i).getName()).append(" , ");
        }
        builder.deleteCharAt(builder.lastIndexOf(", "));
        return builder.toString();
    }
}
