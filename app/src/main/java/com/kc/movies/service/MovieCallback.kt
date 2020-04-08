package com.kc.movies.service

interface MovieCallback<T> {
    fun onSuccess(data:List<T>?)
    fun onError(error:String?)
}