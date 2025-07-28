package com.kev.instantsystem

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kev.instantsystem.ui.components.LoaderScreen
import com.kev.instantsystem.ui.components.LoaderState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoaderScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun showsCircularProgressIndicator_whenLoading() {
        composeRule.setContent {
            LoaderScreen(state = LoaderState.Loading)
        }

        composeRule
            .onNodeWithTag("LoaderIndicator")
            .assertIsDisplayed()
    }
}
