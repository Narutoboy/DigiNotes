package com.do_big.diginotes.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.Group;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.do_big.diginotes.R;
import com.do_big.diginotes.activity.SearchActivity;
import com.do_big.diginotes.model.Note;
import com.do_big.diginotes.utils.Utils;

import java.util.List;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private final List<Note> notesList;
    private final OnNoteItemClickListener onNoteItemClickListener;

    public SearchAdapter(List<Note> notesList, OnNoteItemClickListener onNoteItemClickListener) {
        this.notesList = notesList;
        this.onNoteItemClickListener = (OnNoteItemClickListener) onNoteItemClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_search_layout, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Note note = notesList.get(position);

        holder.mTitleText.setText("" + note.getNoteTitle());

        holder.mCreatedAtText.append(Utils.formateDate(note.createdAt));
        if(note.isFav){
            holder.mFavImage.setImageResource(android.R.drawable.star_big_on);
        }else {
            holder.mFavImage.setImageResource(android.R.drawable.star_big_off);
        }
        if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView mTitleText, mCreatedAtText,mEdit, mDelete;
        public ImageView mFavImage;
        private Group groupMoreOptions;
        OnNoteItemClickListener onNotesRowItemClick;

        public ViewHolder(View view) {
            super(view);
            mTitleText = view.findViewById(R.id.row_text_title);
            mCreatedAtText = view.findViewById(R.id.row_createdAt);
            groupMoreOptions = view.findViewById(R.id.group_row_options);
            mFavImage=view.findViewById(R.id.btn_fav);
            mEdit=view.findViewById(R.id.tv_edit);
            mDelete=view.findViewById(R.id.tv_delete);
            mEdit.setOnClickListener(this);
            mDelete.setOnClickListener(this);
            mFavImage.setOnClickListener(this);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            this.onNotesRowItemClick = onNoteItemClickListener;


            //   popText = (TextView) view.findViewById(R.id.pop);
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();


                Note currentNote = notesList.get(getAdapterPosition());
                Log.d("recyclerview Adapter", "onClick: item " + currentNote.getNoteTitle());
                onNotesRowItemClick.onNoteItemClick(getAdapterPosition(), currentNote,id);
            /*Intent intent = new Intent(ctx, Description.class);
            intent.putExtra("des", currentNote.getNoteTitle());
            this.ctx.startActivity(intent);*/






        }

        @Override
        public boolean onLongClick(View v) {

            groupMoreOptions.setVisibility(groupMoreOptions.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            return true;
        }
    }


}
