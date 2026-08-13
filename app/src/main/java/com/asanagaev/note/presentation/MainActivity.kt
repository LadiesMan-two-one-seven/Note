package com.asanagaev.note.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.asanagaev.note.presentation.screens.creation.CreateNoteScreen
import com.asanagaev.note.presentation.screens.notes.NotesScreen
import com.asanagaev.note.presentation.ui.theme.NoteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NoteTheme {
                CreateNoteScreen(
                    onFinished = {
                        Log.d("CreateNoteScreen", "onFinished")
                    }
                )
//                NotesScreen(
//                    onNoteClick = {
//                        Log.d("MainActivity", "onNoteClick: $it")
//                    },
//                    onAddNoteClick = {
//                        Log.d("MainActivity", "onAddNoteClick")
//                    }
//                )
            }
        }
    }
}