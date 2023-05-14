package com.example.a4_basic

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.a4_basic.model.CourseException
import com.example.a4_basic.model.MarkManagementViewModel

class AddFragment : Fragment() {
    companion object {
        fun newInstance() = AddFragment()
    }

    private lateinit var viewModel: MarkManagementViewModel
    private val termOptions = arrayListOf("F19", "W20", "S20", "F20", "W21", "S21", "F21", "W22", "S22", "F22", "W23")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MarkManagementViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_add, container, false)

        // Retrieve view model
        viewModel = ViewModelProvider(requireActivity())[MarkManagementViewModel::class.java]

        // Term spinner
        val courseTermSpinner = root.findViewById<Spinner>(R.id.spinner_term)

        val termOptionsAdapter = ArrayAdapter(
            this.requireContext(),
            android.R.layout.simple_spinner_item,
            termOptions
        )
        termOptionsAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        courseTermSpinner.adapter = termOptionsAdapter

        // WD'ed switch
        val markEditTextNumber = root.findViewById<EditText>(R.id.editTextNumber_mark)
        var mark = ""
        val withdrawnSwitch = root.findViewById<Switch>(R.id.switch_wd)
        withdrawnSwitch.setOnClickListener {
            if (withdrawnSwitch.isChecked) {
                mark = markEditTextNumber.text.toString()
                markEditTextNumber.setText("")
                markEditTextNumber.isEnabled = false
            } else {
                markEditTextNumber.setText(mark)
                markEditTextNumber.isEnabled = true
            }
            Log.d("WD", "withdrawn: ${withdrawnSwitch.isChecked}")
        }

        // Save button
        val createButton = root.findViewById<Button>(R.id.button_submit)
        createButton.setOnClickListener{
            val courseCode = root.findViewById<EditText>(R.id.editText_course_code).text.toString()
            val courseDescription = root.findViewById<EditText>(R.id.editText_course_description).text.toString()
            val courseMark = markEditTextNumber.text.toString()
            val courseTerm = courseTermSpinner.selectedItem.toString()
            val withdrawn = withdrawnSwitch.isChecked
            println("$courseCode, $courseDescription, $courseMark, $courseTerm, $withdrawn")
            try {
                viewModel.addCourse(courseCode, courseDescription, courseTerm, courseMark, withdrawn)
                findNavController().navigate(R.id.action_addFragment_pop)
            } catch (e : CourseException) {
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
            findNavController().navigate(R.id.action_addFragment_pop)
        }




        return root
    }

}