package com.amin.foodparty.ext


interface BasePresenter<T> {
    fun onAttach(view:T)
    fun onDetach()
}

interface BaseView {
    fun showProgressBar()
}
