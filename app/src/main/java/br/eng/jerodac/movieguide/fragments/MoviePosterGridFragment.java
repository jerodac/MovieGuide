package br.eng.jerodac.movieguide.fragments;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.eng.jerodac.movieguide.R;
import br.eng.jerodac.movieguide.adapters.SortSpinnerAdapter;
import br.eng.jerodac.movieguide.business.AppLog;
import br.eng.jerodac.movieguide.business.AppPreferences;
import br.eng.jerodac.movieguide.business.MovieFavorites;
import br.eng.jerodac.movieguide.business.RestError;
import br.eng.jerodac.movieguide.components.SortOption;
import br.eng.jerodac.movieguide.database.MovieContract;
import br.eng.jerodac.movieguide.interfaces.MovieListListener;
import br.eng.jerodac.movieguide.interfaces.MovieListener;
import br.eng.jerodac.movieguide.vo.Movie;
import br.eng.jerodac.movieguide.vo.MovieListResponse;
import br.eng.jerodac.movieguide.vo.MovieResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jean Rodrigo Dalbon Cunha on 06/10/2017.
 */
public class MoviePosterGridFragment extends BaseFragment
            implements MovieListListener, MovieListener{
    static final String TAG = MoviePosterGridFragment.class.getSimpleName();

    static final String KEY_MOVIES = "movies";

    @BindView(R.id.recycler_container)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.loading)
    protected View mLoading;

    private Spinner mSortSpinner;

    private MovieGridRecyclerAdapter mAdapter;
    private RecyclerScrollListener mScrollListener;

    int mSortMethod = SortOption.POPULARITY;

    // number of pages available for scrolling
    int mPageMax = 25;

    // number of items per page
    int mPageSize = 20;

    // communicates selection events back to listener
    OnMovieSelectedListener mListener;

    // interface to communicate movie selection events to MainActivity
    public interface OnMovieSelectedListener {
        public void onMovieSelected(Movie selection, boolean onClick, View view);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.movie_poster_grid;
    }

    @Override
    protected void initComponents(View rootView) {
        mAdapter = new MovieGridRecyclerAdapter();
        mScrollListener = new RecyclerScrollListener();
        
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setData(movies);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this.getActivity(), 3));
        mRecyclerView.addOnScrollListener(mScrollListener);

        // request movies
        if (mAdapter.getItemCount() == 0) {
            if (mSortMethod == SortOption.POPULARITY) {
                getController().requestMostPopularMovies(this);
            } else {
                getController().requestHighestRatedMovies(this);
            }
        }

        // check for network connection
        checkNetwork();

    }

    ArrayList<Movie> movies = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // restore movie list from instance state on orientation change
        if (savedInstanceState != null) {
            mSortMethod = AppPreferences.getCurrentSortMethod(getActivity());
            movies = savedInstanceState.getParcelableArrayList(KEY_MOVIES);
        } else {
            mSortMethod = AppPreferences.getPreferredSortMethod(getActivity());
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(KEY_MOVIES, mAdapter.data);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_poster_grid, menu);

        MenuItem menuItem = menu.findItem(R.id.spin_test);

        // specify layout for the action
        menuItem.setActionView(R.layout.sort_spinner);
        View view = menuItem.getActionView();

        // set custom adapter on spinner
        mSortSpinner = (Spinner) view.findViewById(R.id.spinner_nav);
        mSortSpinner.setAdapter(new SortSpinnerAdapter(this, getActivity(), SortOption.getSortOptions()));
        mSortSpinner.setSelection(mSortMethod);
        mSortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AppPreferences.setCurrentSortMethod(getActivity(), position);
                handleSortSelection(SortOption.getSortMethod(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnMovieSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnMovieSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }


    @Override
    public void success(MovieListResponse response) {

        if (response.getPage() == 1) {
            // initialize with results for first page
            int pageMax = (response.getTotalPages() < mPageMax) ? response.getTotalPages() : mPageMax;
            mScrollListener.totalPages = pageMax;
            mAdapter.setData(response.getMovies(), mPageSize, pageMax);
        } else {
            // append results for subsequent pages
            mAdapter.appendData(response.getMovies());
        }
        mLoading.setVisibility(View.GONE);
    }

    @Override
    public void success(MovieResponse response) {
        mAdapter.appendData(response.getMovie());
        mLoading.setVisibility(View.GONE);
    }

    @Override
    public void error(RestError error) {
        if (error.isConnectionError()) {
            snackBarUtil.showMessageSuccess("Unable to connect to remote host");
        }
    }

    public void handleSortSelection(int sortType) {
        if (mSortMethod == sortType)
            return;

        mSortMethod = sortType;
        mScrollListener.init();
        mRecyclerView.scrollToPosition(0);

        switch (mSortMethod) {
            case SortOption.POPULARITY:
                getController().requestMostPopularMovies(this);
                return;
            case SortOption.RATING:
                getController().requestHighestRatedMovies(this);
                return;
            case SortOption.FAVORITE:
                queryFavorites();
                return;
            default:
                snackBarUtil.showMessageSuccess("Sort type not supported");
                return;
        }
    }

    public void removeMovie(Movie movie) {
        mAdapter.removeData(movie);
    }

    private void queryFavorites() {

        mListener.onMovieSelected(null, false, null);

        if (isNetworkAvailable()) {
            AppLog.v(AppLog.TAG, "Query favorites (online mode)");
            mAdapter.clearData();
            List<Integer> favoriteIds = MovieFavorites.getFavoriteMovies(getActivity());
            for (int favoriteId : favoriteIds) {
                getController().requestMovie(favoriteId, this);
            }
        } else {
            AppLog.v(AppLog.TAG, "Query favorites (offline mode)");
            mAdapter.clearData();
            FavoritesQueryHandler handler = new FavoritesQueryHandler(getActivity().getContentResolver());
            handler.startQuery(1, null, MovieContract.MovieEntry.CONTENT_URI,
                    new String[]{"*"},
                    MovieContract.MovieEntry.SELECT_FAVORITES,
                    null,
                    null
            );
        }
    }

    private void loadNextPage(int page) {
        AppLog.v(AppLog.TAG, "Pagination: "+page);

        switch (mSortMethod) {
            case SortOption.POPULARITY:
                getController().requestMostPopularMovies(page, this);
                return;
            case SortOption.RATING:
                getController().requestHighestRatedMovies(page, this);
                return;
            default:
                return;
        }

    }

    private boolean checkNetwork() {
        if (!isNetworkAvailable()) {
            snackBarUtil.showMessageSuccess("Network unavailable (check your connection)");
            return true;
        }
        return false;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    class MovieGridRecyclerAdapter extends RecyclerView.Adapter<MovieGridRecyclerAdapter.MovieGridItemViewHolder> {

        ArrayList<Movie> data = new ArrayList<>();

        int maxPages = -1;
        int pageSize = 20;

        public void setData(List<Movie> data, int pageSize, int maxPages) {
            this.maxPages = maxPages;
            this.pageSize = pageSize;
            setData(data);
        }

        public void setData(List<Movie> data) {
            this.data.clear();
            this.data.addAll(data);
            this.notifyDataSetChanged();
            notifyMovieSelectionListener();
        }

        public void appendData(List<Movie> movies) {
            this.data.addAll(movies);
            this.notifyDataSetChanged();
        }

        public void appendData(Movie movie) {
            this.data.add(movie);
            this.notifyItemChanged(this.data.size() - 1);
            if (this.data.size() == 1) {
                notifyMovieSelectionListener();
            }
        }

        public void removeData(Movie movie) {
            int index = this.data.indexOf(movie);
            if (index != -1)
                this.data.remove(index);

            this.notifyItemRemoved(index);
            notifyMovieSelectionListener();
        }

        public void clearData() {
            this.maxPages = -1;
            this.data.clear();
            this.notifyDataSetChanged();
        }

        public void notifyMovieSelectionListener() {
            if (mListener != null && !data.isEmpty()) {
                View view = mRecyclerView.getChildAt(0);
                AppLog.v(AppLog.TAG, "Found child view: " + view);
                View posterView = null;
                if (view != null) {
                    posterView = view.findViewById(R.id.movie_poster);
                    AppLog.v(AppLog.TAG, "Found poster view: " + posterView);
                }
                mListener.onMovieSelected(data.get(0), false, posterView);
            }
        }

        @Override
        public MovieGridItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.movie_poster_item, parent, false);
            return new MovieGridItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MovieGridItemViewHolder holder, int position) {

            // pending results
            if (position >= data.size()) {
                holder.movieTitle.setText("");
                holder.moviePoster.setImageResource(R.drawable.ic_image_white_36dp);
                return;
            }

            // display movie details
            Movie movie = data.get(position);
            holder.movieTitle.setText(movie.getTitle());
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            Picasso.with(holder.moviePoster.getContext())
                    .load(movie.getPosterUrl(screenWidth))
                    .placeholder(R.drawable.ic_local_movies_white_36dp)
                    .error(R.drawable.ic_local_movies_white_36dp)
                    .into(holder.moviePoster);
        }

        @Override
        public int getItemCount() {
            // returns the expected size when paging is enabled; this prevents an 'invalid view holder position'
            // exception thrown by validateViewHolderForOffsetPosition
            if (maxPages == -1) {
                return  data.size();
            } else {
                return maxPages * pageSize;
            }
        }

        class MovieGridItemViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.movie_title)
            TextView movieTitle;

            @BindView(R.id.movie_poster)
            ImageView moviePoster;

            public MovieGridItemViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            @OnClick(R.id.movie_poster)
            public void onClick() {
                int adapterPosition = this.getAdapterPosition();
                if (adapterPosition < data.size()) {
                    Movie movie = data.get(adapterPosition);
                    if (mListener != null) {
                        mListener.onMovieSelected(movie, true, moviePoster);
                    }
                }
            }
        }
    }

    class RecyclerScrollListener extends RecyclerView.OnScrollListener {
        int currentPage;
        int totalPages;
        int previousTotal;
        int visibleThreshold;
        boolean loading;

        public void init() {
            currentPage = 1;
            totalPages = 1;
            previousTotal = 0;
            visibleThreshold = 5;
            loading = false;
        }

        public RecyclerScrollListener() {
            super();
            init();
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            int visibleItemCount = recyclerView.getChildCount();
            //int totalItemCount = gridLayoutManager.getItemCount();
            int totalItemCount = mAdapter.data.size();
            int firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();

            // load finished
            if (loading && totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
                currentPage++;
            }

            // load more data when near end of scroll view (within threshold)
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                if (currentPage < totalPages) {
                    loadNextPage(currentPage + 1);
                    loading = true;
                }
            }
        }
    }

    class FavoritesQueryHandler extends AsyncQueryHandler {

        public FavoritesQueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            List<Movie> favorites = new ArrayList<Movie>();

            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Movie movie = Movie.createFromCursor(cursor);
                    favorites.add(movie);
                }
                cursor.close();
            }

            mAdapter.setData(favorites);
        }

    }

    @Override
    protected void settings(View rootView) {

    }
}
