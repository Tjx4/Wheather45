package com.platform45.weather45.helpers

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.platform45.weather45.R
import com.platform45.weather45.base.fragments.BaseDialogFragment
import com.platform45.weather45.constants.LAYOUT
import com.platform45.weather45.constants.TITLE
import com.platform45.weather45.features.history.datetime.DateTimePickerFragment

fun showConfirmDialog(context: Context, title: String, message: String, yesButtonText: String, noButtonText: String, yesCallbackFun: () -> Unit, noCallbackFun: () -> Unit){
    val ab = setupBasicMessage(title, message, yesButtonText, "", noButtonText, yesCallbackFun,
        { }, noCallbackFun, context
    )
    ab.setIcon(R.drawable.confirm_icon)
    ab.setCancelable(false)
    showAlertMessage(ab, context)
}

fun showErrorDialog(context: Context, title: String, message: String, buttonText: String = context.getString(
    R.string.ok), callbackFun: () -> Unit = {}){
    val ab = setupBasicMessage(title, message, buttonText, "", "", callbackFun, {}, {}, context)
    ab.setIcon(R.drawable.ic_error)
    ab.setCancelable(false)
    showAlertMessage(ab, context)
}

private fun setupBasicMessage(title: String, message: String,
                              positiveButtonText: String?, neutralButtonText: String?, negativeButtonText: String?,
                              positiveCallback: () -> Unit, neutralCallback: () -> Unit, negativeCallback: () -> Unit,
                              context: Context
): AlertDialog.Builder {

    val ab = AlertDialog.Builder(context, R.style.AlertDialogCustom)
    ab.setMessage(message)
        .setTitle(title)
        .setPositiveButton(positiveButtonText) { dialogInterface, i ->
            positiveCallback()
        }

    if (neutralButtonText != null) {
        ab.setNeutralButton(neutralButtonText) { dialogInterface, i ->
            neutralCallback()
        }
    }

    if (negativeButtonText != null) {
        ab.setNegativeButton(negativeButtonText) { dialogInterface, i ->
            negativeCallback()
        }
    }

    return ab
}

private fun showAlertMessage(ab: AlertDialog.Builder, context: Context) {
    val a = ab.create()
    a.requestWindowFeature(Window.FEATURE_NO_TITLE)
    a.show()

    a.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(context.resources.getColor(R.color.darkText))
    a.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(context.resources.getColor(R.color.darkText))
    a.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(context.resources.getColor(R.color.darkText))
}


fun showDateTimeDialogFragment(dateTimeContext: DateTimePickerFragment.DateTimeSetter) {
    var dateTimePickerFragment = DateTimePickerFragment.newInstance()
    dateTimePickerFragment.isCancelable = true

    var title = "Date time picker"
    val layout = R.layout.fragment_date_time_picker

    val ft = if(dateTimeContext is Activity) (dateTimeContext as AppCompatActivity).supportFragmentManager.beginTransaction() else (dateTimeContext as Fragment).childFragmentManager.beginTransaction()

    var newFragment = getFragmentDialog(title, layout, dateTimePickerFragment)
    newFragment.show(ft, "dialog")

    //Todo: Rethink
    //activity.activeDialogFragment = newFragment
}

private fun getFragmentDialog(title: String, Layout: Int, newFragmentBaseBase: BaseDialogFragment) : BaseDialogFragment {
    val payload = newFragmentBaseBase.arguments
    payload?.putString(TITLE, title)
    payload?.putInt(LAYOUT, Layout)

    newFragmentBaseBase.arguments = payload
    return newFragmentBaseBase
}