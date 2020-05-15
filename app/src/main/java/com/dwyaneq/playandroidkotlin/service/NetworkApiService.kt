package com.dwyaneq.playandroidkotlin.service

import com.dwyaneq.playandroidkotlin.data.CommonPagerResult
import com.dwyaneq.playandroidkotlin.data.CommonResult
import com.dwyaneq.playandroidkotlin.data.bean.*
import retrofit2.http.*

/**
 * Created by DWQ on 2020/4/27.
 * E-Mail:lomapa@163.com
 */
interface NetworkApiService {
    /**
     * 用户登录
     */
    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("username") username: String
        , @Field("password") password: String
    ): CommonResult<UserInfo>

    /**
     * 用户注册
     */
    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String
    ): CommonResult<UserInfo>

    /**
     * 用户退出登录
     */
    @GET("user/logout/json")
    suspend fun logout(): CommonResult<Any?>


    /**
     * 轮播图
     */
    @GET("banner/json")
    suspend fun getBannerList(): CommonResult<ArrayList<BannerResult>>

    /**
     * 首页文章
     */
    @GET("article/list/{pageIndex}/json")
    suspend fun getArticleList(@Path("pageIndex") pageIndex: Int)
            : CommonResult<CommonPagerResult<ArrayList<ArticleResult>>>

    /**
     * 首页置顶文章
     */
    @GET("article/top/json")
    suspend fun getTopArticleList()
            : CommonResult<ArrayList<ArticleResult>>

    /**
     * 搜索热词
     */
    @GET("hotkey/json")
    suspend fun getHotKeyList(): CommonResult<ArrayList<HotKeyResult>>

    /**
     * 搜索
     */
    @POST("article/query/{pageIndex}/json")
    suspend fun getSearchList(@Path("pageIndex") pageIndex: Int, @Query("k") k: String)
            : CommonResult<CommonPagerResult<ArrayList<ArticleResult>>>

    /**
     * 获取个人积分
     */
    @GET("lg/coin/userinfo/json")
    suspend fun getPersonalCoin(): CommonResult<PersonalCoinResult>

    /**
     * 收藏文章
     */

    @POST("lg/collect/{id}/json")
    suspend fun collect(@Path("id") id: Int): CommonResult<Any?>

    /**
     * 取消收藏文章
     */

    @POST("lg/uncollect_originId/{id}/json")
    suspend fun cancelCollect(@Path("id") id: Int): CommonResult<Any?>

    /**
     * 收藏站外文章
     */
    @POST("lg/collect/add/json")
    suspend fun collectUrl(
        @Field("title") title: String,
        @Field("author") author: String,
        @Field("link") link: String
    ): CommonResult<Any?>

    /**
     * 取消收藏站外文章
     */
    @POST("lg/collect/add/json")
    suspend fun cancelCollectUrl(
        @Field("title") title: String,
        @Field("author") author: String,
        @Field("link") link: String
    ): CommonResult<Any?>

    /**
     * 取消收藏我的收藏
     */
    @FormUrlEncoded
    @POST("lg/uncollect/{id}/json")
    suspend fun cancelCollection(
        @Path("id") id: Int,
        @Field("originId") originId: Int
    ): CommonResult<Any?>

    /**
     * 获取公众号列表
     */
    @GET("wxarticle/chapters/json")
    suspend fun getWXTabList(): CommonResult<ArrayList<TabResult>>

    /**
     * 获取某个公众号历史文章
     */
    @GET("wxarticle/list/{id}/{pageIndex}/json")
    suspend fun getWXArticleList(
        @Path("id") id: Int
        , @Path("pageIndex") pageIndex: Int
    ): CommonResult<CommonPagerResult<ArrayList<ArticleResult>>>

    /**
     * 获取项目分类
     */
    @GET("project/tree/json")
    suspend fun getProjectTabList(): CommonResult<ArrayList<TabResult>>

    /**
     * 获取某一分类下项目文章列表
     */
    @GET("project/list/{pageIndex}/json")
    suspend fun getProjectArticleList(
        @Path("pageIndex") pageIndex: Int,
        @Query("cid") id: Int
    ): CommonResult<CommonPagerResult<ArrayList<ArticleResult>>>

    /**
     * 获取最新项目列表
     */
    @GET("article/listproject/{pageIndex}/json")
    suspend fun getNewProjectList(@Path("pageIndex") pageIndex: Int)
            : CommonResult<CommonPagerResult<ArrayList<ArticleResult>>>

    /**
     * 获取广场文章列表
     */
    @GET("user_article/list/{pageIndex}/json")
    suspend fun getSquareList(@Path("pageIndex") pageIndex: Int)
            : CommonResult<CommonPagerResult<ArrayList<ArticleResult>>>

    /**
     * 获取导航数据
     */
    @GET("navi/json")
    suspend fun getNavigatorList(): CommonResult<ArrayList<NavigatorResult>>

    /**
     * 问答列表
     */
    @GET("wenda/list/{pageIndex}/json")
    suspend fun getQAList(@Path("pageIndex") pageIndex: Int)
            : CommonResult<CommonPagerResult<ArrayList<ArticleResult>>>

    /**
     * 体系分类
     */

    @GET("tree/json")
    suspend fun getSystemTabList(): CommonResult<ArrayList<SystemTabResult>>

    /**
     * 获取体系下文章列表
     */

    @GET("article/list/{pageIndex}/json")
    suspend fun getSystemArticleList(
        @Path("pageIndex") pageIndex: Int
        , @Query("cid") cid: Int
    ): CommonResult<CommonPagerResult<ArrayList<ArticleResult>>>

    /**
     * 我的积分
     */
    @GET("lg/coin/list/{pageIndex}/json")
    suspend fun getCoinHistoryList(@Path("pageIndex") pageIndex: Int)
            : CommonResult<CommonPagerResult<ArrayList<IntegralHistoryResult>>>

    /**
     * 我的收藏列表
     */
    @GET("lg/collect/list/{pageIndex}/json")
    suspend fun getCollectionList(@Path("pageIndex") pageIndex: Int)
            : CommonResult<CommonPagerResult<ArrayList<CollectionResult>>>
}