package com.example.myecommercesystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myecommercesystem.activities.AcceptAppointmentActivity;
import com.example.myecommercesystem.activities.UpdateProductInfoActivity;

import java.util.ArrayList;

public class AppointmentsListAdapter extends RecyclerView.Adapter<AppointmentsListAdapter.MyViewHolder> {
    public ArrayList<AppointmentsListModel> childModelArrayList;
    Context cxt;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView Name;
        public TextView date;
        public TextView location;
        public TextView status;
        public  View viewItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.name_appoint);
            date = itemView.findViewById(R.id.dateTime_appoint);
            location = itemView.findViewById(R.id.location_appoint);
            status = itemView.findViewById(R.id.status_appoint);
            viewItem=itemView;

        }
    }

    public AppointmentsListAdapter(ArrayList<AppointmentsListModel> arrayList, Context mContext) {
        this.cxt = mContext;
        this.childModelArrayList = arrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointments_list_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AppointmentsListModel currentItem = childModelArrayList.get(position);
        holder.Name.setText(currentItem.getName());
        holder.date.setText(currentItem.getDate());
        holder.location.setText(currentItem.getLocation());
        holder.status.setText(currentItem.getValue());
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//// Set video url as data source
//        retriever.setDataSource(currentItem.getUrl(), new HashMap<String, String>());
//// Get frame at 2nd second as Bitmap image
//        Bitmap bitmap = retriever.getFrameAtTime(2000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
//// Display the Bitmap image in an ImageView
//        holder.videoImage.setImageBitmap(bitmap);
        holder.viewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentItem.getValue().equals("Not Accepted Yet")) {
                    Intent intent = new Intent(cxt, AcceptAppointmentActivity.class);
                    intent.putExtra("name", currentItem.getName());
                    intent.putExtra("date", currentItem.getDate());
                    intent.putExtra("location", currentItem.getLocation());
                    intent.putExtra("push", currentItem.getItem_push_id());
                    cxt.startActivity(intent);
                    ((Activity) cxt).finish();
                }

                else {
                    Toast.makeText(cxt, "Already Accepted", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return childModelArrayList.size();
    }

}
