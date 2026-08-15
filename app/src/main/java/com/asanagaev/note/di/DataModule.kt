package com.asanagaev.note.di

import android.content.Context
import androidx.room.Room
import com.asanagaev.note.data.NotesDao
import com.asanagaev.note.data.NotesDatabase
import com.asanagaev.note.data.NotesRepositoryImpl
import com.asanagaev.note.domain.NotesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindNotesRepository(
        impl: NotesRepositoryImpl
    ): NotesRepository

    companion object {

        @Singleton
        @Provides
        fun provideNotesDatabase(
            @ApplicationContext context: Context
        ): NotesDatabase {
            return Room.databaseBuilder(
                context = context,
                klass = NotesDatabase::class.java,
                name = "notes.db"
            ).build()
        }

        @Singleton
        @Provides
        fun provideNotesDao(
            database: NotesDatabase
        ): NotesDao {
            return database.notesDao()
        }
    }
}