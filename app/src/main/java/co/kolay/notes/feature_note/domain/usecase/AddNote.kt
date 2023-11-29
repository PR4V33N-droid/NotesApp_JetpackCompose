package co.kolay.notes.feature_note.domain.usecase

import co.kolay.notes.feature_note.domain.model.InvalidNoteException
import co.kolay.notes.feature_note.domain.model.Note
import co.kolay.notes.feature_note.domain.repository.NoteRepository
import kotlin.jvm.Throws

class AddNote(
    private val repository: NoteRepository
) {

    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note){
        if(note.title.isBlank()){
            throw InvalidNoteException("The title of the note cannot be empty.")
        }
        if(note.content.isBlank()){
            throw InvalidNoteException("The content of the note cannot be empty.")
        }
        repository.insertNote(note)
    }
}