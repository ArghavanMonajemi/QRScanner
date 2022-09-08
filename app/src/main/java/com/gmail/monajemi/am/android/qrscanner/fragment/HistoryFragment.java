package com.gmail.monajemi.am.android.qrscanner.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.monajemi.am.android.qrscanner.R;
import com.gmail.monajemi.am.android.qrscanner.Validation;
import com.gmail.monajemi.am.android.qrscanner.adapter.HistoryAdapter;
import com.gmail.monajemi.am.android.qrscanner.database.SqliteDatabase;
import com.gmail.monajemi.am.android.qrscanner.model.History;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class HistoryFragment extends Fragment implements HistoryAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private boolean undo = false;

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
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                if (i == ItemTouchHelper.LEFT) {
                    int position = viewHolder.getAdapterPosition();
                    History history = adapter.getItemAt(position);
                    histories.remove(position);
                    adapter.notifyItemRemoved(position);
                    Snackbar.make(recyclerView, getString(R.string.item_removed), BaseTransientBottomBar.LENGTH_SHORT)
                            .setTextColor(ContextCompat.getColor(requireContext(),R.color.dark_grey))
                            .setAction(getString(R.string.undo), view1 -> {
                                undo = true;
                                histories.add(position, history);
                                adapter.notifyItemInserted(position);
                            }).setActionTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                            .addCallback(new Snackbar.Callback() {
                                @Override
                                public void onDismissed(Snackbar snackbar, int event) {
                                    if (!undo){
                                        database.deleteHistory(String.valueOf(history.getId()));
                                    }
                                }

                                @Override
                                public void onShown(Snackbar snackbar) {
                                }
                            })
                            .show();
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    Bitmap icon;
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;
                    Paint p = new Paint();
                    if (dX < 0) {
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        p.setColor(Color.BLACK);
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(requireActivity().getResources(), R.drawable.icons_remove);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
            }
        }).attachToRecyclerView(recyclerView);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onItemClick(String data) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity()).setTitle(getString(R.string.scanned_data))
                .setMessage(data).setNeutralButton(getString(R.string.ok), (dialogInterface, i) -> {

                });
        if (Validation.isValidURL(data))
            alertDialog.setPositiveButton(getString(R.string.open_browser), (dialogInterface, i) -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(data));
                startActivity(intent);
            });
        alertDialog.show();
    }
}