package com.platform45.weather45.features.history.datetime

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TimePicker
import com.platform45.weather45.R
import com.platform45.weather45.base.fragments.BaseDialogFragment
import com.platform45.weather45.base.fragments.BaseLowDialog
import com.platform45.weather45.constants.TITLE
import java.sql.Time
import java.text.Format
import java.text.SimpleDateFormat


class DateTimePickerFragment : BaseLowDialog(){

    private var currentActivity: Context? = null
    private var isCancelled: Boolean = false
    private var dateTimeContext: DateTimeSetter? = null

    private var datePickerContainerLl: View? = null
    private var timePickerContainerLl: View? = null
    private var selectedDateDp: DatePicker? = null
    private var selectedTimeTp: TimePicker? = null
    protected var dateTimeNextBtn: Button? = null
    protected var dateTimeBackBtn: Button? = null
    protected var dateTimeDoneBtn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dateTimeContext = getParentFragment() as DateTimeSetter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.window?.setDimAmount(0.8f)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val title = arguments?.getString(TITLE)
        dialog?.window?.setTitle(title)

        val parentView = super.onCreateView(inflater, container, savedInstanceState)
        initViews(parentView)
        return parentView
    }


    fun initViews(parentView: View) {
        datePickerContainerLl = parentView.findViewById(R.id.llDatePickerContainer)
        selectedDateDp = parentView.findViewById(R.id.dpAppointmentDate)

        dateTimeNextBtn = parentView.findViewById(R.id.btnDateTimeNext)
        dateTimeNextBtn?.setOnClickListener {
            showTimePicker()
            // onSetDate(appointMentTimeDp?.year, appointMentTimeDp?.month, appointMentTimeDp?.dayOfMonth)
            //clientDashboardPresenter?.currentService?.timeScheduled = scheduledDate
        }

        timePickerContainerLl = parentView.findViewById(R.id.llTimePickerContainer)
        selectedTimeTp = parentView.findViewById(R.id.tpAppointmentTime)

        dateTimeBackBtn = parentView.findViewById(R.id.btnDateTimeBack)
        dateTimeBackBtn?.setOnClickListener{
            showDatePicker()
        }

        dateTimeDoneBtn = parentView.findViewById(R.id.btnDateTimeDone)
        dateTimeDoneBtn?.setOnClickListener {
            dismiss()
            //  onDateTimeSet( getTime(appointmentTimeTp?.currentHour!!, appointmentTimeTp?.currentMinute!!))
        }

        showDatePicker()
    }

    fun showDatePicker(){
        datePickerContainerLl?.visibility = View.VISIBLE
        timePickerContainerLl?.visibility = View.GONE
        dateTimeBackBtn?.visibility = View.GONE
        dateTimeNextBtn?.visibility = View.VISIBLE
        dateTimeDoneBtn?.visibility = View.GONE
    }

    fun showTimePicker(){
        datePickerContainerLl?.visibility = View.GONE
        timePickerContainerLl?.visibility = View.VISIBLE
        dateTimeBackBtn?.visibility = View.VISIBLE
        dateTimeNextBtn?.visibility = View.GONE
        dateTimeDoneBtn?.visibility = View.VISIBLE
    }

    private fun getTime(hr: Int, min: Int): String {
        val tme = Time(hr, min, 0)//seconds by default set to zero
        val formatter: Format
        formatter = SimpleDateFormat("h:mm a")
        return formatter.format(tme)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        currentActivity = context
    }

    override fun onBackPressed() {
        super.onBackPressed()
        isCancelled = true
    }
    override fun onDestroy() {
        super.onDestroy()
        if(isCancelled) return

        val year = selectedDateDp?.year ?: 0
        val month = selectedDateDp?.month ?: 0
        val day = selectedDateDp?.dayOfMonth ?: 0
        dateTimeContext?.setDate(year, month, day)

        val hour = selectedTimeTp?.currentHour ?: 0
        val min = selectedTimeTp?.currentMinute ?: 0
        val time = getTime(hour, min)
        dateTimeContext?.setTime(time)
    }

    interface DateTimeSetter{
        fun setDate(year: Int, month: Int, day: Int)
        fun setTime(scheduledTime: String)
    }

    companion object {
        fun newInstance(): BaseDialogFragment {
            val bundle = Bundle()
            val dateTimePickerFragment = DateTimePickerFragment()
            dateTimePickerFragment.arguments = bundle
            return dateTimePickerFragment
        }
    }
}
