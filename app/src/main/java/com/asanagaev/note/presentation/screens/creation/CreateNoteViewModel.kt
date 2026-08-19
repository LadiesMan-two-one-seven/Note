package com.asanagaev.note.presentation.screens.creation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asanagaev.note.domain.AddNoteUseCase
import com.asanagaev.note.domain.ContentItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateNoteViewModel @Inject constructor(
    private val addNoteUseCase: AddNoteUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<EditNoteState>(EditNoteState.Creation())
    val state = _state.asStateFlow()

    fun processCommand(command: CreateNoteCommand) {
        when (command) {
            CreateNoteCommand.Back -> {
                _state.update { EditNoteState.Finished }
            }

            is CreateNoteCommand.InputContent -> {
                _state.update { previousState ->
                    if (previousState is EditNoteState.Creation) {
                        previousState.copy(
                            content = command.content,
                            isSaveEnable = previousState.title.isNotBlank() && command.content.isNotBlank()
                        )
                    } else {
                        EditNoteState.Creation(content = command.content)
                    }
                }
            }

            is CreateNoteCommand.InputTitle -> {
                _state.update { previousState ->
                    if (previousState is EditNoteState.Creation) {
                        previousState.copy(
                            title = command.title,
                            isSaveEnable = command.title.isNotBlank() && previousState.title.isNotBlank()
                        )
                    } else {
                        EditNoteState.Creation(title = command.title)
                    }
                }
            }

            CreateNoteCommand.Save -> {
                viewModelScope.launch {
                    _state.update { previousState ->
                        if (previousState is EditNoteState.Creation) {
                            val title = previousState.title
                            val content = ContentItem.Text(content = previousState.content)
                            addNoteUseCase(title, listOf(content))
                            EditNoteState.Finished
                        } else {
                            previousState
                        }
                    }
                }
            }
        }
    }
}

sealed interface CreateNoteCommand {

    data class InputTitle(val title: String) : CreateNoteCommand

    data class InputContent(val content: String) : CreateNoteCommand

    data object Save : CreateNoteCommand

    data object Back : CreateNoteCommand
}

sealed interface EditNoteState {

    data class Creation(
        val title: String = "",
        val content: String = "",
        val isSaveEnable: Boolean = false
    ) : EditNoteState

    data object Finished : EditNoteState
}