package com.example.mobile_dev_project.data.repository

import android.util.Log
import java.io.File
import javax.inject.Inject
import com.example.mobile_dev_project.data.dao.*
import com.example.mobile_dev_project.data.entity.*
import com.example.mobile_dev_project.data.BooksPaths

class ParsingRepository @Inject constructor(
    private val paths: BooksPaths,
    private val bookd: BookDao,
    private val chapterd: ChapterDao,
    private val contentd: ContentDao
){
}