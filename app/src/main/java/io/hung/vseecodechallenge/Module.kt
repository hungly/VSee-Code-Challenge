package io.hung.vseecodechallenge

import com.squareup.moshi.Moshi
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
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .baseUrl(BuildConfig.BASE_ENDPOINT)
            .build()
    }

    single<NewsService> { get<Retrofit>().create(NewsService::class.java) }

    factory<ItemListRepository> { ItemListRepositoryImpl(get()) }

    viewModel { ItemListViewModel(get()) }
}