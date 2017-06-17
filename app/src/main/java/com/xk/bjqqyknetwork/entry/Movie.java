package com.xk.bjqqyknetwork.entry;

/**
 * Created by xuekai on 2017/6/17.
 */

public class Movie {
    private int id;
    private String movieName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
                "id=" + id +
                ", movieName='" + movieName + '\'' +
                '}';
    }
}
