package ui.adpter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;
import com.tyhj.wallpaper.R;

import java.util.ArrayList;

import model.entity.WallPaper;
import ui.activity.ShowImage_;
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
        holder.tv_name.setText(papers.get(position).getName()+"");
        if(papers.get(holder.getPosition()).getId()>0) {
            Picasso.with(context).load(papers.get(holder.getPosition()).getImage()).resize(100,100).centerCrop().into(holder.iv_paper);
        }else {
            switch (papers.get(holder.getPosition()).getId()){
                case -1:
                    Picasso.with(context).load(R.mipmap.logo1).into(holder.iv_paper);
                    break;
                case -2:
                    Picasso.with(context).load(R.mipmap.ic_camera).into(holder.iv_paper);
                    break;
                case -3:
                    Picasso.with(context).load(R.mipmap.ic_bird).into(holder.iv_paper);
                    break;
                case -4:
                    Picasso.with(context).load(R.mipmap.ic_girl).into(holder.iv_paper);
                    break;
            }
        }
        holder.iv_paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowImage_.class);
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
        SimpleDraweeView iv_paper;
        TextView tv_name;
        public PaperHolder(View itemView) {
            super(itemView);
            tv_name= (TextView) itemView.findViewById(R.id.tv_name);
            iv_paper = (SimpleDraweeView) itemView.findViewById(R.id.iv_paper);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                iv_paper.setClipToOutline(true);
                iv_paper.setOutlineProvider(Uiutil.getOutline(false,10,12));
            }
        }
    }
}
