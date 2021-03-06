package io.hung.vseecodechallenge.news_detail

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import io.hung.vseecodechallenge.databinding.FragmentNewsDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsDetailFragment : Fragment() {

    private val viewModel: NewsDetailViewModel by viewModel()

    private var _binding: FragmentNewsDetailBinding? = null
    private val binding get() = _binding!!

    private var url: String? = null
    private var title: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_NEWS_URL)) {
                url = it.getString(ARG_NEWS_URL)
            }
            if (it.containsKey(ARG_NEWS_TITLE)) {
                title = it.getString(ARG_NEWS_TITLE)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsDetailBinding.inflate(inflater, container, false)

        binding.wvNews.webChromeClient = WebChromeClient()
        binding.wvNews.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                viewModel.isLoading(true)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                viewModel.isLoading(false)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        title?.let {
            (activity as AppCompatActivity?)?.supportActionBar?.title = it
        }

        url?.let {
            binding.wvNews.loadUrl(it.replace("http:", "https:"))
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.pbPageIsLoading.isVisible = it
        }
    }

    companion object {
        const val ARG_NEWS_URL = "news_url"
        const val ARG_NEWS_TITLE = "news_title"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}