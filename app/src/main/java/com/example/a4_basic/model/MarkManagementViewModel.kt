package com.example.a4_basic.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MarkManagementViewModel : ViewModel() {
//    var testCount = 0;
    private val model = Model()
    var displayCourseList = MutableLiveData(mutableListOf<Course>())
    private var sortMethod : Int = 0 // 0: Course code, 1: Term, 2: Mark (desc)
    private var filterOption : Int = 0 // 0: All, 1: CS Courses, 2: Math Courses, 3: Others

    fun addCourse(courseCode : String, courseDescription : String, term : String, markStr : String, hasWithdrawn : Boolean) {
        println("addCourse(1): ${model.getCourseList().size}")
        model.addCourse(courseCode, courseDescription, term, markStr, hasWithdrawn)
        println("addCourse(1): ${model.getCourseList().size}")

        refreshDisplayCourseList()
//        testCount ++
//        println("testCount = $testCount")
    }
//    fun addCourse(course: Course) {
//        addCourse(course.courseCode, course.courseDescription, course.term, course.getMark())
//    }

    fun getCourse(courseCode : String) : Course? {
        return model.getCourse(courseCode)
    }

    fun updateCourse(courseCode: String, courseDescription : String, term : String, markStr: String, hasWithdrawn: Boolean) {
        println("courseCode = $courseCode, courseDescription = $courseDescription, term = $term, markStr = $markStr, hasWithdrawn = $hasWithdrawn")
        model.updateCourse(courseCode, courseDescription, term, markStr, hasWithdrawn)
    }

    fun removeCourse(courseCode: String?) {
        println("removeCourse(1): ${model.getCourseList().size}")
        model.removeCourse(courseCode)
        println("removeCourse(2): ${model.getCourseList().size}")
        refreshDisplayCourseList()
    }

    fun setFilterOption(filterOption : Int) {
        this.filterOption = filterOption
        refreshDisplayCourseList()
//        updateAllViews()
    }

//    fun setFilterOption(filterOption : String) {
//        setFilterOption(
//            when(filterOption) {
//                "All" -> 0
//                "CS Courses" -> 1
//                "Math Courses" -> 2
//                else -> 3
//            }
//        )
//    }

    fun setSortMethod(sortMethod : Int) {
        this.sortMethod = sortMethod
        println("sortMethod = $sortMethod")
        refreshDisplayCourseList()
//        updateAllViews()
    }
//    fun setSortMethod(sortMethod : String) {
//        setSortMethod(when(sortMethod) {
//            "Course code" -> 0
//            "Term" -> 1
//            "Mark (asc)" -> 2
//            else -> 3
//        })
//    }

    private fun refreshDisplayCourseList() {
//        var courseListLocal = displayCourseList.value
        // Filter
        val courseListLocal = model.getCourseList().filter{
            when(filterOption) {
                0 -> true
                1 -> it.isCSCourse()
                2 -> it.isMathCourse()
                else -> !it.isMathCourse() && !it.isCSCourse()
            }
        }.toMutableList()

        // Sort
        courseListLocal.sortWith { c1, c2 ->
            when (sortMethod) {
                0 -> c1.courseCode.compareTo(c2.courseCode)
                1 -> c1.getTermStart().compareTo(c2.getTermStart())
                2 -> c2.mark - c1.mark
                else -> c1.mark - c2.mark
            }
        }

//        displayCourseList.value?.addAll(courseListLocal)
        displayCourseList.value = courseListLocal
        println("courseListLocal.size = ${courseListLocal.size}")
        println("displayCourseList.value!!.size = ${displayCourseList.value!!.size}")
////        calculateCourseStats()
    }


}