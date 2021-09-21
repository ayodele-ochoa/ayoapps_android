package com.ayodeleochoa.ayoapps.models

interface TaskLoadedCallback {
    fun onTaskDone(vararg values: Any?)
}