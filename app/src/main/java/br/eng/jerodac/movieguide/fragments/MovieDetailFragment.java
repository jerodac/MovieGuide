package br.eng.jerodac.movieguide.fragments;

import android.annotation.TargetApi;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.text.TextUtils;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.eng.jerodac.movieguide.R;
import br.eng.jerodac.movieguide.business.AppLog;
import br.eng.jerodac.movieguide.business.MovieFavorites;
import br.eng.jerodac.movieguide.business.RestError;
import br.eng.jerodac.movieguide.database.MovieContract;
import br.eng.jerodac.movieguide.interfaces.ReviewListener;
import br.eng.jerodac.movieguide.interfaces.VideoListener;
import br.eng.jerodac.movieguide.vo.Movie;
import br.eng.jerodac.movieguide.vo.Review;
import br.eng.jerodac.movieguide.vo.ReviewResponse;
import br.eng.jerodac.movieguide.vo.Video;
import br.eng.jerodac.movieguide.vo.VideoResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MovieDetailFragment extends BaseFragment
        implements ReviewListener, VideoListener {

    static final String TAG = MovieDetailFragment.class.getSimpleName();

    public static final String KEY_MOVIE = "movie";

    public Movie mMovie;

    private MovieDetailsAdapter mAdapter;

    private ShareActionProvider mShareActionProvider;

    private MenuItem mShareMenuItem;

    @BindView(R.id.recycler)
    protected RecyclerView mRecyclerView;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_movie_detail;
    }

    public static MovieDetailFragment newInstance(Movie movie) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_MOVIE, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initComponents(View rootView) {
        // Create adapter
        mAdapter = new MovieDetailsAdapter();

        // Request reviews and trailers
        if (mMovie != null) {
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
            getController().requestMovieReviews(mMovie.getId(), this);
            getController().requestMovieVideos(mMovie.getId(), this);
            queryGenres();
        }

        setupTransition();

        // show options menu
        setHasOptionsMenu(true);
    }

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMovie = getArguments().getParcelable(KEY_MOVIE);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupTransition() {
        //setEnterTransition(new Explode());
        setSharedElementEnterTransition(new Slide());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_movie_detail_frag, menu);
        mShareMenuItem = menu.findItem(R.id.menu_item_share);
        mShareMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                shareVideoUrl();
                return true;
            }
        });
    }

    @Override
    public void success(ReviewResponse response) {
        List<Review> reviews = response.getReviews();
        AppLog.v(AppLog.TAG, "Reviews: "+ reviews.size());
        mAdapter.setReviews(reviews);
    }

    @Override
    public void success(VideoResponse response) {
        List<Video> trailers = response.getYoutubeTrailers();
        AppLog.v(AppLog.TAG, "Trailers: " + trailers.size());
        mAdapter.setTrailers(trailers);
    }

    @Override
    public void error(RestError error) {
        AppLog.e(AppLog.TAG, "Error retrieving data from API: " + error.getException().getMessage());
    }

    private void shareVideoUrl() {

        Uri url = mAdapter.getFirstTrailerUri();
        if (url != null) {
            Intent shareIntent = new Intent();
            shareIntent.setType("text/plain");
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.share_action_subject_prefix) + mMovie.getTitle());
            shareIntent.putExtra(Intent.EXTRA_TEXT, url.toString());
            startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.title_share_action)));
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.message_no_trailer) + mMovie.getTitle(), Toast.LENGTH_LONG).show();
        }
    }

    // query local database for genre ids in offline mode
    private void queryGenres() {

        if (mMovie.getGenreIds().length == 0 && MovieFavorites.isFavoriteMovie(getActivity(), mMovie.getId())) {

            GenresQueryHandler handler = new GenresQueryHandler(getActivity().getContentResolver());

            handler.startQuery(5, null, MovieContract.MovieGenreEntry.CONTENT_URI,
                  new String[]{"*"},
                  MovieContract.MovieGenreEntry.WHERE_MOVIE_ID,
                  new String[]{""+mMovie.getId()},
                  null
            );
        }
    }

    /**
     * Adapter for recycler view has 3 view types to display movie information. The first item is
     * always the header. It is followed by any trailers then any reviews.
     *
     *   1. Header  - details about the movie (poster, rating, genres, release, synopsis, etc)
     *   2. Trailer - video trailers on YouTube
     *   3. Review  - user review
     */
    public class MovieDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        static final int HEADER_VIEW_TYPE = 1;
        static final int TRAILER_VIEW_TYPE = 2;
        static final int REVIEW_VIEW_TYPE = 3;

        List<Review> reviews = new ArrayList<>();
        List<Video> trailers = new ArrayList<>();

        public void setReviews(List<Review> reviews) {
            this.reviews.clear();
            this.reviews.addAll(reviews);
            this.notifyDataSetChanged();
        }

        public void setTrailers(List<Video> trailers) {
            this.trailers.clear();
            this.trailers.addAll(trailers);
            this.notifyDataSetChanged();
        }

        public void updateGenres() {
            this.notifyDataSetChanged();
        }

        public Uri getFirstTrailerUri() {
            return !trailers.isEmpty() ? trailers.get(0).getYoutubUrl() : null;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder holder = null;
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            switch (viewType) {
                case HEADER_VIEW_TYPE:
                    View headerView = inflater.inflate(R.layout.detail_header, parent, false);
                    holder = new MovieHeaderViewHolder(headerView);
                    break;
                case TRAILER_VIEW_TYPE:
                    View trailerView = inflater.inflate(R.layout.video_item, parent, false);
                    holder = new MovieTrailerViewHolder(trailerView);
                    break;
                case REVIEW_VIEW_TYPE:
                    View reviewView = inflater.inflate(R.layout.review_item, parent, false);
                    holder = new MovieReviewViewHolder(reviewView);
                    break;
            }

            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            switch (holder.getItemViewType()) {
                case HEADER_VIEW_TYPE:
                    MovieHeaderViewHolder headerViewHolder = (MovieHeaderViewHolder) holder;
                    int screenWidth = getResources().getDisplayMetrics().widthPixels;
                    Picasso.with(headerViewHolder.posterView.getContext())
                          .load(mMovie.getPosterUrl(screenWidth))
                          .placeholder(R.drawable.ic_local_movies_white_36dp)
                          .into(headerViewHolder.posterView);
                    headerViewHolder.ratingView.setText("" + mMovie.getVoteAverage());
                    headerViewHolder.releaseView.setText(mMovie.getReleaseDate());
                    headerViewHolder.synopsisView.setText(mMovie.getOverview());
                    headerViewHolder.genres.setText(TextUtils.join(", ", mMovie.getMovieGenres()).toUpperCase());
                    break;
                case TRAILER_VIEW_TYPE:
                    Video trailer = trailers.get(position - 1);
                    MovieTrailerViewHolder trailerViewHolder = (MovieTrailerViewHolder) holder;
                    trailerViewHolder.videoTitle.setText(trailer.getName());
                    break;
                case REVIEW_VIEW_TYPE:
                    Review review = reviews.get(position - trailers.size() - 1);
                    MovieReviewViewHolder reviewViewHolder = (MovieReviewViewHolder) holder;
                    reviewViewHolder.author.setText(review.getAuthor());
                    reviewViewHolder.content.setText("\"" + review.getContent() + "\"");
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return 1 + reviews.size() + trailers.size();
        }

        @Override
        public int getItemViewType(int position) {
            int viewType;

            if (mMovie == null)
                return -1;

            if (position == 0) {
                viewType = HEADER_VIEW_TYPE;
            } else if (position < trailers.size() + 1) {
                viewType =  TRAILER_VIEW_TYPE;
            } else {
                viewType =  REVIEW_VIEW_TYPE;
            }

            //Log.d(TAG, "getItemViewType - position:" + position + " type: " + viewType);
            return viewType;
        }

        class MovieHeaderViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.movie_detail_poster)
            ImageView posterView;

            @BindView(R.id.movie_detail_rating)
            TextView ratingView;

            @BindView(R.id.movie_detail_release)
            TextView releaseView;

            @BindView(R.id.movie_detail_synopsis)
            TextView synopsisView;

            @BindView(R.id.movie_genres)
            TextView genres;

            public MovieHeaderViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        class MovieTrailerViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.video_name)
            TextView videoTitle;

            @BindView(R.id.view_play_button)
            ImageButton playButton;

            public MovieTrailerViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            @OnClick({R.id.view_play_button, R.id.video_name})
            public void playTrailer(View v) {
                Uri url = trailers.get(getAdapterPosition() - 1).getYoutubUrl();
                Log.d(TAG, "Play url: " + url);
                startActivity(new Intent(Intent.ACTION_VIEW, url));
            }
        }


        class MovieReviewViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.review_author)
            TextView author;

            @BindView(R.id.review_content)
            TextView content;

            public MovieReviewViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            @OnClick(R.id.review_content)
            public void openReview() {
                Review review = reviews.get(getAdapterPosition() - trailers.size() - 1);
                Log.d(TAG, "Display complete review: " + mMovie.getTitle());
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(review.getUrl())));
            }
        }
    }

    class GenresQueryHandler extends AsyncQueryHandler {

        public GenresQueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            Log.d(TAG, "Genres query returned cursor: " + cursor.getCount());
            int[] genreIds = new int[cursor.getCount()];

            int i = 0;
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieGenreEntry.COLUMN_GENRE_ID));
                    genreIds[i++] = id;
                }
                cursor.close();
                mMovie.setGenreIds(genreIds);
                mAdapter.updateGenres();
            }
        }

    }

    @Override
    protected void settings(View rootView) {

    }
}
