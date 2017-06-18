package com.xk.bjqqyknetwork.entry;

/**
 * Created by xuekai on 2017/6/17.
 */

public class Movie {
    private int movieId;
    private String movieName;

    public int getId() {
        return movieId;
    }

    public void setId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "movieId=" + movieId +
                ", movieName='" + movieName + '\'' +
                '}';
    }
}
