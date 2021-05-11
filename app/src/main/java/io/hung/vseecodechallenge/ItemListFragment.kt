package io.hung.vseecodechallenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import io.hung.vseecodechallenge.databinding.FragmentItemListBinding
import io.hung.vseecodechallenge.databinding.ItemListContentBinding
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class ItemListFragment : Fragment() {

    private val viewModel: ItemListViewModel by viewModel()

    private var _binding: FragmentItemListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: SimpleItemRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemListBinding.inflate(inflater, container, false)
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
                putString(ItemDetailFragment.ARG_NEWS_URL, item.url)
                putString(ItemDetailFragment.ARG_NEWS_TITLE, item.title)
            }
            if (itemDetailFragmentContainer != null) {
                itemDetailFragmentContainer.findNavController()
                    .navigate(R.id.fragment_item_detail, bundle)
            } else {
                itemView.findNavController().navigate(R.id.show_item_detail, bundle)
            }
        }

        setupRefresh(swipeRefresh)
        setupRecyclerView(recyclerView, onClickListener)
        setupObservers(swipeRefresh)
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
            onClickListener
        )
        recyclerView.adapter = adapter
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
        private val onClickListener: View.OnClickListener
    ) : RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding =
                ItemListContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.tvTitle.text = item.title
            holder.tvDesc.text = item.description
            holder.tvTime.text = item.publishedAt

            Glide.with(holder.itemView)
                .load(item.urlToImage)
                .centerCrop()
                .into(holder.ivImage)

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        fun updateNewsList(newsList: List<News>) {
            values.clear()
            values.addAll(newsList)
            notifyDataSetChanged()
        }

        inner class ViewHolder(binding: ItemListContentBinding) :
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