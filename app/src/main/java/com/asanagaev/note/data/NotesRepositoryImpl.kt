package com.asanagaev.note.data

import com.asanagaev.note.domain.ContentItem
import com.asanagaev.note.domain.Note
import com.asanagaev.note.domain.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(
    private val notesDao: NotesDao,
    private val imageFIleManager: ImageFIleManager
) : NotesRepository {

    override suspend fun addNote(
        title: String,
        content: List<ContentItem>,
        isPinned: Boolean,
        updatedAt: Long
    ) {
        val processedContent = content.processForStorage()
        val noteDbModel = NoteDbModel(0, title, updatedAt, isPinned)
        val noteId = notesDao.addNote(noteDbModel).toInt()
        val contentItems = processedContent.toContentItemDbModels(noteId)
        notesDao.addNoteContent(contentItems)
    }

    override suspend fun deleteNote(noteId: Int) {
        val note = notesDao.getNote(noteId).toEntity()
        notesDao.deleteNote(noteId)

        note.content
            .filterIsInstance<ContentItem.Image>()
            .map { it.url }
            .forEach {
                imageFIleManager.deleteImage(it)
            }
    }

    override suspend fun editNote(note: Note) {
        val oldNote = notesDao.getNote(note.id).toEntity()

        val oldUrls = oldNote.content.filterIsInstance<ContentItem.Image>().map { it.url }
        val newUrls = note.content.filterIsInstance<ContentItem.Image>().map { it.url }
        val removedUrls = oldUrls - newUrls

        removedUrls.forEach {
            imageFIleManager.deleteImage(it)
        }

        val processedContent = note.content.processForStorage()
        val processedNote = note.copy(content = processedContent)

        notesDao.addNote(processedNote.toDBModel())
        notesDao.deleteNoteContent(note.id)
        notesDao.addNoteContent(processedContent.toContentItemDbModels(note.id))
    }

    override suspend fun getNote(noteId: Int): Note {
        return notesDao.getNote(noteId).toEntity()
    }

    override fun getAllNotes(): Flow<List<Note>> {
        return notesDao.getAllNotes().map { it.toEntities() }
    }

    override fun searchNotes(query: String): Flow<List<Note>> {
        return notesDao.searchNotes(query).map { it.toEntities() }
    }

    override suspend fun switchPinnedStatus(noteId: Int) {
        notesDao.switchPinnedStatus(noteId)
    }

    private suspend fun List<ContentItem>.processForStorage(): List<ContentItem> {
        return map { contentItem ->
            when (contentItem) {
                is ContentItem.Image -> {
                    if (imageFIleManager.isInternal(contentItem.url)) {
                        contentItem
                    } else {
                        val internalPath =
                            imageFIleManager.copyImageToInternalStorage(contentItem.url)
                        ContentItem.Image(internalPath)
                    }
                }

                is ContentItem.Text -> {
                    contentItem
                }
            }
        }
    }
}