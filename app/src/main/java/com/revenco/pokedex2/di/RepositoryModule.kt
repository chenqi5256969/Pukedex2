package com.revenco.pokedex2.di

import com.revenco.pokedex2.PokedexApp
import com.revenco.pokedex2.network.PokedexClient
import com.revenco.pokedex2.persistence.PokemonDao
import com.revenco.pokedex2.persistence.PokemonInfoDao
import com.revenco.pokedex2.repository.DetailRepository
import com.revenco.pokedex2.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {

    @Provides
    @ActivityRetainedScoped
    fun provideMainRepository(client: PokedexClient, dao: PokemonDao): MainRepository {
        return MainRepository(client, dao)
    }

    @Provides
    @ActivityRetainedScoped
    fun provideDetailRepository(client: PokedexClient, infoDao: PokemonInfoDao): DetailRepository {
        return DetailRepository(client, infoDao)
    }
}