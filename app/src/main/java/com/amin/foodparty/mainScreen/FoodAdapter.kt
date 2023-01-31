package com.amin.foodparty.mainScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amin.foodparty.databinding.RecyclerItemBinding
import com.amin.foodparty.model.FoodDC
import com.bumptech.glide.Glide
import java.util.ArrayList

class FoodAdapter(private val list: ArrayList<FoodDC>, private val foodEvent: FoodEvent) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {
    inner class FoodViewHolder(private val binding: RecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(position: Int) {
            binding.itemTvTitleRV.text = list[position].title
            binding.itemTvDistanceRV.text = list[position].distance + " miles from you"
            binding.itemTvPriceRV.text = "$" + list[position].price + " VIP"
            binding.itemTvPlaceRV.text = list[position].place
            binding.itemRatingTv.text = "("+list[position].numOfRating.toString() + " Rating)"
            binding.itemRatingRV.rating = list[position].rating
            Glide.with(binding.root.context)
                .load(list[position].imageUrl)
                .into(binding.itemImgRV)
            itemView.setOnLongClickListener {
                foodEvent.onFoodItemLongClicked(list[adapterPosition] , adapterPosition)
                true
            }
            itemView.setOnClickListener {
                foodEvent.onFoodItemClicked(list[adapterPosition] , adapterPosition)
            }
        }
    }
    fun addNewFood(newFood: FoodDC){
        list.add(0 , newFood)
        notifyItemInserted(0)
    }
    fun removeFood(oldFood: FoodDC, oldPosition: Int){
        list.remove(oldFood)
        notifyItemRemoved(oldPosition)
        notifyDataSetChanged()
    }
    fun updateFood(newFood : FoodDC, position: Int){
        list[position] = newFood
        notifyItemChanged(position)
    }
    fun setAllData(newList:ArrayList<FoodDC>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding = RecyclerItemBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return FoodViewHolder(binding)
    }
    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bindData(position)
    }
    override fun getItemCount(): Int {
        return list.size
    }
    interface  FoodEvent{
        fun onFoodItemClicked(foodDC: FoodDC, position: Int)
        fun onFoodItemLongClicked(foodDC: FoodDC, position: Int)
    }
}