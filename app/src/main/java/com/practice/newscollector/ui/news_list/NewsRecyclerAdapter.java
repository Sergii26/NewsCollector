package com.practice.newscollector.ui.news_list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.practice.newscollector.R;
import com.practice.newscollector.model.dao.ArticleSchema;
import com.practice.newscollector.model.logger.Logger;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import rx.Subscription;


public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.NewsViewHolder> {

    private final List<ArticleSchema> articlesList = new ArrayList<>();

    private final PublishSubject<Integer> itemViewClickSubject = PublishSubject.create();

    public Observable<Integer> getClicksObservable() {
        return itemViewClickSubject;
    }

    public void setArticlesList(List<ArticleSchema> articlesList) {
        this.articlesList.clear();
        if(articlesList != null){
            this.articlesList.addAll(articlesList);
        }
        notifyDataSetChanged();
    }

    public List<ArticleSchema> getArticlesList() {
        return articlesList;
    }

    public long getLastArticleTime(){
        return articlesList.isEmpty() ? 0 : articlesList.get(articlesList.size() - 1).getPublishedAt();
    }

    public void addArticlesToList(List<ArticleSchema> newArticles) {
        if(newArticles != null){
            articlesList.addAll(newArticles);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
            holder.bindView(articlesList.get(position));
    }

    @Override
    public int getItemCount() {
        return articlesList.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull NewsViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.subscribe();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull NewsViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.dispose();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        TextView title;
        @BindView(R.id.tvSource)
        TextView source;
        @BindView(R.id.tvDate)
        TextView date;
        @BindView(R.id.ivImage)
        ImageView image;
        private View itemView;
        private Subscription subscription;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.itemView = itemView;

        }

        public void bindView(ArticleSchema article) {
            this.title.setText(article.getTitle());
            this.source.setText(article.getSource());
            this.date.setText(getBeautifulDate(article.getPublishedAtAsString()));
            Picasso.get().load(article.getUrlToImage()).into(image);
        }

        public void subscribe() {
            subscription = RxView.clicks(itemView)
                    .doOnNext(aVoid -> {
                        Logger.withTag("MyLog").log("NewsViewHolder on item click position: " + getAdapterPosition());
                        itemViewClickSubject.onNext(articlesList.get(getAdapterPosition()).getUniqueId());
                    }).subscribe();
        }

        public void dispose() {
            if (subscription != null) {
                subscription.unsubscribe();
            }
        }

        private String getBeautifulDate(String date) {
            return String.format("%s/%s/%s at %s", date.substring(0, 4), date.substring(5, 7), date.substring(8, 10), date.substring(11, 16));
        }
    }

}
