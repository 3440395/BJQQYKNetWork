package com.xk.bjqqyknetwork;

import com.xk.bjqqyknetwork.entry.Movie;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
        ArrayList<Movie> movies = new ArrayList<>();
        Movie movie1 = new Movie();
        movie1.setId(1);
        movie1.setMovieName("1");
        Movie movie2 = new Movie();
        movie2.setId(2);
        movie2.setMovieName("2");
        Movie movie3 = new Movie();
        movie3.setId(3);
        movie3.setMovieName("3");
        movies.add(movie1);
        movies.add(movie2);
        movies.add(movie3);
        for (Movie movy : movies) {
            System.out.println(movy.toString());
        }
        for (Movie movy : movies) {
            if (movy.getId()==2) {
                movy.setId(22);
            }
        }
        for (Movie movy : movies) {
            System.out.println(movy.toString());
        }


    }
}