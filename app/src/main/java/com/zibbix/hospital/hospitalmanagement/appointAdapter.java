package com.zibbix.hospital.hospitalmanagement;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class appointAdapter extends RecyclerView.Adapter<appointAdapter.appointViewHolder>{

    private List<appoint> appointList;
    private Context context;

    private static int currentPosition = 0;

    appointAdapter(List<appoint> appointList, Context context) {
        this.appointList = appointList;
        this.context = context;
    }

    @Override
    public appointViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout_appoint, parent, false);
        return new appointViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final appointViewHolder holder, final int position) {
        appoint hero = appointList.get(position);
        holder.textViewDate.setText(hero.getDate());

        holder.textViewDoctor.setText(hero.getDoctor());


        holder.linearLayout.setVisibility(View.GONE);

        //if the position is equals to the item position which is to be expanded
        if (currentPosition == position) {
            //creating an animation
            Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.anim);

            //toggling visibility
            holder.linearLayout.setVisibility(View.VISIBLE);

            //adding sliding effect
            holder.linearLayout.startAnimation(slideDown);
        }

        holder.textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getting the position of the item to expand it
                currentPosition = position;

                //reloding the list
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointList.size();
    }

    class appointViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDate, textViewSession, textViewCounter, textViewDoctor;
        LinearLayout linearLayout;

        appointViewHolder(View itemView) {
            super(itemView);

            textViewDate = (TextView) itemView.findViewById(R.id.textViewDate);

            textViewDoctor = (TextView) itemView.findViewById(R.id.textViewDoctor);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        }
    }
}
