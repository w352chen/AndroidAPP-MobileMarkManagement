package com.example.a4_basic.model

class Model {

    private var courseList = mutableListOf<Course>() // all courses

    fun getCourseList() : MutableList<Course> {
        return courseList
    }

    fun getCourse(courseCode : String) : Course? {
        for (course in courseList) {
            if (course.courseCode == courseCode) {
                return course
            }
        }
        return null
    }


//    fun addCourse(courseCode : String, courseDescription : String, term : String, markStr : String) {
//        val mark = validateCourseData(courseCode.uppercase(), term, markStr.uppercase())
//        courseList.add(Course(courseCode.uppercase(), courseDescription, term, mark, mark == -1))
////        refreshDisplayCourseList()
////        updateAllViews()
//    }

    fun addCourse(courseCode : String, courseDescription : String, term : String, markStr : String, hasWithdrawn : Boolean) {
        val mark = validateCourseData(courseCode.uppercase(), courseDescription, term, markStr, hasWithdrawn)
        println("Before: courseList.size = ${courseList.size}")
        courseList.add(Course(courseCode.uppercase(), courseDescription, term, mark, hasWithdrawn))
        println("After: courseList.size = ${courseList.size}")
//        refreshDisplayCourseList()
//        updateAllViews()
    }

    private fun validateCourseData(courseCode: String, courseDescription: String, term: String, markStr: String, hasWithdrawn: Boolean, forUpdate : Boolean = false) : Int {
//        println("markStr = $markStr")
        if (courseCode.trim().isEmpty()) {
            throw CourseException("Course code is mandatory!")
        } else if (courseDescription.trim().isEmpty()) {
            // Course description is not a mandatory field.
            // Uncomment the following line if it is mandatory
//            throw CourseException("Course description is mandatory!")
        } else if (term.trim().isEmpty()) {
            throw CourseException("Course term is mandatory!")
        }
        if (!forUpdate) {
            for (course in courseList) {
                if (course.courseCode == courseCode) {
                    throw CourseException("Course code ($courseCode) has already existed.")
                }
            }
        }
        if (hasWithdrawn) {
            return  -1 // mark = 1 if it was withdrawn
        } else {
            try {
                var mark = Integer.parseInt(markStr)
                if (mark !in 0..100) {
                    throw CourseException("Course mark must be an integer between 0 and 100, if it's not withdrawn!")
                }
                return mark
            } catch (e: NumberFormatException) {
                throw CourseException("Course mark must be an integer between 0 and 100, if it's not withdrawn!")
            }
        }
    }

//    private fun validateCourseData(courseCode: String, term: String, markStr: String, forUpdate : Boolean = false): Int {
//        println("markStr = $markStr")
//        if (courseCode.trim().isEmpty()) {
//            throw CourseException("Course code is mandatory!")
//        } else if (term.trim().isEmpty()) {
//            throw CourseException("Course term is mandatory!")
//        } else if (markStr.trim().isEmpty()) {
//            throw CourseException("Course mark must be an integer between 0 and 100, or WD!")
//        }
//        if (!forUpdate) {
//            for (course in courseList) {
//                if (course.courseCode == courseCode) {
//                    throw CourseException("Course code ($courseCode) has already existed.")
//                }
//            }
//        }
//        val mark : Int
//        if (markStr == "WD") {
//            mark = -1
//        } else {
//            try{
//                mark = Integer.parseInt(markStr)
//                if (mark < 0 || mark > 100) {
//                    throw CourseException("Course mark must be an integer between 0 and 100, or WD!")
//                }
//            } catch (e: NumberFormatException) {
//                throw CourseException("Course mark must be an integer between 0 and 100, or WD!")
//            }
//        }
//        return mark
//    }

    fun removeCourse(courseCode: String?) {
        for (course in courseList) {
            if (course.courseCode == courseCode) {
                courseList.remove(course)
//                refreshDisplayCourseList()
//                updateAllViews()
                break
            }
        }
    }

    fun updateCourse(courseCode: String, courseDescription : String, term : String, markStr: String, hasWithdrawn: Boolean) {
        for (course in courseList) {
            if (course.courseCode == courseCode) {
                val mark = validateCourseData(courseCode, courseDescription, term, markStr, hasWithdrawn, true)
                course.courseDescription = courseDescription
                course.term = term
                course.mark = mark
                course.hasWithdrawn = hasWithdrawn
//                refreshDisplayCourseList()
                break
            }
        }
    }

}