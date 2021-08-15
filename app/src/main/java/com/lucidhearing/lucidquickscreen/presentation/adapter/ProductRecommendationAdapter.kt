package com.lucidhearing.lucidquickscreen.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lucidhearing.lucidquickscreen.R
import com.lucidhearing.lucidquickscreen.data.model.dataModel.RetailerProduct
import com.lucidhearing.lucidquickscreen.databinding.ProductRecommendationListItemBinding

class ProductRecommendationAdapter:RecyclerView.Adapter<ProductRecommendationAdapter.ProductRecommendationViewHolder>() {

    var productRecommendations:List<RetailerProduct> = listOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductRecommendationViewHolder {
       val binding = ProductRecommendationListItemBinding.inflate(
           LayoutInflater.from(parent.context),
           parent,
           false
       )
        return ProductRecommendationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductRecommendationViewHolder, position: Int) {
        holder.bind(productRecommendations[position])
    }

    override fun getItemCount(): Int {
        return productRecommendations.size
    }

    inner class ProductRecommendationViewHolder(
        val binding:ProductRecommendationListItemBinding
    ):RecyclerView.ViewHolder(binding.root){
        fun bind(retailerProduct: RetailerProduct){
            binding.apply {
                imageProduct.setImageResource(retailerProduct.imageAssetResourceID)
            }
        }
    }
}