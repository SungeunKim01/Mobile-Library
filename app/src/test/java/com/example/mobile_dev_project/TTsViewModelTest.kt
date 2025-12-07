package com.example.mobile_dev_project.ui.screens

import com.example.mobile_dev_project.data.TtsState
import com.example.mobile_dev_project.data.entity.Chapter
import com.example.mobile_dev_project.data.entity.Content
import com.example.mobile_dev_project.data.repository.ChapterRepository
import com.example.mobile_dev_project.data.repository.ContentRepository
import com.example.mobile_dev_project.data.repository.TtsRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TTsViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private lateinit var ttsRepo: TtsRepository
    private lateinit var chapterRepo: ChapterRepository
    private lateinit var contentRepo: ContentRepository

    private lateinit var viewModel: TTsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)

        ttsRepo = mockk(relaxed = true)
        chapterRepo = mockk()
        contentRepo = mockk()

        every { ttsRepo.state } returns MutableStateFlow(TtsState.Idle)

        viewModel = TTsViewModel(
            ttsRepository = ttsRepo,
            chapterRepository = chapterRepo,
            contentRepository = contentRepo
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun setChapter_loads_chapter_into_state() = runTest {
        val ch = Chapter().apply {
            chapterId = 1
            bookId = 10
            chapterName = "Test Chapter"
            chapterOrder = 5
            contentId = 99
        }

        coEvery { chapterRepo.getChapterById(1) } returns flowOf(ch)

        viewModel.setChapter(1)
        dispatcher.scheduler.advanceUntilIdle()

        val ui = viewModel.chapter.value!!
        assertEquals(1, ui.chapterId)
        assertEquals("Test Chapter", ui.chapterTitle)
        assertEquals(5, ui.chapterOrder)
        assertEquals(99, ui.contentId)
    }

    @Test
    fun setContent_loads_content_into_state() = runTest {
        val ch = Chapter().apply {
            chapterId = 2
            bookId = 10
            chapterName = "C2"
            chapterOrder = 2
            contentId = 200
        }
        coEvery { chapterRepo.getChapterById(2) } returns flowOf(ch)

        viewModel.setChapter(2)
        dispatcher.scheduler.advanceUntilIdle()

        val contentEntity = Content().apply {
            contentId = 200
            chapterId = 2
            content = "Hello world"
        }
        coEvery { contentRepo.getContentForChapter(2) } returns flowOf(contentEntity)

        viewModel.setContent()
        dispatcher.scheduler.advanceUntilIdle()

        val ui = viewModel.content.value!!
        assertEquals(200, ui.contentId)
        assertEquals("Hello world", ui.content)
    }

    @Test
    fun prepareTTs_calls_ttsRepository_with_correct_params() = runTest {
        val ch = Chapter().apply {
            chapterId = 3
            bookId = 11
            chapterName = "Chapter X"
            chapterOrder = 1
            contentId = 300
        }
        val ct = Content().apply {
            contentId = 300
            chapterId = 3
            content = "This is TTS text"
        }

        coEvery { chapterRepo.getChapterById(3) } returns flowOf(ch)
        coEvery { contentRepo.getContentForChapter(3) } returns flowOf(ct)
        coEvery { ttsRepo.prepare(any(), any()) } just Runs

        viewModel.prepareChapterById(3)
        dispatcher.scheduler.advanceUntilIdle()
        
        coVerify { ttsRepo.prepare(3, "This is TTS text") }
    }


    @Test
    fun prepareChapterById_loads_chapter_content_and_calls_prepare() = runTest {
        val ch = Chapter().apply {
            chapterId = 7
            bookId = 1
            chapterName = "Pipeline Test"
            chapterOrder = 1
            contentId = 700
        }
        val ct = Content().apply {
            contentId = 700
            chapterId = 7
            content = "Pipeline Content"
        }

        coEvery { chapterRepo.getChapterById(7) } returns flowOf(ch)
        coEvery { contentRepo.getContentForChapter(7) } returns flowOf(ct)
        coEvery { ttsRepo.prepare(any(), any()) } just Runs

        viewModel.prepareChapterById(7)
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(7, viewModel.chapter.value!!.chapterId)
        assertEquals(700, viewModel.content.value!!.contentId)

        coVerify { ttsRepo.prepare(7, "Pipeline Content") }
    }

    @Test
    fun play_pause_stop_forward_to_repository() {
        every { ttsRepo.play() } just Runs
        every { ttsRepo.pause() } just Runs
        every { ttsRepo.stop() } just Runs

        viewModel.playTTs()
        viewModel.pauseTTs()
        viewModel.stopTTs()

        verify { ttsRepo.play() }
        verify { ttsRepo.pause() }
        verify { ttsRepo.stop() }
    }
}
