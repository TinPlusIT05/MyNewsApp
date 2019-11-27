package com.tinphuc.mynews.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.tinphuc.mynews.Models.Article;
import com.tinphuc.mynews.Models.Utils;
import com.tinphuc.mynews.R;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.DataViewHolder> {

    private List<Article> articles;
    Context context;

    public ArticleAdapter(List<Article> mArticles, Context mContext){
        this.articles = mArticles;
        this.context = mContext;
    }

//  Phương thức onCreateViewHolder dùng để gán giao diện cho RecyclerView.ViewHolder
//  Phương thức này sẽ là chính tạo ra các item trên View thông qua ViewHolder
    @NonNull
    @Override
    public ArticleAdapter.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView;

//      Khởi tạo đối tượng LayoutInflater(giúp chuyển layout file(Xml) thành View(Java code) trong Android)
        LayoutInflater layoutInflater = LayoutInflater.from(context);

//      Sử dụng phương thức inflate để chuyển đổi 1 xml layout file thành 1 View trong java
        itemView = layoutInflater.inflate(R.layout.item, parent, false);

//      Trả về đối tượng Data View Holder với view như trên
        return new DataViewHolder(itemView);
    }

//    Phương thức onBindViewHolder dùng để gán dữ liệu từ listData vào viewHolder
    @Override
    public void onBindViewHolder(@NonNull final ArticleAdapter.DataViewHolder holder, int position) {
        Article article = articles.get(position);

//        Phương thức RequestOptions cung cấp các loại tùy chọn độc lọc để tùy chỉnh load ảnh với Glide
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(Utils.randomColorDrawable())
                .error(Utils.randomColorDrawable())
//                Cơ chế lưu bộ nhớ đệm với đối tượng requestOptions là DiskCacheStrategy.ALL
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop();

        Glide.with(context)
                .load(article.getUrlToImage())
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.pbLoadImage.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.pbLoadImage.setVisibility(View.GONE);
                        return false;
                    }
                })
//                Hiệu ứng chuyển ảnh mờ dần cho Drawable
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imgTitle);

        holder.txtTitle.setText(article.getTitle());
        holder.txtDesc.setText(article.getDescription());
        holder.txtSource.setText(article.getSource().getName());
        holder.txtPublishedAt.setText(Utils.DateFomat(article.getPublishedAt()));
        holder.txtAuthor.setText(article.getAuthor());
        holder.txtTime.setText("\u2022 " + Utils.DateToTimeFomat(article.getPublishedAt()));

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

//    class này giúp kiểm soát các view tốt hơn, tránh việc findViewById nhiều lần, trong file item.xml
    public class DataViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgTitle;
        private TextView txtTitle, txtTime, txtPublishedAt, txtAuthor, txtSource, txtDesc;
        private ProgressBar pbLoadImage;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            imgTitle = itemView.findViewById(R.id.imgTitle);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtAuthor = itemView.findViewById(R.id.txtAuthor);
            txtPublishedAt = itemView.findViewById(R.id.txtPublishedAt);
            txtSource = itemView.findViewById(R.id.txtSource);
            txtDesc = itemView.findViewById(R.id.txtDesc);
            pbLoadImage = itemView.findViewById(R.id.pbLoadImage);
        }
    }
}
