package com.example.a4_basic

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.a4_basic.model.Course
import com.example.a4_basic.model.MarkManagementViewModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MarkManagementViewModel

    private val courseFiltersOptions = arrayListOf("All Courses", "CS Only","Math Only","Other Only")
    private val sortingOptions = arrayListOf(
        "By Course Code",   // (lexicographically)
        "By Term",          // (old to recent)
        "By Mark"           // (high to low, WD’ed courses last)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MarkManagementViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_main, container, false)

        // Retrieve view model
        viewModel = ViewModelProvider(requireActivity())[MarkManagementViewModel::class.java]

        val courseFilterSpinner = root.findViewById<Spinner>(R.id.spinner_filter)
        val filterOptionsAdapter = ArrayAdapter(
                this.requireContext(),
                android.R.layout.simple_spinner_item,
                courseFiltersOptions
            )
        filterOptionsAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        courseFilterSpinner.adapter = filterOptionsAdapter
        courseFilterSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d("Course Filter selected", "position = $position, id = $id")
                viewModel.setFilterOption(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d ("Course Filter selected","Nothing is selected.")
            }
        }

        val sortingSpinner = root.findViewById<Spinner>(R.id.spinner_sorting)
        val sortingOptionsAdapter = ArrayAdapter(
            this.requireContext(),
            android.R.layout.simple_spinner_item,
            sortingOptions
        )
        sortingOptionsAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        sortingSpinner.adapter = sortingOptionsAdapter
        sortingSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d("Sorting Option", "position = $position, id = $id")
                viewModel.setSortMethod(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
//                Log.d ("Sorting Option","Nothing is selected.")
            }
        }

        val addButton = root.findViewById<ImageButton>(R.id.imageButton_add)
        addButton.setOnClickListener {
            newCourse()
//            Log.d("Add Button", "Button is clicked.")
//            viewModel.addCourse(
//                "CS$courseIndex",
//                "CS$courseIndex - description",
//                "W19",
//                (60 + courseIndex).toString()
//            )
//            courseIndex ++
        }

        val courseListView = root.findViewById<LinearLayout>(R.id.courseItemCard)

        viewModel.displayCourseList.observe(viewLifecycleOwner) { courseList ->
            courseListView.removeAllViews()
            courseList.forEach { course ->
                val courseItemView = inflater.inflate(R.layout.course_item_card, null)
                val courseCodeTextView = courseItemView.findViewById<TextView>(R.id.textView_couse_code)
                val markTextView = courseItemView.findViewById<TextView>(R.id.textView_mark)
                val courseTermTextView = courseItemView.findViewById<TextView>(R.id.textView_course_term)
                val courseDescriptionTextView = courseItemView.findViewById<TextView>(R.id.textView_course_description)
                val editCourseButton = courseItemView.findViewById<ImageButton>(R.id.imageButton_edit)
                val deleteCourseButton = courseItemView.findViewById<ImageButton>(R.id.imageButton_delete)
                editCourseButton.setOnClickListener{
                    println("Edit course is clicked: ${course.courseCode}")
                    editCourse(course)
                }
                deleteCourseButton.setOnClickListener {
                    viewModel.removeCourse(course.courseCode)
                }
                courseCodeTextView.text = course.courseCode
                markTextView.text = course.getMarkWithWD()
                courseTermTextView.text = course.term
                courseDescriptionTextView.text = course.courseDescription
                courseItemView.setBackgroundColor(resources.getColor(
                    if (course.hasWithdrawn)
                        R.color.withdrawn_color
                    else if (course.mark < 50)
                        R.color.failed_color
                    else if (course.mark < 60)
                        R.color.pass_50_60_color
                    else if (course.mark < 91)
                        R.color.pass_60_91_color
                    else if (course.mark < 96)
                        R.color.pass_91_96_color
                    else
                        R.color.pass_96_100_color,
                    resources.newTheme()))
                courseListView.addView(courseItemView)
            }
        }
//        for (i in 1..20) {
//            val courseItemView = inflater.inflate(R.layout.course_item_card, null)
//            val courseCodeTextView = courseItemView.findViewById<TextView>(R.id.textView_couse_code)
//            val markTextView = courseItemView.findViewById<TextView>(R.id.textView_mark)
//            val courseTermTextView = courseItemView.findViewById<TextView>(R.id.textView_course_term)
//            val courseDescriptionTextView = courseItemView.findViewById<TextView>(R.id.textView_course_description)
//            courseCodeTextView.text = "CS$i"
//            markTextView.text = (60 + i).toString()
//            courseTermTextView.text = "W19"
//            courseDescriptionTextView.text = "CS$i - description"
//            courseListView.addView(courseItemView)
//        }

        // ---------------------------------------


        return root
    }

    private fun editCourse(course: Course) {
        findNavController().navigate(
            R.id.action_mainFragment_to_editFragment,
            bundleOf("COURSE_CODE" to course.courseCode)
        )
    }

    private fun newCourse() {
        findNavController().navigate(R.id.action_mainFragment_to_addFragment)
    }

}