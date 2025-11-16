package com.timmay.tarot.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.InputStream
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Named("deckStream")
    fun provideCardStream(@ApplicationContext context: Context): InputStream {
        return context.assets.open("deck.json")
    }

    @Provides
    @Named("spreadStream")
    fun provideSpreadStream(@ApplicationContext context: Context): InputStream {
        return context.assets.open("spreads.json")
    }
}
