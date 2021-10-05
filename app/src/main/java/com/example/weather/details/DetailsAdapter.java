package com.example.weather.details;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.data.ForecastDetails;

import java.util.ArrayList;
import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder> {

    private List<ForecastDetails> data = new ArrayList<>();

    public void setData(List<ForecastDetails> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ForecastDetails item = data.get(position);
        holder.bind(item);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView value;

        public ViewHolder(@NonNull View view) {
            super(view);
            name = view.findViewById(R.id.name);
            value = view.findViewById(R.id.value);
        }

        public void bind(ForecastDetails details) {
            name.setText(details.getDescription());
            value.setText(details.getValue());
        }
    }
}
