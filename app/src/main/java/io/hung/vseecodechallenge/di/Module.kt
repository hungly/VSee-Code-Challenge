package io.hung.vseecodechallenge

import androidx.room.Room
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import com.squareup.moshi.Moshi
import io.hung.vseecodechallenge.database.VSeeCodeChallengeDatabase
import io.hung.vseecodechallenge.network.NewsService
import io.hung.vseecodechallenge.news_list.NewsListViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val module = module {

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
}