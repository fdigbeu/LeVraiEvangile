package org.levraievangile.Model;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Maranatha on 10/10/2017.
 */

public class ApiClient {
    public static final String BASE_URL_LVE = "http://www.levraievangile.org/";
    public static Retrofit retrofitLVE = null;

    public static Retrofit getApiClientLeVraiEvangile(){
        if(retrofitLVE==null){
            retrofitLVE = new Retrofit.Builder()
                    .baseUrl(BASE_URL_LVE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitLVE;
    }
}
