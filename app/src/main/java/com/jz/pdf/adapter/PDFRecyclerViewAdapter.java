package com.jz.pdf.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jz.pdf.R;
import com.jz.pdf.aws.Constants;
import com.jz.pdf.base.App;
import com.jz.pdf.bean.PDFInfos;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by and2long on 16-8-28.
 */
public class PDFRecyclerViewAdapter extends RecyclerView.Adapter<PDFRecyclerViewAdapter.MyHolder> {


    List<PDFInfos> datas;
    LayoutInflater mInflater;
    int itemHeight;

    public PDFRecyclerViewAdapter(List<PDFInfos> datas, int itemHeight) {
        this.datas = datas;
        this.itemHeight = itemHeight;
        this.mInflater = LayoutInflater.from(App.appContext);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyHolder holder = new MyHolder(mInflater.inflate(R.layout.item_pdfinfo, parent, false));
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight);
        holder.layout.setLayoutParams(params);
        return holder;

    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        holder.desc.setText(datas.get(position).getPDFDescription());
        holder.time.setText(datas.get(position).getPDFDate());
        ImageLoader.getInstance().displayImage(Constants.S3_IMAGE_BUCKET + datas.get(position).getPDFImageName(), holder.image, App.imageOptions);
        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        ImageView image;
        TextView desc;
        TextView time;

        public MyHolder(View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView.findViewById(R.id.ll_item_layout);
            image = (ImageView) itemView.findViewById(R.id.iv_item_image);
            desc = (TextView) itemView.findViewById(R.id.tv_item_desc);
            time = (TextView) itemView.findViewById(R.id.tv_item_time);

        }
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}
