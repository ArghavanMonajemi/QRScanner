package com.gmail.monajemi.am.android.qrscanner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.monajemi.am.android.qrscanner.R;
import com.gmail.monajemi.am.android.qrscanner.model.History;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    Context context;
    List<History> historyLisT;
    OnItemClickListener onItemClickListener;

    public HistoryAdapter(Context context, List<History> historyLisT) {
        this.context = context;
        this.historyLisT = historyLisT;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_item, viewGroup, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder historyViewHolder, int i) {
        History history = historyLisT.get(i);
        historyViewHolder.link.setText(history.getLink());
        historyViewHolder.date.setText(history.getDate());
    }

    @Override
    public int getItemCount() {
        return historyLisT.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {

        CardView item;
        TextView link, date;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.history_item);
            link = itemView.findViewById(R.id.history_item_link);
            date = itemView.findViewById(R.id.history_item_date);

            item.setOnClickListener(view -> onItemClickListener.onItemClick(link.getText().toString()));
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(String link);
    }
}
