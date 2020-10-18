package com.tyhj.wallpaper.ui.adpter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tyhj.wallpaper.R;
import com.tyhj.wallpaper.ui.activity.GifActivity;
import com.tyhj.wallpaper.ui.activity.ShowImage;

import java.util.ArrayList;

import model.entity.WallPaper;
import util.Uiutil;

/**
 * Created by Tyhj on 2017/5/25.
 */

public class PaperAdapter extends RecyclerView.Adapter<PaperAdapter.PaperHolder> {

    ArrayList<WallPaper> papers;
    Context context;
    LayoutInflater inflater;

    public PaperAdapter(ArrayList<WallPaper> papers, Context context) {
        this.papers = papers;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public PaperHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_paper, parent, false);
        return new PaperHolder(view);
    }

    @Override
    public void onBindViewHolder(final PaperHolder holder, int position) {
        holder.tv_name.setText(papers.get(position).getName() + "");
        if (papers.get(holder.getPosition()).getId() >= 0) {
            //Picasso.with(context).load(papers.get(holder.getPosition()).getImage()).resize(100, 100).centerCrop().into(holder.iv_paper);
            Glide.with(context).load(papers.get(holder.getPosition()).getImage()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.iv_paper);
        } else {
            switch (papers.get(holder.getPosition()).getId()) {
                case -1:
                    Glide.with(context).load(R.mipmap.logo1).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.iv_paper);
                    break;
                case -2:
                    Glide.with(context).load(R.mipmap.ic_camera).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.iv_paper);
                    break;
                case -3:
                    Glide.with(context).load(R.mipmap.ic_bird).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.iv_paper);
                    break;
                case -4:
                    Glide.with(context).load(R.mipmap.ic_girl).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.iv_paper);
                    break;
            }
        }
        holder.iv_paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;

                if (papers.get(holder.getPosition()).getMv() == null || papers.get(holder.getPosition()).getMv().endsWith(".mp4"))
                    intent = new Intent(context, ShowImage.class);
                else
                    intent = new Intent(context, GifActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("wallPager", papers.get(holder.getPosition()));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return papers.size();
    }

    class PaperHolder extends RecyclerView.ViewHolder {
        ImageView iv_paper;
        TextView tv_name;

        public PaperHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            iv_paper = (ImageView) itemView.findViewById(R.id.iv_paper);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                iv_paper.setClipToOutline(true);
                iv_paper.setOutlineProvider(Uiutil.getOutline(false, 10, 12));
            }
        }
    }
}
