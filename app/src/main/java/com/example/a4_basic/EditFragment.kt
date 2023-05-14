package com.example.a4_basic

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.a4_basic.model.CourseException
import com.example.a4_basic.model.MarkManagementViewModel

private const val ARG_PARAM_COURSE_CODE = "COURSE_CODE"

class EditFragment : Fragment() {
    private var courseCode: String? = null

    private lateinit var viewModel: MarkManagementViewModel
    private val termOptions = arrayListOf("F19", "W20", "S20", "F20", "W21", "S21", "F21", "W22", "S22", "F22", "W23")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            courseCode = it.getString(ARG_PARAM_COURSE_CODE)
        }
        viewModel = ViewModelProvider(this)[MarkManagementViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_edit, container, false)

        // Retrieve view model
        viewModel = ViewModelProvider(requireActivity())[MarkManagementViewModel::class.java]
        val course = if (courseCode == null) null else viewModel.getCourse(courseCode!!)
        if (course != null) {
            // Populate course code
            val courseCodeTextView = root.findViewById<TextView>(R.id.textView_edit_course_code)
            courseCodeTextView.text = courseCode

            // Populate course description
            val courseDescriptionEditText = root.findViewById<EditText>(R.id.editText_course_description)
            courseDescriptionEditText.setText(course.courseDescription)

            // Populate mark
            val markEditTextNumber = root.findViewById<EditText>(R.id.editTextNumber_mark)
            markEditTextNumber.setText(course.getMark())

            // Populate WD'ed
            var mark = ""
            val wdSwitch = root.findViewById<Switch>(R.id.switch_wd)
            wdSwitch.setOnClickListener {
                if (wdSwitch.isChecked) {
                    mark = markEditTextNumber.text.toString()
                    markEditTextNumber.setText("")
                    markEditTextNumber.isEnabled = false
                } else {
                    markEditTextNumber.setText(mark)
                    markEditTextNumber.isEnabled = true
                }
            }
            wdSwitch.isChecked = course.hasWithdrawn

            // Populate term spinner
            val termSpinner = root.findViewById<Spinner>(R.id.spinner_term)
            val termOptionsAdapter = ArrayAdapter(
                this.requireContext(),
                android.R.layout.simple_spinner_item,
                termOptions
            )
            termOptionsAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
            )
            termSpinner.adapter = termOptionsAdapter

            termSpinner.setSelection(1)
            val termNo = termOptions.withIndex().first {
                it.value == course.term
            }
            termSpinner.setSelection(termNo.index, true)

            // Submit button
            val submitButton = root.findViewById<Button>(R.id.button_submit)
            submitButton.setOnClickListener {
                try {
                    viewModel.updateCourse(
                        courseCode!!,
                        courseDescriptionEditText.text.toString(),
                        termSpinner.selectedItem.toString(),
                        markEditTextNumber.text.toString(),
                        wdSwitch.isChecked
                    )
                    findNavController().navigate(R.id.action_editFragment_pop)
                } catch (e: CourseException) {
                    val alertBuilder: AlertDialog.Builder? = activity?.let {
                        AlertDialog.Builder(it)
                    }
                    alertBuilder?.setMessage(e.message)?.setTitle(R.string.alert_title_course_data)
                    val alertBox: AlertDialog? = alertBuilder?.create()
                    alertBox?.show()
                }
            }

            // Cancel button
            val cancelButton = root.findViewById<Button>(R.id.button_cancel)
            cancelButton.setOnClickListener {
                findNavController().navigate(R.id.action_editFragment_pop)
            }

        }

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance(courseCode: String) =
            EditFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM_COURSE_CODE, courseCode)
                }
            }
    }
}