package com.android.search.data

import com.google.gson.annotations.SerializedName

data class Video(
    @SerializedName("documents")
    var documents: ArrayList<Documents>,

    @SerializedName("meta")
    var meta: Meta
) {
    data class Documents(
        @SerializedName("title")
        var title: String,

        @SerializedName("url")
        var url: String,

        @SerializedName("datetime")
        var datetime: String,

        @SerializedName("play_time")
        var playTime: Int,

        @SerializedName("thumbnail")
        var thumbnail: String,

        @SerializedName("author")
        var author: String
    )
    data class Meta(
        @SerializedName("is_end")
        var isEnd: Boolean,

        @SerializedName("pageable_count")
        var pageableCount: Int,

        @SerializedName("total_count")
        var totalCount: Int
    )
}