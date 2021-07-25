package com.mayokunadeniyi.instantweather.utils

import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import android.view.ActionMode
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.SearchEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import androidx.annotation.Nullable

/**
 * Created by Mayokun Adeniyi on 22/02/2021.
 */

class UserInteractionAwareCallback(
    private val originalCallback: Window.Callback,
    private val activity: Activity?
) : Window.Callback {

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        return originalCallback.dispatchKeyEvent(event)
    }

    override fun dispatchKeyShortcutEvent(event: KeyEvent): Boolean {
        return originalCallback.dispatchKeyShortcutEvent(event)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> activity?.onUserInteraction()
        }
        return originalCallback.dispatchTouchEvent(event)
    }

    override fun dispatchTrackballEvent(event: MotionEvent): Boolean {
        return originalCallback.dispatchTrackballEvent(event)
    }

    override fun dispatchGenericMotionEvent(event: MotionEvent): Boolean {
        return originalCallback.dispatchGenericMotionEvent(event)
    }

    override fun dispatchPopulateAccessibilityEvent(event: AccessibilityEvent): Boolean {
        return originalCallback.dispatchPopulateAccessibilityEvent(event)
    }

    @Nullable
    override fun onCreatePanelView(featureId: Int): View? {
        return originalCallback.onCreatePanelView(featureId)
    }

    override fun onCreatePanelMenu(featureId: Int, menu: Menu): Boolean {
        return originalCallback.onCreatePanelMenu(featureId, menu)
    }

    override fun onPreparePanel(featureId: Int, view: View?, menu: Menu): Boolean {
        return originalCallback.onPreparePanel(featureId, view, menu)
    }

    override fun onMenuOpened(featureId: Int, menu: Menu): Boolean {
        return originalCallback.onMenuOpened(featureId, menu)
    }

    override fun onMenuItemSelected(featureId: Int, item: MenuItem): Boolean {
        return originalCallback.onMenuItemSelected(featureId, item)
    }

    override fun onWindowAttributesChanged(attrs: WindowManager.LayoutParams) {
        originalCallback.onWindowAttributesChanged(attrs)
    }

    override fun onContentChanged() {
        originalCallback.onContentChanged()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        originalCallback.onWindowFocusChanged(hasFocus)
    }

    override fun onAttachedToWindow() {
        originalCallback.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        originalCallback.onDetachedFromWindow()
    }

    override fun onPanelClosed(featureId: Int, menu: Menu) {
        originalCallback.onPanelClosed(featureId, menu)
    }

    override fun onSearchRequested(): Boolean {
        return originalCallback.onSearchRequested()
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onSearchRequested(searchEvent: SearchEvent): Boolean {
        return originalCallback.onSearchRequested(searchEvent)
    }

    @Nullable
    override fun onWindowStartingActionMode(callback: ActionMode.Callback): ActionMode? {
        return originalCallback.onWindowStartingActionMode(callback)
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Nullable
    override fun onWindowStartingActionMode(callback: ActionMode.Callback, type: Int): ActionMode? {
        return originalCallback.onWindowStartingActionMode(callback, type)
    }

    override fun onActionModeStarted(mode: ActionMode) {
        originalCallback.onActionModeStarted(mode)
    }

    override fun onActionModeFinished(mode: ActionMode) {
        originalCallback.onActionModeFinished(mode)
    }
}
