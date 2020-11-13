package com.example.pagingwithroomexample.repository

import com.example.viewimages.model.ImageItem
import com.memebattle.pwc.util.PwcListing


interface PixabayImagesRepository {
    fun getImages() : PwcListing<ImageItem>
}