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
import tech.mlsn.eatgo.response.menu.AllMenuResponse;
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
            @Field("id_restaurant") String id_restaurant,
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

    @FormUrlEncoded
    @POST("allMenus.php")
    Call<AllMenuResponse>allMenus(
            @Field("id_restaurant") String id_resto
    );

    @FormUrlEncoded
    @POST("allMenusActived.php")
    Call<AllMenuResponse>allMenusActived(
            @Field("id_restaurant") String id_resto
    );




}
