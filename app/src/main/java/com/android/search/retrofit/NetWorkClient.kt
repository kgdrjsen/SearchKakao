package com.android.search.retrofit

import com.android.search.data.Constants
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetWorkClient {
    //굳이 안 바꿔도 될거 같으니 밑에 예제코드만
    private fun createOkHttpClient() : OkHttpClient {
        val interceptor = HttpLoggingInterceptor()

        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addNetworkInterceptor(interceptor)
            .build()
    }
    var gson = GsonBuilder().setLenient().create()

    private val imgRetrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).client(
            createOkHttpClient()).build()

    val imgNetWork : NetWorkInterface = imgRetrofit.create(NetWorkInterface::class.java)

    // API 서비스 객체를 반환한다.
//    val apiService: Retrofit_interface
//        get() = instance.create(Retrofit_interface::class.java)
//
//    // Retrofit 인스턴스를 초기화하고 반환한다.
//    private val instance: Retrofit
//        private get() {
//            // Gson 객체 생성. setLenient()는 JSON 파싱이 좀 더 유연하게 처리되도록 한다.
//            val gson = GsonBuilder().setLenient().create()
//
//            // Retrofit 빌더를 사용하여 Retrofit 인스턴스 생성
//            return Retrofit.Builder()
//                .baseUrl(Constants.BASE_URL)  // 기본 URL 설정
//                .addConverterFactory(GsonConverterFactory.create(gson))  // JSON 파싱을 위한 컨버터 추가
//                .build()
//        }
}