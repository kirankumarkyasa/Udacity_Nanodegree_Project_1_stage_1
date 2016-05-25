package kyasa.com.popularmovies1.APIManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kiran on 12/4/16.
 */
public class RetroManager {

    Retrofit retrofit;
    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    public static final String API_KEY = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";


    public Retrofit getRetrofit() {

        //Interceptor to add Request Header
        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Accept", "application/json")
                        .build();
                return chain.proceed(newRequest);
            }
        };

        //Inteceptor for Logging Requests and Responses
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //Make list of interceptors for the okHttp Client
        List<Interceptor> interceptorsList = new ArrayList<>();
        interceptorsList.add(interceptor);
        interceptorsList.add(logInterceptor);

        //build okHttp Client with interceptors attached
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().addAll(interceptorsList);
        OkHttpClient client = builder.build();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        //Build Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit;
    }
}