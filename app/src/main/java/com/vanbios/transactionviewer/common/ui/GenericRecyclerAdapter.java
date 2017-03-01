package com.vanbios.transactionviewer.common.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vanbios.transactionviewer.R;

import java.util.List;

import static butterknife.ButterKnife.findById;

/**
 * @author Ihor Bilous
 */

public class GenericRecyclerAdapter<T> extends RecyclerView.Adapter<GenericRecyclerAdapter.GenericViewHolder> {

    protected List<T> list;
    protected Context context;


    public GenericRecyclerAdapter(Context context, List<T> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GenericViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(GenericViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    protected static class GenericViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle, tvSubTitle;
        public View view;

        GenericViewHolder(View view) {
            super(view);
            this.view = view;
            tvTitle = findById(view, R.id.tvItemListTitle);
            tvSubTitle = findById(view, R.id.tvItemListSubTitle);
        }
    }
}