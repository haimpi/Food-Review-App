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
import androidx.core.view.isVisible
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

    private var isEditing: Boolean = false

    private var itemToEdit: Item? = null

    private val pickImageLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) {
                requireActivity().contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                imageUri = uri

                if (_binding != null) {
                    binding.resultImage.setImageURI(uri)
                    binding.resultImage.isVisible = true
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddItemLayoutBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemToEdit = viewModel.chosenItem.value
        itemToEdit?.let { item ->
            isEditing = true
            with(binding) {
                itemFoodName.setText(item.foodName)
                itemRestaurantName.setText(item.restaurantName)
                itemFoodDetails.setText(item.description)
                ratingBar.rating = item.ratingRate
                item.photo?.let {
                    resultImage.setImageURI(Uri.parse(it))
                    resultImage.isVisible = true}
            }
        }

        with(binding) {
            submitReviewBtn.setOnClickListener {
                val foodName = itemFoodName.text.toString().trim()
                val restaurantName = itemRestaurantName.text.toString().trim()
                val foodDetails = itemFoodDetails.text.toString().trim()

                val errorMessage = when {
                    foodName.isEmpty() -> getString(R.string.error_fill_food_name)
                    !foodName.matches(Regex("^[a-zA-Z\\s]+$")) -> getString(R.string.error_food_name_contain_only_letters)
                    foodName.length < 3 -> getString(R.string.food_name_not_enough_chars)
                    restaurantName.isEmpty() -> getString(R.string.error_fill_restaurant_name)
                    restaurantName.length < 3 -> getString(R.string.error_restaurant_name_not_enough_chars)
                    foodDetails.isEmpty() -> getString(R.string.error_need_fill_details_review)
                    foodDetails.length < 10 -> getString(R.string.error_details_not_enough_chars)
                    ratingBar.rating <= 0.0 -> getString(R.string.error_need_rate_rood)
                    else -> null
                }

                if (errorMessage != null) {
                    showToast(errorMessage)
                } else {
                    val item = Item(
                        foodName,
                        restaurantName,
                        foodDetails,
                        ratingBar.rating,
                        imageUri?.toString() ?: itemToEdit?.photo
                    )

                    if (isEditing) {
                        item.id = itemToEdit?.id ?: 0
                        viewModel.updateItem(item)
                    } else {
                        viewModel.addItem(item)
                    }

                    findNavController().navigate(R.id.action_addItemFragment_to_allItemsFragment)
                }
            }

            uploadImageBtn.setOnClickListener {
                pickImageLauncher.launch(arrayOf("image/*"))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showToast(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}