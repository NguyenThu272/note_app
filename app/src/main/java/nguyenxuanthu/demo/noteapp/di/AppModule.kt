package nguyenxuanthu.demo.noteapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import nguyenxuanthu.demo.noteapp.database.NotesDatabase
import nguyenxuanthu.demo.noteapp.repository.NoteRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNotesDatabase(@ApplicationContext context: Context): NotesDatabase {
        return NotesDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideNoteRepository(database: NotesDatabase): NoteRepository {
        return NoteRepository.getRepository(database)
    }

}