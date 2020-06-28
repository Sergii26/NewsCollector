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

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import rx.functions.Func1;


public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.NewsViewHolder> {

    private List<ArticleSchema> articlesList;

    private final PublishSubject<Integer> itemViewClickSubject = PublishSubject.create();

    private final PublishSubject<Long> reachEndSubject = PublishSubject.create();

    public Observable<Integer> getClicksObservable(){
        return itemViewClickSubject;
    }

    public Observable<Long> getRichEndObservable() {
        return reachEndSubject;
    }

    public void setArticlesList(List<ArticleSchema> articlesList){
        this.articlesList = articlesList;
    }

    public List<ArticleSchema> getArticlesList(){
        return articlesList;
    }

    public void addArticlesToList(List<ArticleSchema> newArticles){
        articlesList.addAll(newArticles);
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        if(articlesList.size() >= 20 && position > articlesList.size() - 4){
            reachEndSubject.onNext(articlesList.get(articlesList.size() - 1).getPublishedAt());
        }
        holder.bindView(articlesList.get(position));
    }

    @Override
    public int getItemCount() {
        if(articlesList == null){
            return 0;
        }
        return articlesList.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tvTitle)
        TextView title;
        @BindView(R.id.tvSource)
        TextView source;
        @BindView(R.id.tvDate)
        TextView date;
        @BindView(R.id.ivImage)
        ImageView image;

        NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            RxView.clicks(itemView)
                    .map((Func1<Void, Integer>) aVoid -> {
                        Logger.withTag("MyLog").log("NewsViewHolder on item click position: " + getAdapterPosition());
                        itemViewClickSubject.onNext(articlesList.get(getAdapterPosition()).getUniqueId());
                        return null;
                    })
                    .subscribe();
        }

        public void bindView(ArticleSchema article){
            this.title.setText(article.getTitle());
            this.source.setText(article.getSource());
            this.date.setText(getBeautifulDate(article.getPublishedAtAsString()));
            Picasso.get().load(article.getUrlToImage()).into(image);
        }

        private String getBeautifulDate(String date){
            return String.format("%s/%s/%s at %s", date.substring(0, 4), date.substring(5, 7), date.substring(8, 10), date.substring(11, 16));
        }
    }

}
