package com.us.maunc.ui.activity.pushbox

/**
 *ClsFunction：
 *CreateDate：2024/5/16
 *Author：TimeWillRememberUs
 */
object PushBoxMapUtils {

    /*
     * 0 无场景
     * 1 墙
     * 2 目标
     * 3 路
     * 4 箱子
     * 5 目标区域
     * 6 人
     */
    var map: Array<Array<IntArray>> = arrayOf(
        arrayOf(
            intArrayOf(0, 0, 1, 1, 1, 0, 0, 0),
            intArrayOf(0, 0, 1, 2, 1, 0, 0, 0),
            intArrayOf(0, 0, 1, 3, 1, 1, 1, 1),
            intArrayOf(1, 1, 1, 4, 3, 4, 2, 1),
            intArrayOf(1, 2, 3, 4, 6, 1, 1, 1),
            intArrayOf(1, 1, 1, 1, 4, 1, 0, 0),
            intArrayOf(0, 0, 0, 1, 2, 1, 0, 0),
            intArrayOf(0, 0, 0, 1, 1, 1, 0, 0)
        ),
        arrayOf(
            intArrayOf(1, 1, 1, 1, 1, 0, 0, 0, 0),
            intArrayOf(1, 3, 3, 6, 1, 0, 0, 0, 0),
            intArrayOf(1, 3, 4, 4, 1, 0, 1, 1, 1),
            intArrayOf(1, 3, 4, 3, 1, 0, 1, 2, 1),
            intArrayOf(1, 1, 1, 3, 1, 1, 1, 2, 1),
            intArrayOf(0, 1, 1, 3, 3, 3, 3, 2, 1),
            intArrayOf(0, 1, 3, 3, 3, 1, 3, 3, 1),
            intArrayOf(0, 1, 3, 3, 3, 1, 1, 1, 1),
            intArrayOf(0, 1, 1, 1, 1, 1, 0, 0, 0)
        ),
        arrayOf(
            intArrayOf(0, 1, 1, 1, 1, 1, 1, 1, 0, 0),
            intArrayOf(0, 1, 3, 3, 3, 3, 3, 1, 1, 1),
            intArrayOf(1, 1, 4, 1, 1, 1, 3, 3, 3, 1),
            intArrayOf(1, 6, 3, 3, 4, 3, 3, 4, 3, 1),
            intArrayOf(1, 3, 2, 2, 1, 3, 4, 3, 1, 1),
            intArrayOf(1, 1, 2, 2, 1, 3, 3, 3, 1, 0),
            intArrayOf(0, 1, 1, 1, 1, 1, 1, 1, 1, 0)
        ),
        arrayOf(
            intArrayOf(0, 1, 1, 1, 1, 0),
            intArrayOf(1, 1, 3, 3, 1, 0),
            intArrayOf(1, 3, 6, 4, 1, 0),
            intArrayOf(1, 1, 4, 3, 1, 1),
            intArrayOf(1, 1, 3, 4, 3, 1),
            intArrayOf(1, 2, 4, 3, 3, 1),
            intArrayOf(1, 2, 2, 3, 2, 1),
            intArrayOf(1, 1, 1, 1, 1, 1)
        )
    )

    fun getMap(grade: Int): Array<IntArray> {
        val temp: Array<IntArray> = if (grade >= 0 && grade < map.size) {
            map[grade]
        } else {
            map[0]
        }
        val row = temp.size
        val column = temp[0].size
        val result = Array(row) {
            IntArray(column)
        }
        for (i in 0 until row) {
            for (j in 0 until column) {
                result[i][j] = temp[i][j]
            }
        }
        return result
    }

}