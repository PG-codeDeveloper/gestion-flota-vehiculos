package com.example.flotaapp.retrofit;

import android.util.Base64;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://10.0.2.2:8080/";
    private static Retrofit retrofit = null;


    private static String usuario = "pablo";
    private static String contraseña = "pablo";

    public static void setCredenciales(String user, String pass) {
        usuario = user;
        contraseña = pass;
        retrofit = null; // Reinicia para aplicar nuevas credenciales
    }

    public static Retrofit getClient() {
        if (retrofit == null) {

            // Interceptor para añadir el encabezado Authorization

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            String credentials = Credentials.basic(usuario, contraseña);
                            Request request = chain.request().newBuilder()
                                    .addHeader("Authorization", credentials)
                                    .build();
                            return chain.proceed(request);
                        }
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ApiService getUsuarioApi() {
        return getClient().create(ApiService.class);
    }
}
