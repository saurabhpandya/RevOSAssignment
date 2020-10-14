package com.revosassignment.utility

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener

class BindingUtils {

    companion object {
        @InverseBindingAdapter(attribute = "app:textInt", event = "textAttrChanged")
        @JvmStatic
        fun getIntFromTextView(editText: TextView): Int? {

            return try {
                editText.text.toString().toInt()
            } catch (e: NumberFormatException) {
                null
            }//catch

        }//getIntFromTextView

        @BindingAdapter("app:textAttrChanged")
        @JvmStatic
        fun setTextChangeListener(editText: TextView, listener: InverseBindingListener) {
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    listener.onChange()
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

            })
        }
    }

}