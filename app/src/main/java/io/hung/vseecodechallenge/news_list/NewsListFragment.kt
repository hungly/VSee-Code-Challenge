package io.hung.vseecodechallenge.news_list

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import io.hung.vseecodechallenge.R
import io.hung.vseecodechallenge.databinding.FragmentNewsListBinding
import io.hung.vseecodechallenge.databinding.NewsListItemBinding
import io.hung.vseecodechallenge.di.DEP_DATE_FORMAT_INPUT
import io.hung.vseecodechallenge.di.DEP_DATE_FORMAT_OUTPUT
import io.hung.vseecodechallenge.model.News
import io.hung.vseecodechallenge.news_detail.NewsDetailFragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named

class NewsListFragment : Fragment() {

    private val viewModel: NewsListViewModel by viewModel()

    private var _binding: FragmentNewsListBinding? = null
    private val binding get() = _binding!!

    private val inputDateFormat: SimpleDateFormat by inject(named(DEP_DATE_FORMAT_INPUT))
    private val outputDateFormat: SimpleDateFormat by inject(named(DEP_DATE_FORMAT_OUTPUT))

    private lateinit var adapter: SimpleItemRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val swipeRefresh = binding.srlRefresh
        val recyclerView = binding.itemList
        val itemDetailFragmentContainer: View? = view.findViewById(R.id.item_detail_nav_container)

        val onClickListener = View.OnClickListener { itemView ->
            val item = itemView.tag as News
            val bundle = Bundle().apply {
                putString(NewsDetailFragment.ARG_NEWS_URL, item.url)
                putString(NewsDetailFragment.ARG_NEWS_TITLE, item.title)
            }
            if (itemDetailFragmentContainer != null) {
                itemDetailFragmentContainer.findNavController()
                    .navigate(R.id.fragment_item_detail, bundle)
            } else {
                itemView.findNavController().navigate(R.id.show_item_detail, bundle)
            }
        }

        binding.etSearch.addTextChangedListener {
            viewModel.search(it.toString())
        }

        setupRefresh(swipeRefresh)
        setupRecyclerView(recyclerView, onClickListener)
        setupObservers(swipeRefresh)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_item_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_refresh -> {
                viewModel.loadNews()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupRefresh(swipeRefresh: SwipeRefreshLayout) {
        swipeRefresh.setOnRefreshListener {
            viewModel.loadNews()
        }
    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        onClickListener: View.OnClickListener
    ) {
        adapter = SimpleItemRecyclerViewAdapter(
            arrayListOf(),
            onClickListener,
            inputDateFormat,
            outputDateFormat
        )
        recyclerView.adapter = adapter

        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            ).apply {
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.drawable_news_divider,
                    requireContext().theme
                )?.let {
                    setDrawable(it)
                }
            }
        )
    }

    private fun setupObservers(swipeRefresh: SwipeRefreshLayout) {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            swipeRefresh.isRefreshing = isLoading
        }

        viewModel.newsList.observe(viewLifecycleOwner) {
            adapter.updateNewsList(it)
        }

        viewModel.isError.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    class SimpleItemRecyclerViewAdapter(
        private val values: ArrayList<News>,
        private val onClickListener: View.OnClickListener,
        private val inputDateFormat: SimpleDateFormat,
        private val outputDateFormat: SimpleDateFormat
    ) : RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding =
                NewsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.tvTitle.text = item.title
            holder.tvDesc.text = item.description

            val date = inputDateFormat.parse(item.publishedAt)
            holder.tvTime.text = holder.itemView.context.getString(
                R.string.news_list_item_publish_date,
                outputDateFormat.format(date)
            )

            Glide.with(holder.itemView)
                .load(item.urlToImage)
                .placeholder(R.drawable.ic_image)
                .centerCrop()
                .into(holder.ivImage)

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        fun updateNewsList(newsList: List<News>) {
            val diffCallback = NewsDiffCallback(values, newsList)
            val result = DiffUtil.calculateDiff(diffCallback)

            values.clear()
            values.addAll(newsList)
            result.dispatchUpdatesTo(this)
        }

        inner class ViewHolder(binding: NewsListItemBinding) :
            RecyclerView.ViewHolder(binding.root) {
            val tvTitle: TextView = binding.tvTitle
            val tvDesc: TextView = binding.tvDesc
            val tvTime: TextView = binding.tvTime
            val ivImage: ImageView = binding.ivImage
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}