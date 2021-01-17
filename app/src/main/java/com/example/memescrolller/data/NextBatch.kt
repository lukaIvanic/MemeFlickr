package com.example.memescrolller.data

import io.realm.RealmObject

open class NextBatch (
    var after: String = "",
    var curr: String = ""
        ): RealmObject()