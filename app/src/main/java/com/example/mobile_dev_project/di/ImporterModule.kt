package com.example.mobile_dev_project.di

import com.example.mobile_dev_project.data.importer.BookImporter
import com.example.mobile_dev_project.data.importer.BookImporterContract
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ImporterModule {
    @Binds
    @Singleton
    abstract fun bindBookImporter(
        impl: BookImporter
    ): BookImporterContract
}
