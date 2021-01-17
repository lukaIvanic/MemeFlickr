package com.example.memescrolller.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import com.example.memescrolller.data.Meme
import com.example.memescrolller.data.MemeRepository

class MemeViewModel @ViewModelInject constructor(val repository: MemeRepository)
    : ViewModel(){
        val meme: LiveData<PagingData<Meme>> = repository.getMeme()
    }