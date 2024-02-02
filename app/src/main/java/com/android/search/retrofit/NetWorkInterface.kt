package com.android.search.retrofit


import com.android.search.data.Image
import com.android.search.data.Video
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
//interface NetWorkInterface {
//    @GET("/v2/search/image")
//    suspend fun getImg(@Header("Authorization")Authorization: String,
//                       @Query("query")search : String,
//                       @Query("sort")sort : String) : Image
//}
//비디오 까지

interface NetWorkInterface {
    @GET("v2/search/image")
    suspend fun image_search(
        @Header("Authorization") apiKey: String?,
        @Query("query") query: String?,
        @Query("sort") sort: String?,
//        @Query("page") page: Int,
        @Query("size") size: Int
    ) : Image

    @GET("v2/search/vclip")
    suspend fun video_search(
        @Header("Authorization") apiKey: String?,
        @Query("query") query: String?,
        @Query("sort") sort: String?,
//        @Query("page") page: Int,
        @Query("size") size: Int
    ) : Video
}