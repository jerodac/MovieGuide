package br.eng.jerodac.movieguide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import br.eng.jerodac.movieguide.business.RestError;
import br.eng.jerodac.movieguide.controllers.MainController;
import br.eng.jerodac.movieguide.interfaces.MovieListListener;
import br.eng.jerodac.movieguide.vo.MovieListResponse;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(getApplicationContext(), "execute", Toast.LENGTH_SHORT).show();
        MainController.getInstance().requestMostPopularMovies(new MovieListListener() {
            @Override
            public void success(MovieListResponse response) {
                Toast.makeText(getApplicationContext(), "Sucesso", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void error(RestError error) {
                Toast.makeText(getApplicationContext(), "ERro", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
