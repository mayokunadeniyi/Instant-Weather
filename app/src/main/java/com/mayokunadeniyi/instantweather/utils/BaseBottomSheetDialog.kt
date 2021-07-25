package com.mayokunadeniyi.instantweather.utils

import android.app.Activity
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 * Created by Mayokun Adeniyi on 7/24/21.
 */

class BaseBottomSheetDialog(private val activity: Activity, theme: Int) :
    BottomSheetDialog(activity, theme) {

    override fun onStart() {
        super.onStart()
        this.window?.let {
            it.callback = UserInteractionAwareCallback(it.callback, activity)
        }
    }
}
