package co.kolay.notes.feature_note.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kolay.notes.feature_note.domain.model.Note
import co.kolay.notes.feature_note.domain.usecase.NoteUseCases
import co.kolay.notes.feature_note.domain.util.NoteOrder
import co.kolay.notes.feature_note.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
): ViewModel() {

    private val _state = mutableStateOf(NoteState())
    val state: State<NoteState> = _state

    private var recentlyDeleteNote: Note? = null

    private var notesJob: Job? = null

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NoteEvent){
        when(event){
            is NoteEvent.Order -> {
                if(state.value.noteOrder::class == event.noteOrder::class &&
                        state.value.noteOrder.orderType == event.noteOrder.orderType) {
                    return
                }
                getNotes(event.noteOrder)
            }
            is NoteEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteUseCases.deleteNote(event.note)
                    recentlyDeleteNote = event.note
                }
            }
            is NoteEvent.RestoreNote -> {
                viewModelScope.launch{
                    noteUseCases.addNote(recentlyDeleteNote ?: return@launch)
                    recentlyDeleteNote = null
                }
            }
            is NoteEvent.ToggleOrderSection -> {
                _state.value = _state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }

    private fun getNotes(noteOrder: NoteOrder){
        notesJob?.cancel()
        notesJob = noteUseCases.getNotes(noteOrder)
            .onEach { notes ->
                _state.value = state.value.copy(
                    notes = notes,
                    noteOrder = noteOrder
                )
            }.launchIn(viewModelScope)
    }
}