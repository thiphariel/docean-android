package com.thiphariel.docean;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by Thiphariel on 27/09/2015.
 */
public class DropletsAdapter extends RecyclerView.Adapter<DropletsAdapter.DropletsViewHolder> {
    private LayoutInflater mInflater;
    private List<DropletsViewItem> mData = Collections.emptyList();
    private DropletsClickListener mDropletsClickListener;

    public DropletsAdapter(Context context, List<DropletsViewItem> data) {
        mInflater = LayoutInflater.from(context);
        mData = data;
    }

    @Override
    public DropletsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.droplet_row, parent, false);

        return new DropletsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DropletsViewHolder holder, int position) {
        DropletsViewItem current = mData.get(position);
        holder.name.setText(current.getName());
        holder.cpus.setText(current.getCpus() + " cpus");
        holder.size.setText(current.getSize() + " Gb");
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setDropletsClickListener(DropletsClickListener listener) {
        mDropletsClickListener = listener;
    }

    public class DropletsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name;
        private TextView cpus;
        private TextView size;

        public DropletsViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.droplet_title);
            cpus = (TextView) itemView.findViewById(R.id.droplet_cpu);
            size = (TextView) itemView.findViewById(R.id.droplet_size);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mDropletsClickListener != null) {
                mDropletsClickListener.click(v, getAdapterPosition());
            }
        }
    }

    public interface DropletsClickListener {
        void click(View view, int position);
    }
}
