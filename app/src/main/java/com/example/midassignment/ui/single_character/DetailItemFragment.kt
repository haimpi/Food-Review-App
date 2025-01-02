package com.example.midassignment.ui.single_character

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.midassignment.R
import com.example.midassignment.databinding.DetailItemLayoutBinding
import com.example.midassignment.ui.ItemsViewModel

class DetailItemFragment : Fragment(){

    private var _binding : DetailItemLayoutBinding? = null

    private val binding get() = _binding!!

    val viewModel : ItemsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = DetailItemLayoutBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.chosenItem.observe(viewLifecycleOwner){
            binding.itemRestaurantName.text = it.restaurantName
            binding.itemFoodName.text = it.foodName
            binding.itemDescription.text = it.description
            binding.itemRate.text = it.ratingRate.toString()
            Glide.with(requireContext()).load(it.photo).into(binding.itemImage)
        }

        binding.ftbEditDetails.setOnClickListener{
            findNavController().navigate(R.id.action_detailItemFragment_to_addItemFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}