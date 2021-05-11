package io.hung.vseecodechallenge.news_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import io.hung.vseecodechallenge.databinding.FragmentNewsDetailBinding


class NewsDetailFragment : Fragment() {

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