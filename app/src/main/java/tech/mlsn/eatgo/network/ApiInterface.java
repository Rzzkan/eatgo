package tech.mlsn.eatgo.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import tech.mlsn.eatgo.response.BaseResponse;
import tech.mlsn.eatgo.response.RestaurantInfoResponse;
import tech.mlsn.eatgo.response.dashboard.SlidersResponse;
import tech.mlsn.eatgo.response.login.LoginResponse;
import tech.mlsn.eatgo.response.profile.UpdateImageResponse;
import tech.mlsn.eatgo.response.restaurant.RestaurantsResponse;
import tech.mlsn.eatgo.response.restaurant.UserRestaurantResponse;
import tech.mlsn.eatgo.response.review.ReviewResponse;
import tech.mlsn.eatgo.response.user.UsersResponse;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("login.php")
    Call<LoginResponse> postLogin(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("register.php")
    Call<BaseResponse> postRegister(
            @Field("name") String name,
            @Field("username") String username,
            @Field("address") String address,
            @Field("phone") String phone,
            @Field("password") String password,
            @Field("role") String role
    );

    @FormUrlEncoded
    @POST("updateUserImg.php")
    Call<UpdateImageResponse> postUpdateImgUser(
            @Field("id_user") String id_user,
            @Field("img") String img
    );

    @FormUrlEncoded
    @POST("updateRestoImg.php")
    Call<UpdateImageResponse> postUpdateImgResto(
            @Field("id_restaurant") String id_restaurant,
            @Field("img") String img
    );

    @FormUrlEncoded
    @POST("updateUser.php")
    Call<BaseResponse> updateUser(
            @Field("id_user") String id_user,
            @Field("name") String name,
            @Field("address") String address,
            @Field("phone") String phone
    );

    @FormUrlEncoded
    @POST("updatePassword.php")
    Call<BaseResponse> postUpdatePw(
            @Field("id_user") String id,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("updateRestaurant.php")
    Call<BaseResponse> updateRestaurant(
            @Field("name") String name,
            @Field("address") String address,
            @Field("link") String link,
            @Field("phone") String phone
    );

    @FormUrlEncoded
    @POST("addResto.php")
    Call<BaseResponse> addRestaurant(
            @Field("id_user") String id_user,
            @Field("name") String name,
            @Field("address") String address,
            @Field("link") String link,
            @Field("phone") String phone
    );

    @FormUrlEncoded
    @POST("addMenu.php")
    Call<BaseResponse> addMenu(
            @Field("id_restaurant") String id_restaurant,
            @Field("name") String name,
            @Field("description") String description,
            @Field("category") String category,
            @Field("price") String price,
            @Field("image") String img
    );


    @GET("allUsers.php")
    Call<UsersResponse>getAllUsers();

    @GET("allRestaurant.php")
    Call<RestaurantsResponse>getAllRestaurants();

    @FormUrlEncoded
    @POST("getRestoInfo.php")
    Call<RestaurantInfoResponse>getRestoInfo(
            @Field("id_restaurant") String id_resto
    );

    @GET("allSliders.php")
    Call<SlidersResponse>getAllSliders();

    @FormUrlEncoded
    @POST("userDashboard.php")
    Call<UserRestaurantResponse> getUserDashboard(
            @Field("latitude") String latitude,
            @Field("longitude") String longitude
    );

    @FormUrlEncoded
    @POST("reviewResto.php")
    Call<ReviewResponse>getRestoReview(
            @Field("id_restaurant") String id_resto
    );



//
//    @FormUrlEncoded
//    @POST("register.php")
//    Call<BaseResponse> postRegister(
//            @Field("email") String email,
//            @Field("name") String name,
//            @Field("username") String username,
//            @Field("born") String born,
//            @Field("phone") String phone,
//            @Field("password") String password,
//            @Field("role") String role
//    );
//
//    @GET("allUsers.php")
//    Call<AllUsersResponse>getAllUsers();
//
//
//    @FormUrlEncoded
//    @POST("userDashboard.php")
//    Call<DashboardResponse> postDashboard(
//            @Field("id_user") String id,
//            @Field("date") String date,
//            @Field("number") String number
//    );
//
//    @FormUrlEncoded
//    @POST("genNumber.php")
//    Call<GenerateNumberResponse> postGenNumber(
//            @Field("date") String date
//    );
//
//    @FormUrlEncoded
//    @POST("addQueue.php")
//    Call<BaseResponse> postAddQueue(
//            @Field("id_user") String id,
//            @Field("date") String date,
//            @Field("number") String number,
//            @Field("status") String status
//    );
//
//    @FormUrlEncoded
//    @POST("updatePw.php")
//    Call<BaseResponse> postUpdatePw(
//            @Field("id_user") String id,
//            @Field("password") String password
//    );
//
//
//    @FormUrlEncoded
//    @POST("updateUser.php")
//    Call<BaseResponse> postUpdateUser(
//            @Field("id_user") String id,
//            @Field("name") String name,
//            @Field("phone") String phone
//    );
//
//    @FormUrlEncoded
//    @POST("allQueueNow.php")
//    Call<QueuesResponse> getQueue(
//            @Field("date") String date
//    );
//
//    @FormUrlEncoded
//    @POST("updateStatus.php")
//    Call<BaseResponse> setStatus(
//            @Field("id_queue") String id_queue,
//            @Field("status") String status
//    );
//
//    @FormUrlEncoded
//    @POST("updateMax.php")
//    Call<BaseResponse> setMax(
//            @Field("max") String max
//    );
//
//    @FormUrlEncoded
//    @POST("adminDashboard.php")
//    Call<AdminDashboardResponse> postAdminDashboard(
//            @Field("date") String date
//    );


}
