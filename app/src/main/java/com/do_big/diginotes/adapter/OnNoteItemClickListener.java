package com.do_big.diginotes.adapter;

import com.do_big.diginotes.model.Note;

public interface OnNoteItemClickListener {
void onNoteItemClick(int adapterPosition, Note note, int viewId);
void onNoteLongItemClick(int adapterPosition, Note note);
void onNoteDeleteClick(Note note);

}
