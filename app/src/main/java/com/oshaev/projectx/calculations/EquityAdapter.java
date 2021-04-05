package com.oshaev.projectx.calculations;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oshaev.projectx.customobjects.Equity;
import com.oshaev.projectx.R;

import java.util.ArrayList;

public class EquityAdapter extends RecyclerView.Adapter<EquityAdapter.EquityViewHolder> {

    ArrayList<Equity> equities;
    OnEquityClickListener clickListener;


    public interface OnEquityClickListener{
        void onClick(int position);
    }

    public void setOnClickListener(OnEquityClickListener listener)
    {
        clickListener = listener;
    }

    public EquityAdapter(ArrayList<Equity> equities) {
        this.equities = equities;
    }

    @NonNull
    @Override
    public EquityAdapter.EquityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_instrument, parent, false);
        EquityViewHolder holder = new EquityViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull EquityAdapter.EquityViewHolder holder, int position) {
        holder.ticker.setText(equities.get(position).getCurrentInstruments().get(0).getTICKER());
        String closeValue = equities.get(position)
                .getCurrentInstruments().get(equities.get(position).getCurrentInstruments().size()-1)
                .getCLOSE().toString()+"₽";
        holder.close.setText(closeValue);
    }

    @Override
    public int getItemCount() {
        return equities.size();
    }

    public class EquityViewHolder extends RecyclerView.ViewHolder {

        TextView ticker;
        TextView close;
        TextView difference;

        public EquityViewHolder(@NonNull View itemView) {
            super(itemView);
            ticker = itemView.findViewById(R.id.instrumentTitleTextView);
            close = itemView.findViewById(R.id.instrumentValueTextView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(clickListener!=null)
                    {
                        if(pos!=RecyclerView.NO_POSITION)
                        {
                            clickListener.onClick(pos);
                        }
                    }
                }
            });
        }
    }
}
