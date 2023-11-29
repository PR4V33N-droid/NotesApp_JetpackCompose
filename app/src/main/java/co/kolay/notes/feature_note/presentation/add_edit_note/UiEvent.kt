package co.kolay.notes.feature_note.presentation.add_edit_note

sealed class UiEvent{
    data class ShowSnackBar(val message: String): UiEvent()
    object SaveNote: UiEvent()
}
