package com.dicoding.myapplication

import android.app.Activity
import android.content.Intent
import com.google.android.material.floatingactionbutton.FloatingActionButton

fun setupFabMenu(
    activity: Activity,
    fabMain: FloatingActionButton,
    fabPendingRequests: FloatingActionButton,
    fabScheduleMeeting: FloatingActionButton,
    fabCoassSchedule: FloatingActionButton
) {
    var isFabOpen = false


    fun toggleFab() {
        if (isFabOpen) {
            fabPendingRequests.hide()
            fabScheduleMeeting.hide()
            fabCoassSchedule.hide()
        } else {
            fabPendingRequests.show()
            fabScheduleMeeting.show()
            fabCoassSchedule.show()
        }
        isFabOpen = !isFabOpen
    }

    fabMain.setOnClickListener { toggleFab() }

    fabPendingRequests.setOnClickListener {
        activity.startActivity(Intent(activity, PendingRequestsActivity::class.java))
    }

    fabScheduleMeeting.setOnClickListener {
        activity.startActivity(Intent(activity, ScheduleMeetingActivity::class.java))
    }

    fabCoassSchedule.setOnClickListener {
        activity.startActivity(Intent(activity, CoassScheduleActivity::class.java))
    }
}
