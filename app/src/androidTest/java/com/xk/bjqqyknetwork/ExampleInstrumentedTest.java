package com.xk.bjqqyknetwork;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.myhttp.api.Api;
import com.example.myhttp.api.ApiManager;
import com.example.myhttp.util.LogUtil;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        Api getMovie = ApiManager.findApi("getMovie");
        Api sendReport = ApiManager.findApi("sendReport");
        Api getMovieList = ApiManager.findApi("getMovieList");
        Api get1MovieList = ApiManager.findApi("get1MovieList");
        LogUtil.d("ExampleInstrumentedTest:useAppContext-->"+getMovie);
        LogUtil.d("ExampleInstrumentedTest:useAppContext-->"+sendReport);
        LogUtil.d("ExampleInstrumentedTest:useAppContext-->"+getMovieList);
        LogUtil.d("ExampleInstrumentedTest:useAppContext-->"+get1MovieList);
    }
}
