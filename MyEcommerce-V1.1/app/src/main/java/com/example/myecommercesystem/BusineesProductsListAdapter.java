package com.example.myecommercesystem;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myecommercesystem.activities.UpdateProductInfoActivity;

import java.util.ArrayList;

public class BusineesProductsListAdapter extends RecyclerView.Adapter<BusineesProductsListAdapter.MyViewHolder> {
    public ArrayList<BusinessProductsListModel> childModelArrayList;
    Context cxt;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView videoImage;
        public TextView videoName;
        public  View viewItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            videoImage = itemView.findViewById(R.id.book_image_main);
            videoName = itemView.findViewById(R.id.tv_book_name_main);
            viewItem=itemView;

        }
    }

    public BusineesProductsListAdapter(ArrayList<BusinessProductsListModel> arrayList, Context mContext) {
        this.cxt = mContext;
        this.childModelArrayList = arrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.business_products_list_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        BusinessProductsListModel currentItem = childModelArrayList.get(position);
        holder.videoName.setText(currentItem.getProduct_name());
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);



        Glide.with(cxt).load(currentItem.getImage_url()).apply(options).into(holder.videoImage);

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
                Intent intent = new Intent(cxt, UpdateProductInfoActivity.class);
                intent.putExtra("name",currentItem.getProduct_name());
                intent.putExtra("desc",currentItem.getProduct_desc());
                intent.putExtra("url",currentItem.getImage_url());
                intent.putExtra("push",currentItem.getItem_push_id());
                cxt.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return childModelArrayList.size();
    }

}
