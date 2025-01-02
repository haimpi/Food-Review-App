package com.example.midassignment.ui.all_characters

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.midassignment.R
import com.example.midassignment.data.model.Item
import com.example.midassignment.databinding.AllItemsLayoutBinding
import com.example.midassignment.ui.ItemsViewModel

class AllItemsFragment : Fragment(){

    private var _binding : AllItemsLayoutBinding? = null

    private val binding get() = _binding!!

    private val viewModel : ItemsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = AllItemsLayoutBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)
        binding.fab.setOnClickListener{
            findNavController().navigate(R.id.action_allItemsFragment_to_addItemFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.items?.observe(viewLifecycleOwner){
            binding.recycler.adapter = ItemAdapter(it, object : ItemAdapter.ItemListener {

                override fun onItemClicked(index: Int) {
                    viewModel.setItem(it[index])
                    findNavController().navigate(R.id.action_allItemsFragment_to_detailItemFragment)
                }

                override fun onItemLongClicked(index: Int) {

                }
            })
            binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        }

        ItemTouchHelper(object : ItemTouchHelper.Callback(){
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) = makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Delete Confirm")
                    .setMessage("Are you sure you want to delete this specific item?")
                    .setPositiveButton("Yes"
                    ) { p0, p1 ->
                        val item = (binding.recycler.adapter as ItemAdapter).itemAt(viewHolder.adapterPosition)
                        viewModel.deleteItem(item)
                        Toast.makeText(requireContext(),"This item has been deleted successfully.",
                            Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("No"){ _,_ ->
                        (binding.recycler.adapter as ItemAdapter).notifyItemChanged(viewHolder.adapterPosition)
                    }
                    .setOnCancelListener {
                        (binding.recycler.adapter as ItemAdapter).notifyItemChanged(viewHolder.adapterPosition)
                    }.show()
            }
        }).attachToRecyclerView(binding.recycler)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_delete){
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete all items?")
                .setPositiveButton("Yes") { p0, p1 ->
                    viewModel.deleteAll()
                    Toast.makeText(requireContext(), "Deleted All", Toast.LENGTH_SHORT).show()
                }.show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}