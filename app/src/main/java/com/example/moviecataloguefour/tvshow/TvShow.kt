package com.example.moviecataloguefour.tvshow

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TvShow (
    var title: String? = null,
    var description: String? = null,
    var poster: String? = null
): Parcelable

