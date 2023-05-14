package com.example.a4_basic.model

class Course (
    var courseCode : String,
    var courseDescription : String,
    var term : String,
    var mark : Int,
    var hasWithdrawn : Boolean) {

    fun getMark() : String {
        return if (hasWithdrawn) "" else mark.toString()
    }

    fun getMarkWithWD(): String {
        return if (hasWithdrawn) "WD" else mark.toString()
    }

        fun getTermStart() : String {
        return term.slice(1..2) +
                when(term.slice(0..0)) {
                    "W" -> "01"
                    "S" -> "05"
                    else -> "09"
                }
    }

    fun isCSCourse() : Boolean {
        return courseCode.startsWith("CS")
    }

    fun isMathCourse() : Boolean {
        return courseCode.startsWith("MATH") ||
                courseCode.startsWith("STAT") ||
                courseCode.startsWith("CO")
    }

}