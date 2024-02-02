package com.android.search.data

import com.google.gson.annotations.SerializedName

//data class Image(
//    @SerializedName("documents")
//    val documents: List<Document>,
//    @SerializedName("meta")
//    val meta : Meta)
//
//data class Meta(
//    val is_end: Boolean,
//    val pageable_count: Int,
//    val total_count: Int
//)
//
//data class Document (
//    val collection: String,
//    val datetime: String,
//    val display_sitename: String,
//    val doc_url: String,
//    val height: Int,
//    val image_url: String,
//    val thumbnail_url: String,
//    val width: Int
//)
//모델클래스로

data class Image(
    @SerializedName("documents")
    val documents: ArrayList<Documents>,

    @SerializedName("meta")
    val meta: Meta
) {

    data class Documents(
        @SerializedName("collection")
        val collection: String,

        @SerializedName("thumbnail_url")
        val thumbnailUrl: String,

        @SerializedName("image_url")
        val imageUrl: String,

        @SerializedName("width")
        val width: Int,

        @SerializedName("height")
        val height: Int,

        @SerializedName("display_sitename")
        val displaySitename: String,

        @SerializedName("doc_url")
        val docUrl: String,

        @SerializedName("datetime")
        val datetime: String
    )

    data class Meta(
        @SerializedName("is_end")
        val isEnd: Boolean,

        @SerializedName("pageable_count")
        val pageableCount: Int,

        @SerializedName("total_count")
        val totalCount: Int
    )
}

