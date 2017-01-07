package com.pkiykov.userpostspagersample.data.database;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DbModule {

    @Provides
    @Singleton
    DatabaseHelper provideOpenHelper(Application application) {
        return new DatabaseHelper(application);
    }

   /* @Provides
    @Singleton
    SqlBrite provideSqlBrite() {
        return new SqlBrite.Builder().build();
    }

    @Provides
    @Singleton
    BriteDatabase provideDatabase(SqlBrite sqlBrite, DatabaseHelper helper) {
        return sqlBrite.wrapDatabaseHelper(helper, Schedulers.io());
    }*/

}
