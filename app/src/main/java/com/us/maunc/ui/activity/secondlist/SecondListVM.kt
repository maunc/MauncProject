package com.us.maunc.ui.activity.secondlist

import com.maunc.jetpackmvvm.base.BaseModel
import com.maunc.jetpackmvvm.base.BaseViewModel

class SecondListVM : BaseViewModel<BaseModel>() {
    val mJson = "{\n" +
            "            \"success\": true,\n" +
            "            \"code\": \"0000\",\n" +
            "            \"msg\": \"操作成功\",\n" +
            "            \"data\": {\n" +
            "            \"plugAndChargeId\": \"360\",\n" +
            "            \"payTypes\": [\n" +
            "            \"活动\",\n" +
            "            \"卡\",\n" +
            "            \"优惠券\"\n" +
            "            ],\n" +
            "            \"discounts\": [\n" +
            "\n" +
            "            {\n" +
            "                \"plugAndChargeDiscountId\": \"46\",\n" +
            "                \"payType\": \"CARD\",\n" +
            "                \"position\": 3,\n" +
            "                \"discountId\": \"1763104581236899842\",\n" +
            "                \"discountName\": \"新充换电3000度循环发\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"plugAndChargeDiscountId\": \"47\",\n" +
            "                \"payType\": \"CARD\",\n" +
            "                \"position\": 4,\n" +
            "                \"discountId\": \"1764840154096848898\",\n" +
            "                \"discountName\": \"自动下发充换电卡\"\n" +
            "            }\n" +
            "            ]\n" +
            "        },\n" +
            "            \"timestamp\": \"1716885893759\"\n" +
            "        }"
}