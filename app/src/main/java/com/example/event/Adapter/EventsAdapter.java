package com.example.event.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.event.Database.Events;
import com.example.event.R;

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventsViewHolder> {

    List<Events> eventsList;

    public EventsAdapter(List<Events> eventsList) {
        this.eventsList = eventsList;
    }

    @NonNull
    @Override
    public EventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_view_layout, parent, false);
        return new EventsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsViewHolder holder, int position) {

        EventsViewHolder eventsViewHolder = (EventsViewHolder)holder;
        Events events = eventsList.get(position);
        eventsViewHolder.setName(events.getEvent_name());
        eventsViewHolder.setPrice("₱" + events.getEvent_price());
        eventsViewHolder.setDetails(events.getEvent_details());
        eventsViewHolder.setSchedule(events.getEvent_schedule());

        boolean isExpandable = eventsList.get(position).isExpandable();
        eventsViewHolder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    class EventsViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        RelativeLayout expandableLayout;

        public EventsViewHolder(@NonNull View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.linear_layout);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Events events = eventsList.get(getAdapterPosition());
                    events.setExpandable(!events.isExpandable());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }

        public void setName(String name){
            TextView event_name = (TextView) itemView.findViewById(R.id.event_name);
            event_name.setText(name);
        }

        public void setPrice(String price){
            TextView event_price = (TextView) itemView.findViewById(R.id.event_price);
            event_price.setText(price);
        }

        public void setDetails(String details){
            TextView event_details = (TextView) itemView.findViewById(R.id.event_details);
            event_details.setText(details);
        }

        public void setSchedule(String schedule){
            TextView event_schedule = (TextView) itemView.findViewById(R.id.event_schedule);
            event_schedule.setText(schedule);
        }
    }
}
