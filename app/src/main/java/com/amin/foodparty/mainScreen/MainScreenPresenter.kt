package com.amin.foodparty.mainScreen

import com.amin.foodparty.model.FoodDC
import com.amin.foodparty.model.FoodDao

class MainScreenPresenter(private val foodDao: FoodDao) : MainScreenContract.Presenter {
    private  var mainView : MainScreenContract.View ?= null

    override fun onAttach(view: MainScreenContract.View) {
        mainView = view
        mainView?.showAllFoods(foodDao.getAllFoods())
    }
    override fun onDetach() {
        mainView = null
    }

    override fun onFirstRun() {
        val foodList = listOf<FoodDC>(
            FoodDC(
                title = "Hamburger",
                price = "15",
                distance = "3",
                place = "Isfahan, Iran",
                imageUrl = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food1.jpg",
                numOfRating = 20,
                rating = 4.5f
            ),
            FoodDC(
                title = "Grilled fish",
                price = "20",
                distance = "2.1",
                place = "Tehran, Iran",
                imageUrl = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food2.jpg",
                numOfRating = 10,
                rating = 4f
            ),
            FoodDC(
                title = "Lasania",
                price = "40",
                distance = "1.4",
                place = "Isfahan, Iran",
                imageUrl = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food3.jpg",
                numOfRating = 30,
                rating = 2f
            ),
            FoodDC(
                title = "pizza",
                price = "10",
                distance = "2.5",
                place = "Zahedan, Iran",
                imageUrl = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food4.jpg",
                numOfRating = 80,
                rating = 1.5f
            ),
            FoodDC(
                title = "Sushi",
                price = "20",
                distance = "3.2",
                place = "Mashhad, Iran",
                imageUrl = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food5.jpg",
                numOfRating = 200,
                rating = 3f
            ),
            FoodDC(
                title = "Roasted Fish",
                price = "40",
                distance = "3.7",
                place = "Jolfa, Iran",
                imageUrl = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food6.jpg",
                numOfRating = 50,
                rating = 3.5f
            ),
            FoodDC(
                title = "Fried chicken",
                price = "70",
                distance = "3.5",
                place = "NewYork, USA",
                imageUrl = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food7.jpg",
                numOfRating = 70,
                rating = 2.5f
            ),
            FoodDC(
                title = "Vegetable salad",
                price = "12",
                distance = "3.6",
                place = "Berlin, Germany",
                imageUrl = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food8.jpg",
                numOfRating = 40,
                rating = 4.5f
            ),
            FoodDC(
                title = "Grilled chicken",
                price = "10",
                distance = "3.7",
                place = "Beijing, China",
                imageUrl = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food9.jpg",
                numOfRating = 15,
                rating = 5f
            ),
            FoodDC(
                title = "Baryooni",
                price = "16",
                distance = "10",
                place = "Ilam, Iran",
                imageUrl = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food10.jpg",
                numOfRating = 28,
                rating = 4.5f
            ),
            FoodDC(
                title = "Ghorme Sabzi",
                price = "11.5",
                distance = "7.5",
                place = "Karaj, Iran",
                imageUrl = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food11.jpg",
                numOfRating = 27,
                rating = 5f
            ),
            FoodDC(
                title = "Rice",
                price = "12.5",
                distance = "2.4",
                place = "Shiraz, Iran",
                imageUrl = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food12.jpg",
                numOfRating = 35,
                rating = 2.5f
            ))
        foodDao.insertAllFoods(foodList)

    }

    override fun onSearchFoodClicked(filter: String) {

        if (filter.isNotEmpty()){
            //show filtered data
            mainView?.refreshAllFoods(foodDao.searchFood(filter))
        }else{
            mainView?.refreshAllFoods(foodDao.getAllFoods())
        }
    }

    override fun onAddNewFoodClicked(newFood: FoodDC) {
        foodDao.insertFood(newFood)
        mainView?.addNewFood(newFood)
    }

    override fun onDeleteAllClicked() {
        foodDao.deleteAllFoods()
        mainView?.refreshAllFoods(foodDao.getAllFoods())
    }

    override fun onUpdateFood(currentFood: FoodDC, pos: Int) {
        foodDao.updateFood(currentFood)
        mainView?.editFood(currentFood , pos)
    }

    override fun onDeleteFood(currentFood: FoodDC, pos: Int) {
        foodDao.deleteFood(currentFood)
        mainView?.deleteFood(currentFood , pos)
    }


}