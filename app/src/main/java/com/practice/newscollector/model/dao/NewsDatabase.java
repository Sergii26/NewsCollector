package com.practice.newscollector.model.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ArticleSchema.class}, version = 3, exportSchema = false)
public abstract class NewsDatabase extends RoomDatabase implements NewsDao {
    public static final String DB_NAME = "news.db";

    public static NewsDatabase newInstance(Context context) {
        return Room.databaseBuilder(context, NewsDatabase.class, DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    public abstract NewsRoomDao newsDao();
}
