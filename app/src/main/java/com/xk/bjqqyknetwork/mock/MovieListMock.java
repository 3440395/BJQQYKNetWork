package com.xk.bjqqyknetwork.mock;

import com.example.myhttp.mock.MockServer;
import com.xk.bjqqyknetwork.entry.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuekai on 2017/6/17.
 */

public class MovieListMock extends MockServer<List<Movie>> {
    @Override
    public List<Movie> setResult() {
        ArrayList<Movie> movies = new ArrayList<>();
        Movie movie1 = new Movie();
        movie1.setId(1);
        movie1.setMovieName("电影1");
        Movie movie2 = new Movie();
        movie2.setId(2);
        movie2.setMovieName("电影2");
        movies.add(movie1);
        movies.add(movie2);
        return movies;
    }
}
