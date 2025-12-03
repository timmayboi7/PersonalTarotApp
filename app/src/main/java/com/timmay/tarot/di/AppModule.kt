package com.timmay.tarot.di

import com.timmay.tarot.repo.CardStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    @Singleton
    fun provideCardStore(): CardStore = CardStore()
}
