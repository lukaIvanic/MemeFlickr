package com.example.memescrolller.api

data class MemeResponse(val kind: String, val data: ResponseData) {
    data class ResponseData(val modhash: String, val children: List<Post>, val after: String) {
        data class Post(val kind: String, val data: PostData) {
            data class PostData(var post_hint: String, val title: String, val url: String)

        }

    }
}