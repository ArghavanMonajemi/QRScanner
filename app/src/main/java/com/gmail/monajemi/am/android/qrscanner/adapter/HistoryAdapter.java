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
        historyViewHolder.data.setText(history.getData());
        historyViewHolder.date.setText(history.getDate());
    }

    @Override
    public int getItemCount() {
        return historyLisT.size();
    }

    public History getItemAt(int position){
        return historyLisT.get(position);
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {

        CardView item;
        TextView data, date;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.history_item);
            data = itemView.findViewById(R.id.history_item_data);
            date = itemView.findViewById(R.id.history_item_date);

            item.setOnClickListener(view -> onItemClickListener.onItemClick(data.getText().toString()));
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(String data);
    }
}
