package com.zgsbrgr.soulsession.core.network.di

import com.zgsbrgr.soulsession.core.network.SoulSessionNetwork
import com.zgsbrgr.soulsession.core.network.api.RetrofitSoulSessionNetwork
import com.zgsbrgr.soulsession.core.network.api.SoulSessionNetworkApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {

    @Binds
    fun bindsSoulSessionNetwork(
        soulSessionNetwork: RetrofitSoulSessionNetwork
    ): SoulSessionNetwork

}