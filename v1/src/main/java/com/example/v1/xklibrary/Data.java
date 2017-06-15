package com.example.v1.xklibrary;

/**
 * Created by xuekai on 2017/6/15.
 */

public class Data {
//    {
//        "isError":0,
//            "errorType":0,
//            "errorMessage":"",
//            "result":{
//        "movieId":1,
//                "movieName":"WarCarft"
//    }
//    }

    public int getIsError() {
        return isError;
    }

    public void setIsError(int isError) {
        this.isError = isError;
    }

    public int getErrorType() {
        return errorType;
    }

    public void setErrorType(int errorType) {
        this.errorType = errorType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    private int isError;
    private int errorType;
    private String errorMessage;
    private Result result;

    class Result {
        @Override
        public String toString() {
            return "Result{" +
                    "movieId=" + movieId +
                    ", movieName='" + movieName + '\'' +
                    '}';
        }

        public int getMovieId() {
            return movieId;
        }

        public void setMovieId(int movieId) {
            this.movieId = movieId;
        }

        public String getMovieName() {
            return movieName;
        }

        public void setMovieName(String movieName) {
            this.movieName = movieName;
        }

        private int movieId;
        private String movieName;
    }
}
