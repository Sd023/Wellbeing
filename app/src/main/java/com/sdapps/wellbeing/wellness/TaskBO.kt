package com.sdapps.wellbeing.wellness

data class TaskBO (var taskId : Int = 0,
                   var question: String = "",
                   var isAnswered : Boolean = false
)