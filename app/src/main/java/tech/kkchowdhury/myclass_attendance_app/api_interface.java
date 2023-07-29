package tech.kkchowdhury.myclass_attendance_app;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import tech.kkchowdhury.myclass_attendance_app.model.responsemodel;

public interface api_interface{
    @FormUrlEncoded
    @POST("retrofit_login.php")
    Call<responsemodel> verifyuser(
            @Field("email") String email,
            @Field("password") String password
    );
}

