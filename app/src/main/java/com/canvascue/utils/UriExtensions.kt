package com.canvascue.utils

import android.net.Uri
import android.widget.ImageView

fun ImageView.setImageURIString(uriString: String?) {
    uriString?.let {
        this.setImageURI(Uri.parse(it))
    }
}