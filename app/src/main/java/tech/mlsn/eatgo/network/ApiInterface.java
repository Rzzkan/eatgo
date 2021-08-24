package tech.mlsn.eatgo.network;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import tech.mlsn.eatgo.response.BaseResponse;
import tech.mlsn.eatgo.response.DashboardAdminResponse;
import tech.mlsn.eatgo.response.DetailOrderResponse;
import tech.mlsn.eatgo.response.OrdersResponse;
import tech.mlsn.eatgo.response.PointResponse;
import tech.mlsn.eatgo.response.RestaurantInfoResponse;
import tech.mlsn.eatgo.response.chat.DetailChatResponse;
import tech.mlsn.eatgo.response.dashboard.SlidersResponse;
import tech.mlsn.eatgo.response.login.LoginResponse;
import tech.mlsn.eatgo.response.menu.AllMenuResponse;
import tech.mlsn.eatgo.response.menu.MenuResponse;
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
            @Field("point_earned") String point_earned,
            @Field("image") String img,
            @Field("is_active") String is_active

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

    @FormUrlEncoded
    @POST("updateRestaurantAdmin.php")
    Call<BaseResponse> updateRestaurantAdmin(
            @Field("id_restaurant") String id_restaurant,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("is_active") String is_active
    );

    @FormUrlEncoded
    @POST("deleteMenu.php")
    Call<BaseResponse> deleteMenu(
            @Field("id_menu") String id_menu
    );

    @FormUrlEncoded
    @POST("deleteSlider.php")
    Call<BaseResponse> deleteslider(
            @Field("id_slider") String id_slider
    );

    @FormUrlEncoded
    @POST("addRating.php")
    Call<BaseResponse> postAddReview(
            @Field("id_user") String id_user,
            @Field("id_restaurant") String id_restaurant,
            @Field("rating") String rating,
            @Field("review") String review
    );

    @FormUrlEncoded
    @POST("updateMenu.php")
    Call<BaseResponse> updateMenu(
            @Field("id_menu") String id_menu,
            @Field("name") String name,
            @Field("description") String description,
            @Field("category") String category,
            @Field("price") String price,
            @Field("point_earned") String point_earned,
            @Field("image") String img,
            @Field("is_active") String is_active
    );

    @FormUrlEncoded
    @POST("getMenu.php")
    Call<MenuResponse>getMenu(
            @Field("id_menu") String id_menu
    );

    @GET("adminDashboard.php")
    Call<DashboardAdminResponse>getAdminDashboard();

    @FormUrlEncoded
    @POST("addChat.php")
    Call<BaseResponse>addChat(
            @Field("id_from") String id_from,
            @Field("id_to") String id_to,
            @Field("message") String message,
            @Field("date") String date
    );

    @FormUrlEncoded
    @POST("getAllChat.php")
    Call<DetailChatResponse>getAllChat(
            @Field("id_from") String id_from
    );

    @FormUrlEncoded
    @POST("getDetailChat.php")
    Call<DetailChatResponse>getDetailChat(
            @Field("id_from") String id_from,
            @Field("id_to") String id_to
    );

    @FormUrlEncoded
    @POST("getPoint.php")
    Call<PointResponse>getPoint(
            @Field("id_user") String id_user
    );

    @FormUrlEncoded
    @POST("setActiveResto.php")
    Call<BaseResponse>setActive(
            @Field("id_restaurant") String id_restaurant,
            @Field("is_active") String is_active
    );


    @GET("allOrders.php")
    Call<OrdersResponse>getAllOrders();

    @FormUrlEncoded
    @POST("getOrderUser.php")
    Call<OrdersResponse>getOrderUser(
            @Field("id_user") String id_user
    );

    @FormUrlEncoded
    @POST("getOrderDetail.php")
    Call<DetailOrderResponse>getDetailOrder(
            @Field("id_order") String id_order
    );

    @FormUrlEncoded
    @POST("getOrderDetailUser.php")
    Call<DetailOrderResponse>getDetailOrderUser(
            @Field("id_order") String id_order
    );


    @FormUrlEncoded
    @POST("setStatusOrder.php")
    Call<BaseResponse>setStatusOrder(
            @Field("id_order") String id_order,
            @Field("status") String status
    );

    @FormUrlEncoded
    @POST("addOrder.php")
    Call<BaseResponse>addOrder(
            @Field("id_user") String id_user,
            @Field("id_restaurant") String id_restaurant,
            @Field("menu_name[]") ArrayList<String> menu_name,
            @Field("menu_price[]") ArrayList<String> menu_price,
            @Field("menu_qty[]") ArrayList<String> menu_qty,
            @Field("total") String  total,
            @Field("point_used") String  point_used,
            @Field("point_earned") String  point_earned,
            @Field("status") String  status,
            @Field("payment") String  payment,
            @Field("date") String  date
    );
}
