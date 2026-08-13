package com.asanagaev.note.data

import com.asanagaev.note.domain.Note

fun Note.toDBModel(): NoteDBModel {
    return NoteDBModel(id, title, content, updatedAt, isPinned)
}

fun NoteDBModel.toEntity(): Note {
    return Note(id, title, content, updatedAt, isPinned)
}