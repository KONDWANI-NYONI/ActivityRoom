package com.example.activityroom;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> eventList;

    public EventAdapter(List<Event> eventList) {
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.bind(event);

        // Make item clickable to open details
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), EventDetailsActivity.class);
            intent.putExtra("event", event);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView textEventTitle, textEventDescription, textEventDate, textEventTime, textEventVenue;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            textEventTitle = itemView.findViewById(R.id.textEventTitle);
            textEventDescription = itemView.findViewById(R.id.textEventDescription);
            textEventDate = itemView.findViewById(R.id.textEventDate);
            textEventTime = itemView.findViewById(R.id.textEventTime);
            textEventVenue = itemView.findViewById(R.id.textEventVenue);
        }

        public void bind(Event event) {
            textEventTitle.setText(event.getTitle());
            textEventDescription.setText(event.getDescription());

            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            textEventDate.setText(dateFormat.format(event.getDate()));

            textEventTime.setText(event.getTime());
            textEventVenue.setText(event.getVenue());
        }
    }
}