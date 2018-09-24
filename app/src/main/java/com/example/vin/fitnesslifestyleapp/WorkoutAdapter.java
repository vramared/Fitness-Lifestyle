package com.example.vin.fitnesslifestyleapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {

    private Context mContext;
    private List<Workout> workoutList;

    private WorkoutAdapter(Context mContext, List<Workout> workoutList) {
        this.mContext = mContext;
        this.workoutList = workoutList;
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.cardview_layout, null);

        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {

        Workout workout = workoutList.get(position);
        holder.workoutName.setText(workout.getName());
        holder.numReps.setText(workout.getReps());
        holder.numSets.setText(workout.getSets());
        holder.workoutDate.setText(workout.getDate());

        int[] images = {R.drawable.workout1, R.drawable.workout2, R.drawable.workout3,
                R.drawable.workout4, R.drawable.workout5, R.drawable.workout6, R.drawable.workout7,
                R.drawable.workout8, R.drawable.workout9, R.drawable.workout10, R.drawable.workout11,
                R.drawable.workout12, R.drawable.workout13, R.drawable.workout14, R.drawable.workout15};

        holder.imageView.setImageResource(images[workout.getImgURL()]);

        final String wName = holder.workoutName.getText().toString();
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, wName + " Page opened", Toast.LENGTH_LONG).show();
                Intent infoPage = new Intent(mContext, WorkoutInfo.class);
                infoPage.putExtra("workout_name", wName);
                mContext.startActivity(infoPage);

            }
        });
    }

    @Override
    public int getItemCount() {
        return workoutList.size();
    }

    class WorkoutViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView workoutName, numSets, numReps, workoutDate;
        ConstraintLayout constraintLayout;

        private WorkoutViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            workoutName = itemView.findViewById(R.id.workout_name);
            numSets = itemView.findViewById(R.id.num_sets);
            numReps = itemView.findViewById(R.id.num_reps);
            workoutDate = itemView.findViewById(R.id.workout_date);
            constraintLayout = itemView.findViewById(R.id.constraint_layout);
        }
    }

}
