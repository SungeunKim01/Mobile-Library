//website used for the mockk and for contructor and for anyConstructed https://mockk.io/

package com.example.mobile_dev_project.data.repository

import android.content.Context
import android.speech.tts.TextToSpeech
import com.example.mobile_dev_project.data.TtsState
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*


@OptIn(ExperimentalCoroutinesApi::class)
class TtsRepositoryTest {

    private val dispatcher = StandardTestDispatcher()

    private lateinit var context: Context
    private lateinit var repository: TtsRepository

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)


        context = mockk()
        mockkConstructor(TextToSpeech::class)

        every {
            anyConstructed<TextToSpeech>().setLanguage(any())
        } returns TextToSpeech.LANG_COUNTRY_AVAILABLE

        every {
            anyConstructed<TextToSpeech>().setSpeechRate(any())
        } returns TextToSpeech.SUCCESS

        every {
            anyConstructed<TextToSpeech>().setPitch(any())
        } returns TextToSpeech.SUCCESS

        every {
            anyConstructed<TextToSpeech>().stop()
        } returns TextToSpeech.SUCCESS

        repository = TtsRepository(context)
    }

    @Test
    fun initial_state_is_idle() = runTest {
        assertEquals(TtsState.Idle, repository.state.value)
    }

    @Test
    fun initial_rate_is_one_point_zero() = runTest {
        assertEquals(1.0f, repository.rate.value, 0.01f)
    }


    @Test
    fun stop_changes_state_to_stopped() = runTest {
        repository.stop()

        assertEquals(TtsState.Stopped, repository.state.value)
    }


    @Test
    fun pause_changes_state_to_paused() = runTest {
        repository.pause()

        assertTrue(repository.state.value is TtsState.Paused)
    }
}