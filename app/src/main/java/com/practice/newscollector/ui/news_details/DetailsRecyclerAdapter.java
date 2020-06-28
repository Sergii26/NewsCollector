package com.practice.newscollector.ui.news_details;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.practice.newscollector.R;
import com.practice.newscollector.model.dao.ArticleSchema;
import com.practice.newscollector.model.logger.ILog;
import com.practice.newscollector.model.logger.Logger;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class DetailsRecyclerAdapter extends RecyclerView.Adapter<DetailsRecyclerAdapter.DetailsViewHolder>{

    private final ILog logger = Logger.withTag("MyLog");

    private List<ArticleSchema> articlesList;

    private final PublishSubject<Long> reachEndSubject = PublishSubject.create();

    public List<ArticleSchema> getArticlesList() {
        return articlesList;
    }

    public Observable<Long> getRichEndObservable(){
        return  reachEndSubject;
    }

    public void setNewsList(List<ArticleSchema> articlesList) {
        this.articlesList = articlesList;
    }

    public void addArticlesToList(List<ArticleSchema> newArticles){
        logger.log("DetailsRecyclerAdapter addArticlesToList()");
        articlesList.addAll(newArticles);
    }

    @NonNull
    @Override
    public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        logger.log("DetailsRecyclerAdapter onCreateViewHolder()");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_item, parent, false);
        return new DetailsRecyclerAdapter.DetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsViewHolder holder, int position) {
        logger.log("DetailsRecyclerAdapter onBindViewHolder()");
        if(articlesList.size() < 20 || position > articlesList.size() - 4){
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

    class DetailsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivArticleImage)
        ImageView image;
        @BindView(R.id.tvArticleSource)
        TextView source;
        @BindView(R.id.tvArticleDate)
        TextView date;
        @BindView(R.id.tvArticleTitle)
        TextView title;
        @BindView(R.id.tvArticleContent)
        TextView content;

        DetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindView(ArticleSchema article){
            Picasso.get().load(article.getUrlToImage()).into(image);
            source.setText(article.getSource());
            date.setText(getBeautifulDate(article.getPublishedAtAsString()));
            title.setText(article.getTitle());
            content.setText(article.getContent());
        }

        private String getBeautifulDate(String date){
            return String.format("%s/%s/%s at %s", date.substring(0, 4), date.substring(5, 7), date.substring(8, 10), date.substring(11, 16));
        }
    }
}
