package com.jonathandarwin.workmanagertest.util

/**
 * Created By : Jonathan Darwin on August 31, 2023
 */
object Database {

    private val list = mutableListOf<String>()

    var isForceError = true

    fun isQueueEmpty(): Boolean {
        return list.isEmpty()
    }

    fun popQueue() {
        list.removeAt(0)
    }

    fun getQueue(): String {
        return list[0]
    }

    fun putQueue(name: String) {
        list.add(name)
    }
}