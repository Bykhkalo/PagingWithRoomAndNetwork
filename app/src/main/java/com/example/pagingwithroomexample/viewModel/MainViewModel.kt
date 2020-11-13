package com.example.pagingwithroomexample.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.pagingwithroomexample.repository.MainRepository
import com.example.pagingwithroomexample.utils.Debug

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    private val trigger = MutableLiveData<Boolean>()
    private val repoResult = Transformations.map(trigger) {
        repository.getImages()
    }

    val images = Transformations.switchMap(repoResult) { it.pagedList }!!
    val networkState = Transformations.switchMap(repoResult) { it.networkState }!!
    val refreshState = Transformations.switchMap(repoResult) { it.refreshState }!!


    fun refresh() {
        repoResult.value?.refresh?.invoke()
    }

    fun retry() {
        val listing = repoResult?.value
        listing?.retry?.invoke()
    }


    fun begin(){
        trigger.value = true
    }

}