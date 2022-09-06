package com.gmail.monajemi.am.android.qrscanner.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.monajemi.am.android.qrscanner.R;
import com.gmail.monajemi.am.android.qrscanner.adapter.HistoryAdapter;
import com.gmail.monajemi.am.android.qrscanner.database.SqliteDatabase;
import com.gmail.monajemi.am.android.qrscanner.model.History;

import java.util.List;

public class HistoryFragment extends Fragment implements HistoryAdapter.OnItemClickListener {

    private RecyclerView recyclerView;

    public HistoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        recyclerView = view.findViewById(R.id.history_fragment_recycler);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SqliteDatabase database = new SqliteDatabase(getContext());
        List<History> histories = database.getAllHistory();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        HistoryAdapter adapter = new HistoryAdapter(getContext(), histories);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onItemClick(String link) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(intent);
    }
}