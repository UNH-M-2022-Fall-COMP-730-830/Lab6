package com.example.weather.main;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.R;
import com.example.weather.data.Forecast;

import java.util.ArrayList;
import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {

    private List<Forecast> data = new ArrayList<>();
    private OnForecastCLickListener onForecastClickListener = null;

    public void setData(List<Forecast> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void setForecastClickListener(OnForecastCLickListener onForecastClickListener) {
        this.onForecastClickListener = onForecastClickListener;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return 0;
        else return 1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutRes = R.layout.item_forecast;
        if (viewType == 0) {
            layoutRes = R.layout.item_forecast_primary;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Forecast item = data.get(position);
        holder.bind(item, position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView weatherIcon;
        private TextView date;
        private TextView weatherDescription;
        private TextView highTemperature;
        private TextView lowTemperature;

        public ViewHolder(@NonNull View view) {
            super(view);
            weatherIcon = view.findViewById(R.id.weather_icon);
            date = view.findViewById(R.id.date);
            weatherDescription = view.findViewById(R.id.weather_description);
            highTemperature = view.findViewById(R.id.high_temperature);
            lowTemperature = view.findViewById(R.id.low_temperature);
        }

        public void bind(Forecast item, int position) {
            // Weather Icon
            Resources resources = itemView.getResources();
            int iconId = resources.getIdentifier(item.getIcon(), "drawable", itemView.getContext().getPackageName());
            weatherIcon.setImageResource(iconId);

            // Date String
            String dateString = item.getDateString("EE, MMM dd");
            if (position == 0) {
                dateString = item.getDateString("'Today', MMM dd");
            }
            date.setText(dateString);

            // Weather Description
            weatherDescription.setText(item.getDescription());

            // High Temp
            highTemperature.setText(item.getHighTemp() + "\u00b0");

            // Low Temp
            lowTemperature.setText(item.getLowTemp() + "\u00b0");

            // OnClickListener
            itemView.setOnClickListener(v -> {
                if (onForecastClickListener != null) {
                    onForecastClickListener.onForecastClick(item);
                }
            });
        }
    }
}
