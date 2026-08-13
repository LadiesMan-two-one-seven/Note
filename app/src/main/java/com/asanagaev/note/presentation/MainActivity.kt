package com.asanagaev.note.presentation

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.asanagaev.note.presentation.navigation.NavGraph
import com.asanagaev.note.presentation.ui.theme.NoteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // val context: Context = this.applicationContext
        enableEdgeToEdge()
        setContent {
            NoteTheme {
                NavGraph()
            }
        }
    }
}