package com.example.memescrolller

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm

@HiltAndroidApp
class MemeScrollerApplication :Application(){
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}