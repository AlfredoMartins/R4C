package ao.co.r4c.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static final String BASE_IP = "http://192.168.43.30";
    //public static final String BASE_IP = "http://192.168.137.1";

    private static final String PORT = ":80/";

    //private static final String PORT = ":81/";

    public static final String BASE_URL = BASE_IP + PORT;
    private static final String BASE_OLD = "http://192.168.137.1:80/";
    private static Retrofit retrofit = null;

    public static Retrofit getApiClient() {

        if (retrofit == null) {
            //Gson gson = new GsonBuilder().setLenient().create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }
}
