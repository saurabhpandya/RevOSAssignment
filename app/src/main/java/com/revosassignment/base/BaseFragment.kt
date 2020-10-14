package com.mobiquityassignment.base

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.revosassignment.ui.MainActivity
import com.revosassignment.R

abstract class BaseFragment : Fragment() {

    private lateinit var builder: AlertDialog.Builder

    fun updateTitle(title: String?) {
        (activity as MainActivity).supportActionBar?.title = title ?: activity?.actionBar?.title
    }

    fun navigateUp() {
        findNavController().navigateUp()
    }

    fun onBackPressed() {
        activity?.onBackPressed()
    }

    fun showAlert(title: String, message: String) {
        builder = AlertDialog.Builder(activity)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {}
        })
        builder.show()

    }

    fun showProgress(show: Boolean) {
        var prgrsIndicator = activity?.findViewById<ConstraintLayout>(R.id.prgrs)
        if (show) {
            prgrsIndicator?.visibility = View.VISIBLE
        } else {
            prgrsIndicator?.visibility = View.GONE
        }
    }
}