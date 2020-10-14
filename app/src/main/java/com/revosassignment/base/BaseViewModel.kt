package com.mobiquityassignment.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = this::class.java.canonicalName

    open fun onTextChanged(textChangeComponent: TextChangeComponent) {

    }

}

enum class TextChangeComponent {
    BOOK_RIDE_USER_NAME,
    BOOK_RIDE_MOBILE,
    BOOK_RIDE_DOB
}