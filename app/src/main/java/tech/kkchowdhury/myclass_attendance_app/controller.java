package tech.kkchowdhury.myclass_attendance_app;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tech.kkchowdhury.myclass_attendance_app.backend_api.ApiUrls;

public class controller {
    private static final String url= ApiUrls.BASE_URL;
    private static controller clientObject;
    private static Retrofit retrofit;

    controller() {
        retrofit=new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized controller getInstance() {
        if(clientObject==null)
            clientObject=new controller();
        return  clientObject;
    }

    public api_interface getapi()
    {
        return retrofit.create(api_interface.class);
    }
}