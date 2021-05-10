package io.hung.vseecodechallenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import io.hung.vseecodechallenge.databinding.FragmentItemListBinding
import io.hung.vseecodechallenge.databinding.ItemListContentBinding

class ItemListFragment : Fragment() {

    private var _binding: FragmentItemListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentItemListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = binding.itemList
        val itemDetailFragmentContainer: View? = view.findViewById(R.id.item_detail_nav_container)

        val onClickListener = View.OnClickListener { itemView ->
            val item = itemView.tag as News
            val bundle = Bundle()
            bundle.putString(
                ItemDetailFragment.ARG_NEWS_URL,
                item.url
            )
            if (itemDetailFragmentContainer != null) {
                itemDetailFragmentContainer.findNavController()
                    .navigate(R.id.fragment_item_detail, bundle)
            } else {
                itemView.findNavController().navigate(R.id.show_item_detail, bundle)
            }
        }

        setupRecyclerView(recyclerView, onClickListener)
    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        onClickListener: View.OnClickListener
    ) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(
            arrayListOf(
                News(
                    "Monday's gossip column",
                    "Paris St-Germain still keen on Salah, Draxler to extend PSG stay, Bissouma set for Brighton departure, plus more.",
                    "http://www.bbc.co.uk/sport/57048382",
                    "https://ichef.bbci.co.uk/live-experience/cps/624/cpsprodpb/0B58/production/_118440920_salah_graphic.png",
                    "2021-05-09T22:37:35.495522Z"
                )
            ),
            onClickListener
        )
    }

    class SimpleItemRecyclerViewAdapter(
        private val values: List<News>,
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
            holder.tvDesc.text = item.desc
            holder.tvTime.text = item.time

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

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