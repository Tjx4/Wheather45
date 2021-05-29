package com.platform45.weather45.features.history.datetime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TimePicker
import com.platform45.weather45.R
import com.platform45.weather45.base.fragments.BaseDialogFragment
import com.platform45.weather45.constants.DATETIME
import com.platform45.weather45.constants.TITLE
import java.sql.Time
import java.text.Format
import java.text.SimpleDateFormat

class DateTimePickerFragment : BaseDialogFragment(){
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
        dateTimeContext = parentFragment as DateTimeSetter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        /*
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.window?.setDimAmount(0.8f)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        */
        //val title = arguments?.getString(TITLE)
       // dialog?.window?.setTitle(title)

        val parentView = super.onCreateView(inflater, container, savedInstanceState)
        initViews(parentView)
        return parentView
    }

    fun initViews(parentView: View?) {
        val dateTime = arguments?.getString(DATETIME)
        val ymd = dateTime?.split("-")
        val y = ymd?.get(0)?.toInt() ?: 0
        var m =  ymd?.get(1)?.toInt()  ?: 0
        m =  if(m > 0) m  - 1 else  m
        val d = ymd?.get(2)?.toInt()  ?: 0

        datePickerContainerLl = parentView?.findViewById(R.id.llDatePickerContainer)
        selectedDateDp = parentView?.findViewById(R.id.dpAppointmentDate)
        selectedDateDp?.init(y, m, d, null)

        dateTimeNextBtn = parentView?.findViewById(R.id.btnDateTimeNext)
        dateTimeNextBtn?.setOnClickListener {
            showTimePicker()
        }

        timePickerContainerLl = parentView?.findViewById(R.id.llTimePickerContainer)
        selectedTimeTp = parentView?.findViewById(R.id.tpAppointmentTime)

        dateTimeBackBtn = parentView?.findViewById(R.id.btnDateTimeBack)
        dateTimeBackBtn?.setOnClickListener{
            showDatePicker()
        }

        dateTimeDoneBtn = parentView?.findViewById(R.id.btnDateTimeDone)
        dateTimeDoneBtn?.setOnClickListener {
            setTdateTime()
            dismiss()
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
        //formatter = SimpleDateFormat("h:mm a")
        formatter = SimpleDateFormat("h:mm")
        return formatter.format(tme)
    }

    fun setTdateTime() {
        val year = selectedDateDp!!.year
        val month = if(selectedDateDp!!.month > 0) selectedDateDp!!.month + 1 else 0
        val day = selectedDateDp!!.dayOfMonth
        dateTimeContext?.setDate(year, month, day)

        val hour = selectedTimeTp?.currentHour ?: 0
        val min = selectedTimeTp?.currentMinute ?: 0
        val time = getTime(hour, min)
        dateTimeContext?.setTime(time)
    }

    interface DateTimeSetter{
        var indx: Int
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
