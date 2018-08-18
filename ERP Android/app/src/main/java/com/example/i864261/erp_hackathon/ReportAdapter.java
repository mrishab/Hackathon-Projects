package com.example.i864261.erp_hackathon;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {
    private String[] mDataset;

    public static  Context ctx;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;

        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }

    public ReportAdapter(Context ctx, String[] myDataset) {
        mDataset = myDataset;
        this.ctx = ctx;
    }

    @Override
    public ReportAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
//        TextView v = (TextView) LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.adapter_report, parent, false);
        TextView v = new TextView(ReportAdapter.ctx);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mTextView.setText(mDataset[position]);

    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
