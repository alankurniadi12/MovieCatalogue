package com.example.moviecataloguefour.movie

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie (
    //var id: Int = 0,
    var title: String? = null,
    var description: String? = null,
    var poster: String? = null
): Parcelable


