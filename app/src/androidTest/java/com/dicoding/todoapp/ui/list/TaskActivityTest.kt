package com.dicoding.todoapp.ui.list

import android.content.ComponentName
import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.dicoding.todoapp.R
import com.dicoding.todoapp.ui.add.AddTaskActivity
import org.junit.After
import org.junit.Before

@RunWith(AndroidJUnit4::class)
class TaskActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(TaskActivity::class.java)

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun cleanup() {
        Intents.release()
    }

    @Test
    fun testAddTaskButton() {
        onView(withId(R.id.fab)).perform(click())

        Intents.intended(IntentMatchers.anyIntent())

        val expectedComponent = ComponentName(getTargetContext(), AddTaskActivity::class.java)
        Intents.intended(IntentMatchers.hasComponent(expectedComponent))
    }
}