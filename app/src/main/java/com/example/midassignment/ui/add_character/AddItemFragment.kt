package com.example.midassignment.ui.add_character

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.midassignment.R
import com.example.midassignment.data.model.Item
import com.example.midassignment.databinding.AddItemLayoutBinding
import com.example.midassignment.ui.ItemsViewModel

class AddItemFragment : Fragment(){

    private var _binding : AddItemLayoutBinding? = null

    private val binding  get() = _binding!!

    private val viewModel : ItemsViewModel by activityViewModels()

    private var imageUri : Uri? = null

    val pickImageLauncher : ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.OpenDocument()){
            binding.resultImage.setImageURI(it)
            requireActivity().contentResolver.takePersistableUriPermission(it!!, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            imageUri = it
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = AddItemLayoutBinding.inflate(inflater, container, false)

        with(binding){
            submitReviewBtn.setOnClickListener {
                val errorMessage = when{
                    itemFoodName.text.isNullOrEmpty() -> "ERROR: You need to fill food name!"
                    itemRestaurantName.text.isNullOrEmpty() -> "ERROR: You need to fill the restaurant name!"
                    itemFoodDetails.text.isNullOrEmpty() -> "ERROR: You need to fill the details review!"
                    ratingBar.rating <= 0.0 -> "ERROR: You need to rate the food!"
                    else -> null
                }

                if(errorMessage != null){
                    showToast(errorMessage)
                }
                else{
                    findNavController().navigate(R.id.action_addItemFragment_to_allItemsFragment)

                    val item = Item(
                        itemFoodName.text.toString(),
                        itemRestaurantName.text.toString(),
                        itemFoodDetails.text.toString(),
                        ratingBar.rating,
                        imageUri.toString()
                    )

                    viewModel.addItem(item)
                }
            }

            uploadImageBtn.setOnClickListener{
                pickImageLauncher.launch(arrayOf("image/*"))
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showToast(message: String){
        Toast.makeText(requireContext(), "$message", Toast.LENGTH_SHORT).show()
    }
}