package com.timmay.tarot.di

import android.content.Context
import com.timmay.tarot.repo.CardStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideCardStore(@ApplicationContext context: Context): CardStore = CardStore.getInstance(context)
}
