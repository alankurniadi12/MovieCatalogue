package com.example.moviecataloguefour.favorite

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Favorite (
    var id: Int = 0,
    var title: String? = null,
    var description: String? = null,
    var poster: String? = null
): Parcelable