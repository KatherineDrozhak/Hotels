package com.example.katrindrozhak.hotels.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.katrindrozhak.hotels.R;
import com.example.katrindrozhak.hotels.model.Hotel;
import com.example.katrindrozhak.hotels.extensions.SpannableText;

import java.util.ArrayList;
import java.util.List;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.ViewHolder> {

    private List<Hotel> hotelList;
    private Context context;
    private OnItemClicked onClick;

    public HotelAdapter(Context context) {
        super();
        this.context = context;
        hotelList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Hotel hotel = hotelList.get(position);

        holder.name.setText(hotel.getName());
        holder.address.setText(makeField(R.string.string_address, hotel.getAddress()));
        holder.stars.setText(makeField(R.string.string_stars, hotel.getStars().toString()));
        holder.distance.setText(makeField(R.string.string_distance, hotel.getDistance().toString()));
        holder.suitesAvailability.setText(makeField(R.string.string_suites_av, hotel.getAvailableRoomsInfo()));

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onItemClick(position);
            }
        });

    }

    public void setOnClick(OnItemClicked onClick) {
        this.onClick = onClick;
    }

    private Spannable makeField(int titleStringId, String fieldValue) {
        String title = context.getString(titleStringId);
        return SpannableText.setupBoldText(title + fieldValue, 0, title.length());
    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    public void addData(List<Hotel> hotels) {
        hotelList = hotels;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView address;
        private TextView stars;
        private TextView distance;
        private TextView suitesAvailability;
        private CardView mCardView;

        private ViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.cardView);
            name = (TextView) itemView.findViewById(R.id.name);
            address = (TextView) itemView.findViewById(R.id.address);
            stars = (TextView) itemView.findViewById(R.id.stars);
            distance = (TextView) itemView.findViewById(R.id.distance);
            suitesAvailability = (TextView) itemView.findViewById(R.id.suites_availability);
        }
    }
}
