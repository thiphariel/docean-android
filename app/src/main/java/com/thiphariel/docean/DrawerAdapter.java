package com.thiphariel.docean;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

/**
 * Created by Thiphariel on 26/09/2015.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.DrawerViewHolder> {

    private LayoutInflater mInflater;
    private List<DrawerViewItem> mData = Collections.emptyList();
    private Context mContext;

    public DrawerAdapter(Context context, List<DrawerViewItem> data) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mData = data;
    }

    @Override
    public DrawerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.drawer_row, parent, false);
        DrawerViewHolder holder = new DrawerViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(DrawerViewHolder holder, int position) {
        DrawerViewItem current = mData.get(position);
        holder.mTitle.setText(current.getTitle());
        holder.mIcon.setImageResource(current.getResourceId());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class DrawerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mIcon;
        private TextView mTitle;

        public DrawerViewHolder(View itemView) {
            super(itemView);

            mIcon = (ImageView) itemView.findViewById(R.id.drawer_row_image);
            mTitle = (TextView) itemView.findViewById(R.id.drawer_row_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(mContext, "Blablabla : " + getPosition(), Toast.LENGTH_SHORT).show();
        }
    }
}
