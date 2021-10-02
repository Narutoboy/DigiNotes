package com.do_big.diginotes.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.do_big.diginotes.R;
import com.do_big.diginotes.activity.Description;
import com.do_big.diginotes.data.Titles;

import java.util.ArrayList;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {
    //private List<Titles> titlesList;
    Context ctx;
    private ArrayList<Titles> titlesList = new ArrayList<Titles>();

    public SearchAdapter(ArrayList<Titles> titlesList, Context ctx) {
        this.titlesList = titlesList;
        this.ctx = ctx;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Titles t = titlesList.get(position);
        holder.mTitleText.setText(t.getTitle());

    }

    @Override
    public int getItemCount() {
        return titlesList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v, ctx, titlesList);
        return vh;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private final ArrayList<Titles> titlesList;
        public TextView mTitleText;
        Context ctx;


        public MyViewHolder(View view, Context ctx, ArrayList<Titles> titlesList) {
            super(view);
            this.titlesList = titlesList;
            this.ctx = ctx;
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            mTitleText = view.findViewById(R.id.texttitle);
            //   popText = (TextView) view.findViewById(R.id.pop);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Titles titlesList = this.titlesList.get(position);

            Intent intent = new Intent(ctx, Description.class);
            intent.putExtra("des", titlesList.getTitle());
            this.ctx.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View view) {
            Titles titlesList = this.titlesList.get(getAdapterPosition());
            //  Toast.makeText(ctx, titlesList.getTitle(), Toast.LENGTH_SHORT).show();

            return true;
        }
    }


}
