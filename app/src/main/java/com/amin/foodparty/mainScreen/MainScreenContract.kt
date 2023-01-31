package com.amin.foodparty.mainScreen
import com.amin.foodparty.ext.BasePresenter
import com.amin.foodparty.ext.BaseView
import com.amin.foodparty.model.FoodDC

interface MainScreenContract {
    interface Presenter : BasePresenter<View>{
        fun onFirstRun()
        fun onSearchFoodClicked(filter:String)
        fun onAddNewFoodClicked(newFood : FoodDC)
        fun onDeleteAllClicked()
        fun onUpdateFood(currentFood: FoodDC , pos : Int)
        fun onDeleteFood(currentFood: FoodDC , pos : Int)
    }

    interface View: BaseView{
        fun showAllFoods(data: List<FoodDC>)
        fun refreshAllFoods(data: List<FoodDC>)
        fun addNewFood(newFood: FoodDC)
        fun deleteFood(oldFood: FoodDC , pos : Int)
        fun editFood(editingFood: FoodDC , pos : Int)
    }

}