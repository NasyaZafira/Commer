package com.commer.app.data.remote

import com.commer.app.data.model.remote.bookmark.BookmarkResponse
import com.commer.app.data.model.remote.forgotpassword.ForgotPasswordBody
import com.commer.app.data.model.remote.forgotpassword.ForgotPasswordResponse
import com.commer.app.data.model.remote.login.LoginBody
import com.commer.app.data.model.remote.login.LoginResponse
import com.commer.app.data.model.remote.post.comment.delete.DeleteCommentResponse
import com.commer.app.data.model.remote.post.comment.insert.InsertCommentResponse
import com.commer.app.data.model.remote.post.delete.DeletePostResponse
import com.commer.app.data.model.remote.post.detail.DetailPostResponse
import com.commer.app.data.model.remote.post.editpost.EditPostResponse
import com.commer.app.data.model.remote.post.get.GetPostResponse
import com.commer.app.data.model.remote.post.like.LikePostResponse
import com.commer.app.data.model.remote.post.newpost.NewPostResponse
import com.commer.app.data.model.remote.post.unlike.UnlikePostResponse
import com.commer.app.data.model.remote.profile.GetDetailProfileResponse
import com.commer.app.data.model.remote.report.comment.ReportCommentResponse
import com.commer.app.data.model.remote.report.post.ReportPostResponse
import com.commer.app.data.model.remote.search.getusers.GetAllUsersResponse
import com.commer.app.data.model.remote.settings.account.UpdateAccountBody
import com.commer.app.data.model.remote.settings.transaction.SimplerTransactionResponse
import com.commer.app.data.model.remote.shop.ListShopResponse
import com.commer.app.data.model.remote.shop.buy.BuyResponse
import com.commer.app.data.model.remote.shop.detail.DetailShopResponse
import com.commer.app.data.model.remote.shop.history.HistoryShopResponse
import com.commer.app.data.model.remote.signup.SignUpBody
import com.commer.app.data.model.remote.signup.SignUpResponse
import com.commer.app.data.model.remote.simpler.CheckPaymentResponse
import com.commer.app.data.model.remote.simpler.SimplerPaymentResponse
import com.commer.app.data.model.remote.user.detail.GetDetailUserResponse
import com.commer.app.data.model.remote.user.follow.FollowUserResponse
import com.commer.app.data.model.remote.user.followers.FollowersResponse
import com.commer.app.data.model.remote.user.following.FollowingResponse
import com.commer.app.data.model.remote.user.suggested.SuggestedUserResponse
import com.commer.app.data.model.remote.user.unfollow.UnfollowUserResponse
import com.skydoves.sandwich.ApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @POST("login-user")
    suspend fun login(
        @Body loginBody: LoginBody
    ): ApiResponse<LoginResponse>

    @POST("register")
    suspend fun postSignUp(
        @Body body: SignUpBody
    ): ApiResponse<SignUpResponse>

    @POST("forget-password/send")
    suspend fun postResetPassword(
        @Body body: ForgotPasswordBody
    ): ApiResponse<ForgotPasswordResponse>

    @POST("forget-password/resend")
    suspend fun resendPassword(
        @Body body: ForgotPasswordBody
    ): ApiResponse<ForgotPasswordResponse>

    @POST("forget-password/validate")
    suspend fun codeVerify(
        @Body body: ForgotPasswordBody
    ): ApiResponse<ForgotPasswordResponse>

    @POST("forget-password/change-password")
    suspend fun postChangePassword(
        @Body body: ForgotPasswordBody
    ): ApiResponse<ForgotPasswordResponse>

    @GET("post/list")
    suspend fun getPostList(
        @Query("tags") tags: String?
    ): ApiResponse<GetPostResponse>

    @GET("user/sugested")
    suspend fun getSuggestedUser(): ApiResponse<SuggestedUserResponse>

    @GET("user/list")
    suspend fun getAllUsers(
        @Query("fullname") fullname: String?
    ): ApiResponse<GetAllUsersResponse>

    @POST("follow/{followIdUser}")
    suspend fun postFollowUser(
        @Path("followIdUser") followIdUser: Int
    ): ApiResponse<FollowUserResponse>

    @POST("unfollow/{followIdUser}")
    suspend fun postUnFollowUser(
        @Path("followIdUser") followIdUser: Int
    ): ApiResponse<UnfollowUserResponse>

    @POST("post/like/{idPost}")
    suspend fun postLikePost(
        @Path("idPost") idPost: Int
    ): ApiResponse<LikePostResponse>

    @POST("post/unlike/{idPost}")
    suspend fun postUnlikePost(
        @Path("idPost") idPost: Int
    ): ApiResponse<UnlikePostResponse>

    @DELETE("post/delete/{idPost}")
    suspend fun postDeletePost(
        @Path("idPost") idPost: Int
    ): ApiResponse<DeletePostResponse>

    @POST("bookmark/save/{idPost}")
    suspend fun postBookmarkPost(
        @Path("idPost") idPost: Int
    ): ApiResponse<BookmarkResponse>

    @DELETE("bookmark/delete/{idPost}")
    suspend fun deleteBookmarkPost(
        @Path("idPost") idPost: Int
    ): ApiResponse<BookmarkResponse>

    @Multipart
    @POST("post/save")
    suspend fun postNewPost(
        @Part("status") status: RequestBody,
        @Part("tags") tags: RequestBody,
        @Part("desc") desc: RequestBody,
        @Part file: List<MultipartBody.Part>?
    ): ApiResponse<NewPostResponse>

    @GET("post/detail/{idPost}")
    suspend fun getDetailPost(
        @Path("idPost") idPost: Int
    ): ApiResponse<DetailPostResponse>

    @POST("komentar/insert/{idPost}")
    suspend fun postComment(
        @Path("idPost") idPost: Int,
        @Query("isiKomentar") fieldComment: String
    ): ApiResponse<InsertCommentResponse>

    @DELETE("komentar/delete/{idComment}")
    suspend fun deleteComment(
        @Path("idComment") idComment: Int
    ): ApiResponse<DeleteCommentResponse>

    @POST("report/insert/{idPost}")
    suspend fun postReportPost(
        @Path("idPost") idPost: Int,
        @Query("reason") reason: String
    ): ApiResponse<ReportPostResponse>

    @POST("report/comment/{idComment}")
    suspend fun postReportComment(
        @Path("idComment") idComment: Int,
        @Query("reason") reason: String
    ): ApiResponse<ReportCommentResponse>

    @GET("user/detail/{idUser}")
    suspend fun getDetailUser(
        @Path("idUser") idUser: Int
    ): ApiResponse<GetDetailUserResponse>

    @GET("list/following/{idUser}")
    suspend fun getUserFollowing(
        @Path("idUser") idUser: Int
    ): ApiResponse<FollowingResponse>

    @GET("list/followers/{idUser}")
    suspend fun getUserFollowers(
        @Path("idUser") idUser: Int
    ): ApiResponse<FollowersResponse>

    @POST("oauth/token")
    suspend fun postRefreshToken(
        @Query("grant_type") grantType: String,
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("refresh_token") refreshToken: String,
    ): ApiResponse<RefreshTokenResponse>

    @Multipart
    @PUT("post/edit/{idPost}")
    suspend fun editPost(
        @Path("idPost") idPost: Int,
        @Part("status") status: RequestBody,
        @Part("tags") tags: RequestBody,
        @Part("desc") desc: RequestBody
    ): ApiResponse<EditPostResponse>

    @Multipart
    @PUT("profile/update")
    suspend fun updateProfile(
        @Part("fullname") fullName: RequestBody,
        @Part("bio") bio: RequestBody,
        @Part file: List<MultipartBody.Part>?
    ): ApiResponse<GetDetailProfileResponse>

    @GET("bookmark/list")
    suspend fun getBookmarks(): ApiResponse<GetPostResponse>

    @PUT("profile/account")
    suspend fun updateAccount(
        @Body updateAccountBody: UpdateAccountBody
    ): ApiResponse<GetDetailProfileResponse>

    @GET("simpler/history")
    suspend fun getTransactionHistory(): ApiResponse<SimplerTransactionResponse>

    //history shop
    @GET("history")
    suspend fun getHistoryShop() : ApiResponse<HistoryShopResponse>
    @POST("report/user/{idUser}")
    suspend fun postReportUser(
        @Path("idUser") idUser: Int,
        @Query("reason") reason: String
    ): ApiResponse<ReportCommentResponse>

    @GET("post/list-simpler/verified")
    suspend fun getAllVerifiedPost(
        @Query("tags") tags: String?
    ): ApiResponse<GetPostResponse>

    @GET("post/list-simpler/official")
    suspend fun getOfficialPost(
        @Query("tags") tags: String?
    ): ApiResponse<GetPostResponse>

    @Multipart
    @POST("simpler/payment")
    suspend fun postSimplerReceipts(
        @Part("plan") plan: RequestBody,
        @Part("transaction_id") transaction_id: RequestBody,
        @Part file: List<MultipartBody.Part>?
    ): ApiResponse<SimplerPaymentResponse>

    @GET("check/payment")
    suspend fun getPaymentStatus(): ApiResponse<CheckPaymentResponse>

    @GET("product")
    suspend fun listProduct() : ApiResponse<ListShopResponse>

    @GET("product/{id}")
    suspend fun detailProduct(
        @Path("id") id: Int
    ) : ApiResponse<DetailShopResponse>

    @GET("product/buy/{id}")
    suspend fun buyProduct(
        @Path("id") id: Int
    ) : ApiResponse<BuyResponse>
}