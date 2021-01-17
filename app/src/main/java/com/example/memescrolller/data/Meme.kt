package com.example.memescrolller.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Meme (
    @PrimaryKey
    var id: Int = 0,
    var title: String = "No title",
    var url: String = "No url",
    var postLink: String = "No post link",
    var seen: Boolean = false
) : RealmObject()