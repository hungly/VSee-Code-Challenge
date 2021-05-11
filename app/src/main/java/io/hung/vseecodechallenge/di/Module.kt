package io.hung.vseecodechallenge.di

import android.icu.text.SimpleDateFormat
import androidx.room.Room
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import com.squareup.moshi.Moshi
import io.hung.vseecodechallenge.BuildConfig
import io.hung.vseecodechallenge.ItemListRepositoryImpl
import io.hung.vseecodechallenge.NewsRepository
import io.hung.vseecodechallenge.database.VSeeCodeChallengeDatabase
import io.hung.vseecodechallenge.network.NewsService
import io.hung.vseecodechallenge.news_detail.NewsDetailViewModel
import io.hung.vseecodechallenge.news_list.NewsListViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*

val module = module {

    single(named(DEP_DATE_FORMAT_INPUT)) {
        SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss",
            Locale.getDefault()
        )
    }

    single(named(DEP_DATE_FORMAT_OUTPUT)) {
        SimpleDateFormat(
            "HH:mm, d MMMM yyyy",
            Locale.getDefault()
        )
    }

    single<Moshi> { Moshi.Builder().build() }

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor {
                val request =
                    it.request().newBuilder().addHeader("X-Api-Key", BuildConfig.API_KEY).build()
                it.proceed(request)
            }
            .build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .client(get())
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .baseUrl(BuildConfig.BASE_ENDPOINT)
            .build()
    }

    single {
        Room.databaseBuilder(
            get(),
            VSeeCodeChallengeDatabase::class.java,
            "vsee-code-challenge"
        ).build()
    }

    single { get<VSeeCodeChallengeDatabase>().newsDao() }

    single<NewsService> { get<Retrofit>().create(NewsService::class.java) }

    factory<NewsRepository> { ItemListRepositoryImpl(get(), get()) }

    viewModel { NewsListViewModel(get()) }

    viewModel { NewsDetailViewModel() }
}