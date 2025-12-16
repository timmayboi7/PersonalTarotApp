@file:Suppress("unused")
package com.timmay.tarot.di

import android.content.Context
import com.timmay.tarot.repo.CardStore
import com.timmay.tarot.repo.SpreadRepository
import com.timmay.tarot.repo.SpreadStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.io.InputStream
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @Named("deckStream")
    fun provideCardStream(@ApplicationContext context: Context): InputStream {
        return context.assets.open("deck.json")
    }

    @Provides
    @Singleton
    @Named("spreadsStream")
    fun provideSpreadStream(@ApplicationContext context: Context): InputStream {
        return context.assets.open("spreads.json")
    }

    @Provides
    @Singleton
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideCardStore(@Named("deckStream") stream: InputStream): CardStore = CardStore(stream)

    @Provides
    @Singleton
    fun provideSpreadStore(@Named("spreadsStream") stream: InputStream): SpreadStore = SpreadStore(stream)

    @Provides
    @Singleton
    fun provideSpreadRepository(spreadStore: SpreadStore): SpreadRepository = SpreadRepository(spreadStore)
}
