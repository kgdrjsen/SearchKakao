package com.android.search.data

//data class KakaoImage(
//    var site: String,
//    var time: String,
//    var imageUrl: String,
////    var isLike: Boolean = false
//)

class KakaoImage(var type: Int, var site: String, var time: String, var url: String) {
    var isLike = false
}
