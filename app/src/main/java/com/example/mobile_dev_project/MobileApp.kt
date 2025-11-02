package com.example.mobile_dev_project

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

//app entry for Hilt. required when using @HiltViewModel amd hiltViewModel()
@HiltAndroidApp
class MobileApp : Application()
