package com.us.mytest.ui.activity.secondlist

data class ListData(
    val code: String,
    val data: Data,
    val msg: String,
    val success: Boolean,
    val timestamp: String
)

data class Data(
    val discounts: MutableList<Discount>?,
    val payTypes: MutableList<String>,
    val plugAndChargeId: String
)

data class Discount(
    val discountId: String,
    val discountName: String,
    val payType: String,
    val plugAndChargeDiscountId: String,
    val position: Int
)


