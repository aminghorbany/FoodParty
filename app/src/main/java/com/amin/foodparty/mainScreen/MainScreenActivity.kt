package com.amin.foodparty.mainScreen

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.amin.foodparty.ext.AppCons
import com.amin.foodparty.databinding.*
import com.amin.foodparty.ext.showToast
import com.amin.foodparty.model.FoodDC
import com.amin.foodparty.model.FoodDao
import com.amin.foodparty.model.MyDatabase
import kotlin.collections.ArrayList

class MainScreenActivity : AppCompatActivity(), FoodAdapter.FoodEvent, MainScreenContract.View {
    private lateinit var binding: ActivityMainBinding
    private lateinit var myAdapter: FoodAdapter
    private lateinit var foodDao: FoodDao
    private lateinit var foodList: ArrayList<FoodDC>
    private lateinit var mainPresenter: MainScreenContract.Presenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainPresenter = MainScreenPresenter(MyDatabase.getDatabase(this).foodDao)

        val shP = getSharedPreferences("foodParty", Context.MODE_PRIVATE)
        if (shP.getBoolean("firstRun", true)) {
            mainPresenter.onFirstRun()
            shP.edit().putBoolean("firstRun", false).apply()
        }
        mainPresenter.onAttach(this)
        onClick()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainPresenter.onDetach()
    }

    private fun onClick()  {
        binding.btnRemoveAllFood.setOnClickListener {
            mainPresenter.onDeleteAllClicked()
        }
        binding.btnAddNewFood.setOnClickListener {
            val dialog = AlertDialog.Builder(this).create()
            val dialogBinding = DialogAddNewItemBinding.inflate(layoutInflater)
            dialog.setView(dialogBinding.root)
            dialog.show()
            dialogBinding.dialogBtnSubmit.setOnClickListener {
                if (TextUtils.isEmpty(dialogBinding.dialogFoodNameEt.editText?.text.toString()) ||
                    TextUtils.isEmpty(dialogBinding.dialogCityEt.editText?.text.toString()) ||
                    TextUtils.isEmpty(dialogBinding.dialogPriceEt.editText?.text.toString()) ||
                    TextUtils.isEmpty(dialogBinding.dialogDistanceEt.editText?.text.toString())
                ) {
                    showToast("please fill all blanks")
                } else {
                    val txtFoodName = dialogBinding.dialogFoodNameEt.editText?.text.toString()
                    val txtCity = dialogBinding.dialogCityEt.editText?.text.toString()
                    val txtPrice = dialogBinding.dialogPriceEt.editText?.text.toString()
                    val txtDistance = dialogBinding.dialogDistanceEt.editText?.text.toString()
                    val txtRatingNum = (0..150).random()
                    val ratingBarStar: Float = (1..5).random().toFloat()
                    val randomForUrl = (1..12).random()
                    val urlPic = AppCons.BASE_URL_IMAGE + randomForUrl.toString() + ".jpg"
                    val newFood = FoodDC(
                        title = txtFoodName,
                        price = txtPrice,
                        distance = txtDistance,
                        place = txtCity,
                        imageUrl = urlPic,
                        numOfRating = txtRatingNum,
                        rating = ratingBarStar
                    )
                    mainPresenter.onAddNewFoodClicked(newFood)
                    dialog.dismiss()
                    binding.recyclerMain.scrollToPosition(0)
                }
            }
            dialogBinding.dialogBtnCancel.setOnClickListener {
                dialog.dismiss()
            }
        }
        binding.btnRemoveAllFood.setOnClickListener {
            val dialog = AlertDialog.Builder(this).create()
            val dialogBinding = DialogDeleteAllItemsBinding.inflate(layoutInflater)
            dialog.setView(dialogBinding.root)
            dialog.show()
            dialogBinding.dialogDeleteBtnNo.setOnClickListener {
                dialog.dismiss()
            }
            dialogBinding.dialogDeleteBtnYes.setOnClickListener {
                mainPresenter.onDeleteAllClicked()
                dialog.dismiss()
            }
        }
        binding.searchEt.addTextChangedListener {
            mainPresenter.onSearchFoodClicked(it.toString())
        }
    }

    override fun showAllFoods(data: List<FoodDC>) {
        myAdapter = FoodAdapter(ArrayList(data), this)
        binding.recyclerMain.adapter = myAdapter
        binding.recyclerMain.layoutManager = LinearLayoutManager(this)
    }

    override fun refreshAllFoods(data: List<FoodDC>) {
        myAdapter.setAllData(ArrayList(data))
    }

    override fun addNewFood(newFood: FoodDC) {
        myAdapter.addNewFood(newFood)
        binding.recyclerMain.scrollToPosition(0)
    }

    override fun deleteFood(oldFood: FoodDC, pos: Int) {
        myAdapter.removeFood(oldFood, pos)
    }

    override fun editFood(editingFood: FoodDC, pos: Int) {
        myAdapter.updateFood(editingFood, pos)
    }

    override fun showProgressBar() {
        // TODO("Not yet implemented") todo loading things
    }

    override fun onFoodItemClicked(foodDC: FoodDC, position: Int) {
        val dialog = AlertDialog.Builder(this).create()
        val dialogBinding = DialogUpdateItemBinding.inflate(layoutInflater)
        dialog.setView(dialogBinding.root)
        dialogBinding.dialogUpdateFoodNameEt.editText?.setText(foodDC.title)
        dialogBinding.dialogUpdateCityEt.editText?.setText(foodDC.place)
        dialogBinding.dialogUpdatePriceEt.editText?.setText(foodDC.price)
        dialogBinding.dialogUpdateDistanceEt.editText?.setText(foodDC.distance)
        dialog.show()
        dialogBinding.dialogUpdateBtnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialogBinding.dialogUpdateBtnUpdate.setOnClickListener {
            //update item:
            if (TextUtils.isEmpty(dialogBinding.dialogUpdateFoodNameEt.editText?.text.toString()) ||
                TextUtils.isEmpty(dialogBinding.dialogUpdateCityEt.editText?.text.toString()) ||
                TextUtils.isEmpty(dialogBinding.dialogUpdatePriceEt.editText?.text.toString()) ||
                TextUtils.isEmpty(dialogBinding.dialogUpdateDistanceEt.editText?.text.toString())
            ) {
                showToast("please fill all blanks")
            } else {
                val txtFoodName = dialogBinding.dialogUpdateFoodNameEt.editText?.text.toString()
                val txtCity = dialogBinding.dialogUpdateCityEt.editText?.text.toString()
                val txtPrice = dialogBinding.dialogUpdatePriceEt.editText?.text.toString()
                val txtDistance = dialogBinding.dialogUpdateDistanceEt.editText?.text.toString()

                val newFood = FoodDC(
                    id = foodDC.id,
                    title = txtFoodName,
                    price = txtPrice,
                    distance = txtDistance,
                    place = txtCity,
                    imageUrl = foodDC.imageUrl,
                    numOfRating = foodDC.numOfRating,
                    rating = foodDC.rating,
                )
                mainPresenter.onUpdateFood(newFood, position)
                dialog.dismiss()
            }
        }
    }

    override fun onFoodItemLongClicked(foodDC: FoodDC, position: Int) {
        val dialog = AlertDialog.Builder(this).create()
        val dialogBinding = DialogDeleteItemBinding.inflate(layoutInflater)
        dialog.setView(dialogBinding.root)
        dialog.show()
        dialogBinding.dialogDeleteBtnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialogBinding.dialogDeleteBtnYes.setOnClickListener {
            mainPresenter.onDeleteFood(foodDC , position)
            dialog.dismiss()
        }
    }
}


// onCreate data
/*foodDao = MyDatabase.getDatabase(this).foodDao
firstRun()
searchInEditText()
val shP = getSharedPreferences("foodParty", Context.MODE_PRIVATE)
if (shP.getBoolean("firstRun", true)) {
    shP.edit().putBoolean("firstRun", false).apply()
}
showAllData()

binding.btnAddNewFood.setOnClickListener {
    val dialog = AlertDialog.Builder(this).create()
    val dialogBinding = DialogAddNewItemBinding.inflate(layoutInflater)
    dialog.setView(dialogBinding.root)
    dialog.show()
    dialogBinding.dialogBtnSubmit.setOnClickListener {
        if (TextUtils.isEmpty(dialogBinding.dialogFoodNameEt.editText?.text.toString()) ||
            TextUtils.isEmpty(dialogBinding.dialogCityEt.editText?.text.toString()) ||
            TextUtils.isEmpty(dialogBinding.dialogPriceEt.editText?.text.toString()) ||
            TextUtils.isEmpty(dialogBinding.dialogDistanceEt.editText?.text.toString())
        ) {
            Toast.makeText(this, "please fill all blanks", Toast.LENGTH_SHORT).show()
        } else {
            val txtFoodName = dialogBinding.dialogFoodNameEt.editText?.text.toString()
            val txtCity = dialogBinding.dialogCityEt.editText?.text.toString()
            val txtPrice = dialogBinding.dialogPriceEt.editText?.text.toString()
            val txtDistance = dialogBinding.dialogDistanceEt.editText?.text.toString()
            val txtRatingNum = (0..150).random()
            val ratingBarStar: Float = (1..5).random().toFloat()
            val randomForUrl = (1..12).random()
            val urlPic = AppCons.BASE_URL_IMAGE + randomForUrl.toString() + ".jpg"
            val newFood = FoodDC(
                title = txtFoodName,
                price = txtPrice,
                distance = txtDistance,
                place = txtCity,
                imageUrl = urlPic,
                numOfRating = txtRatingNum,
                rating = ratingBarStar
            )
            myAdapter.addNewFood(newFood)
            foodDao.insertFood(newFood)
            dialog.dismiss()
            binding.recyclerMain.scrollToPosition(0)
        }
    }

    dialogBinding.dialogBtnCancel.setOnClickListener {
        dialog.dismiss()
    }
}
searchInEditText()
binding.btnRemoveAllFood.setOnClickListener {
    val dialog = AlertDialog.Builder(this).create()
    val dialogBinding = DialogDeleteAllItemsBinding.inflate(layoutInflater)
    dialog.setView(dialogBinding.root)
    dialog.show()
    dialogBinding.dialogDeleteBtnNo.setOnClickListener {
        dialog.dismiss()
    }
    dialogBinding.dialogDeleteBtnYes.setOnClickListener {
        foodDao.deleteAllFoods()
        showAllData()
        dialog.dismiss()
    }
}*/
// food data
/*

private fun showAllData() {
    val foodList = foodDao.getAllFoods()
    myAdapter = FoodAdapter(foodList as ArrayList<FoodDC>, this)
    binding.recyclerMain.adapter = myAdapter
    binding.recyclerMain.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

}

private fun firstRun() {
    foodList = arrayListOf<FoodDC>(
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
        ),
    )
    foodDao.insertAllFoods(foodList)
}

override fun onFoodItemClicked(foodDC: FoodDC, position: Int) {
    val dialog = AlertDialog.Builder(this).create()
    val dialogBinding = DialogUpdateItemBinding.inflate(layoutInflater)
    dialog.setView(dialogBinding.root)
    dialogBinding.dialogUpdateFoodNameEt.editText?.setText(foodDC.title)
    dialogBinding.dialogUpdateCityEt.editText?.setText(foodDC.place)
    dialogBinding.dialogUpdatePriceEt.editText?.setText(foodDC.price)
    dialogBinding.dialogUpdateDistanceEt.editText?.setText(foodDC.distance)
    dialog.show()
    dialogBinding.dialogUpdateBtnDelete.setOnClickListener {
        dialog.dismiss()
    }
    dialogBinding.dialogUpdateBtnUpdate.setOnClickListener {
        //update item:
        if (TextUtils.isEmpty(dialogBinding.dialogUpdateFoodNameEt.editText?.text.toString()) ||
            TextUtils.isEmpty(dialogBinding.dialogUpdateCityEt.editText?.text.toString()) ||
            TextUtils.isEmpty(dialogBinding.dialogUpdatePriceEt.editText?.text.toString()) ||
            TextUtils.isEmpty(dialogBinding.dialogUpdateDistanceEt.editText?.text.toString())
        ){
            Toast.makeText(this, "please fill all blanks", Toast.LENGTH_SHORT).show()
        } else{
            val txtFoodName = dialogBinding.dialogUpdateFoodNameEt.editText?.text.toString()
            val txtCity = dialogBinding.dialogUpdateCityEt.editText?.text.toString()
            val txtPrice = dialogBinding.dialogUpdatePriceEt.editText?.text.toString()
            val txtDistance = dialogBinding.dialogUpdateDistanceEt.editText?.text.toString()

            val newFood = FoodDC(
                id = foodDC.id ,
                title = txtFoodName,
                price = txtPrice,
                distance = txtDistance,
                place = txtCity,
                imageUrl = foodDC.imageUrl,
                numOfRating = foodDC.numOfRating ,
                rating = foodDC.rating ,
            )
            myAdapter.updateFood(newFood , position )
            foodDao.updateFood(newFood)
            dialog.dismiss()
        }
    }
}

override fun onFoodItemLongClicked(foodDC: FoodDC, position: Int) {
    val dialog = AlertDialog.Builder(this).create()
    val dialogBinding = DialogDeleteItemBinding.inflate(layoutInflater)
    dialog.setView(dialogBinding.root)
    dialog.show()
    dialogBinding.dialogDeleteBtnNo.setOnClickListener {
        dialog.dismiss()
    }
    dialogBinding.dialogDeleteBtnYes.setOnClickListener {
        myAdapter.removeFood(foodDC, position)
        foodDao.deleteFood(foodDC)
        dialog.dismiss()
    }
}

//search in editText
private fun searchInEditText(){
    binding.searchEt.addTextChangedListener { editTextInput ->
        if (editTextInput!!.isEmpty()){
            showAllData()
            val myFood = foodDao.getAllFoods()
            myAdapter.setAllData(myFood as ArrayList<FoodDC> )
        }else{
            val resultFoodDC = foodDao.searchFood(editTextInput.toString())
            val cloneList = foodList.clone() as ArrayList<FoodDC>
            val filteredlist = cloneList.filter {
                it.title.contains(editTextInput.toString())

            }
            myAdapter.setAllData(resultFoodDC as ArrayList<FoodDC>)
        }

    }

}*/
