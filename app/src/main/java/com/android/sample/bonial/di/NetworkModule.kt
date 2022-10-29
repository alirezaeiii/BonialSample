package com.android.sample.bonial.di

import com.android.sample.bonial.BuildConfig
import com.android.sample.bonial.network.ApiService
import com.android.sample.bonial.response.Content
import com.android.sample.bonial.response.ContentType
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import javax.inject.Singleton

/**
 * Main entry point for network access.
 */
@Module
class NetworkModule {

    /**
     * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
     * full Kotlin compatibility.
     */
    @Singleton
    @Provides
    fun provideMoshi() : Moshi = Moshi.Builder()
        .add(PolymorphicJsonAdapterFactory.of(Content::class.java, "contentType")
            .withSubtype(Content.NetworkBrochure::class.java, ContentType.brochure.name)
            .withSubtype(Content.BrochurePremium::class.java, ContentType.brochurePremium.name)
            .withSubtype(Content.CashbackOffersV2::class.java, ContentType.cashbackOffersV2.name)
            .withSubtype(Content.RateUs::class.java, ContentType.rateUs.name)
            .withSubtype(Content.SuperBannerCarousel::class.java, ContentType.superBannerCarousel.name)
            .withSubtype(Content.StoryCarousel::class.java, ContentType.storyCarousel.name)
            .withSubtype(Content.BlogCarousel::class.java, ContentType.blogCarousel.name)
            .withSubtype(Content.Referral::class.java, ContentType.referral.name))
        .add(KotlinJsonAdapterFactory())
        .build()

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor {
            Timber.d(it)
        }
        logger.level = HttpLoggingInterceptor.Level.BASIC

        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BONIAL_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)
}