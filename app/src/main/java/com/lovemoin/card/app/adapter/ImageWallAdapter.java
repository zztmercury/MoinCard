package com.lovemoin.card.app.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lovemoin.card.app.R;
import com.lovemoin.card.app.constant.Config;
import com.lovemoin.card.app.db.ImageInfo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by zzt on 15-9-14.
 */
public class ImageWallAdapter extends RecyclerView.Adapter<ImageWallAdapter.ViewHolder> {
    private Context context;
    private List<ImageInfo> imageList;

    public ImageWallAdapter(Context context) {
        this.context = context;
        imageList = new ArrayList<>();
    }

    public void add(ImageInfo data) {
        imageList.add(data);
        notifyDataSetChanged();
    }

    public void addAll(Collection<ImageInfo> dataSet) {
        imageList.addAll(dataSet);
        notifyDataSetChanged();
    }

    public void clear() {
        imageList.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_with_desc, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageLoader loader = ImageLoader.getInstance();
        final ImageInfo imageInfo = imageList.get(position);
        ViewGroup.LayoutParams layoutParams = holder.img.getLayoutParams();
        layoutParams.height = imageInfo.getHeight();
        layoutParams.width = imageInfo.getWidth();
        holder.img.setLayoutParams(layoutParams);
        loader.displayImage(Config.SERVER_URL + imageInfo.getUrl(), holder.img, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                view.setBackgroundColor(imageInfo.getColor());
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        holder.textImgDesc.setText(imageInfo.getDesc());

    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView textImgDesc;

        public ViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
            textImgDesc = (TextView) itemView.findViewById(R.id.textImgDesc);
        }
    }
}
