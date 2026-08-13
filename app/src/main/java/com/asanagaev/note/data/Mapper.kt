package com.asanagaev.note.data

import com.asanagaev.note.domain.Note

fun Note.toDBModel(): NoteDbModel {
    return NoteDbModel(id, title, content, updatedAt, isPinned)
}

fun NoteDbModel.toEntity(): Note {
    return Note(id, title, content, updatedAt, isPinned)
}