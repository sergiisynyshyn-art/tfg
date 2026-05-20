package com.example.serenum.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.serenum.R;
import com.example.serenum.models.HistoryEntry;

import java.util.List;

/**
 * RecyclerView adapter para mostrar historial con tarjetas visuales (YouTube-like).
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<HistoryEntry> entries;

    public HistoryAdapter(List<HistoryEntry> entries) {
        this.entries = entries;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HistoryViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_card, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoryEntry entry = entries.get(position);
        holder.imgThumb.setImageResource(entry.imageRes);
        holder.tvTitle.setText(entry.title);
        holder.tvType.setText(entry.type);
        holder.tvDate.setText(entry.date);
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumb;
        TextView tvTitle, tvType, tvDate;

        HistoryViewHolder(@NonNull android.view.View itemView) {
            super(itemView);
            imgThumb = itemView.findViewById(R.id.imgHistoryThumb);
            tvTitle = itemView.findViewById(R.id.tvHistoryCardTitle);
            tvType = itemView.findViewById(R.id.tvHistoryCardType);
            tvDate = itemView.findViewById(R.id.tvHistoryCardDate);
        }
    }
}

