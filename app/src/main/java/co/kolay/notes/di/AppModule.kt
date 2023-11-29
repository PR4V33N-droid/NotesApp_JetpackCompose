package co.kolay.notes.di

import android.app.Application
import androidx.room.Room
import co.kolay.notes.feature_note.data.data_source.NoteDatabase
import co.kolay.notes.feature_note.data.repository.NoteRepositoryImpl
import co.kolay.notes.feature_note.domain.repository.NoteRepository
import co.kolay.notes.feature_note.domain.usecase.AddNote
import co.kolay.notes.feature_note.domain.usecase.DeleteNote
import co.kolay.notes.feature_note.domain.usecase.GetNote
import co.kolay.notes.feature_note.domain.usecase.GetNotes
import co.kolay.notes.feature_note.domain.usecase.NoteUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(appContext: Application): NoteDatabase{
        return Room.databaseBuilder(
            appContext,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase): NoteRepository{
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases{
        return NoteUseCases(
            getNotes = GetNotes(repository),
            deleteNote = DeleteNote(repository),
            addNote = AddNote(repository),
            getNote = GetNote(repository)
        )
    }
}