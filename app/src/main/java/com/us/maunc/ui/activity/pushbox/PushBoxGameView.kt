package com.us.maunc.ui.activity.pushbox

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import com.maunc.mvvmhabit.utils.AppUtils
import com.maunc.mvvmhabit.utils.DeviceUtils
import com.us.maunc.R
import com.us.maunc.ui.activity.pushbox.PushBoxConstant.BOX
import com.us.maunc.ui.activity.pushbox.PushBoxConstant.BOX_AT_GOAL
import com.us.maunc.ui.activity.pushbox.PushBoxConstant.GOAL
import com.us.maunc.ui.activity.pushbox.PushBoxConstant.MAN
import com.us.maunc.ui.activity.pushbox.PushBoxConstant.ROAD
import com.us.maunc.ui.activity.pushbox.PushBoxConstant.WALL
import kotlin.math.floor


/**
 *ClsFunction：
 *CreateDate：2024/5/16
 *Author：TimeWillRememberUs
 */
@SuppressLint("NotConstructor")
class PushBoxGameView constructor(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {
    private var gate = 0 //当前关数
    private var map: Array<IntArray>? = null //当前地图
    private var width = DeviceUtils.getDeviceWidth() //宽
    private var height = DeviceUtils.getDeviceHeight(true) //高
    private var mapRow = 0 //地图行数
    private var mapColumn = 0 //地图列数
    private var manX = 0 //人所在行
    private var manY = 0 //人所在列
    private var xoff = 10f //左边距
    private var yoff = 20f //上边距
    private var tileSize = 0 //图片大小
    private lateinit var tem: Array<IntArray>//原始地图
    private var pic: Array<Bitmap?>? = null//图片
    private val paint: Paint? = null //定义画笔

    init {
        this.isFocusable = true
        //初始化地图和图片
        initMap()
        initPic()
    }

    //初始化地图
    private fun initMap() {
        //根据关数获得当前游戏地图
        map = getMap(gate)
        //获取地图和人物详细信息
        getMapDetail()
        getManPosition()
    }

    //获取地图
    private fun getMap(grade: Int): Array<IntArray> {
        return PushBoxMapUtils.getMap(grade)
    }

    //获取地图详细信息
    private fun getMapDetail() {
        mapRow = map!!.size
        mapColumn = map!![gate].size
        xoff = 30f
        yoff = 60f
        val t = if (mapRow > mapColumn) mapRow else mapColumn
        val s1 = floor(((width - 2 * xoff) / t).toDouble()).toInt()
        val s2 = floor(((height - yoff) / t).toDouble()).toInt()
        tileSize = if (s1 < s2) s1 else s2
        tem = PushBoxMapUtils.getMap(gate)
    }

    //获取人物位置
    private fun getManPosition() {
        for (i in map!!.indices) {
            for (j in map!![0].indices) {
                if (map!![i][j] == MAN) {
                    manX = i
                    manY = j
                    break
                }
            }
        }
    }

    //初始化图片资源
    private fun initPic() {
        pic = arrayOfNulls(7)
        AppUtils.getDrawable(R.drawable.push_box_qiang)?.let { loadPic(WALL, it) }
        AppUtils.getDrawable(R.drawable.push_box_goal)?.let { loadPic(GOAL, it) }
        AppUtils.getDrawable(R.drawable.push_box_lu)?.let { loadPic(ROAD, it) }
        AppUtils.getDrawable(R.drawable.push_box_xiangzi)?.let { loadPic(BOX, it) }
        AppUtils.getDrawable(R.drawable.push_box_boxgoal)?.let { loadPic(BOX_AT_GOAL, it) }
        AppUtils.getDrawable(R.drawable.push_box_ren)?.let { loadPic(MAN, it) }
    }

    //加载图片
    private fun loadPic(key: Int, tile: Drawable) {
        val bitmap = Bitmap.createBitmap(tileSize, tileSize, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        tile.setBounds(0, 0, tileSize, tileSize)
        tile.draw(canvas)
        pic!![key] = bitmap
    }

    //更换关卡
    private fun nextGate() {
        if (gate < PushBoxMapUtils.map.size - 1) {
            gate++
        } else {
            Toast.makeText(this.context, "最后一关了", Toast.LENGTH_SHORT).show()
        }
        reInitMap()
    }

    private fun reInitMap() {
        initMap()
        initPic()
    }

    //如果在地图上找不到空的目标区域或者可移动的箱子，则游戏结束
    private fun gameFinished(): Boolean {
        var finish = true
        for (i in 0 until mapRow) {
            for (j in 0 until mapColumn) {
                if (map!![i][j] == GOAL || map!![i][j] == BOX) {
                    finish = false
                }
            }
        }
        return finish
    }

    //人物向右移动→
    fun moveRight() {
        //如果人前面是箱子或者是目标区域的箱子则进行下一步
        if (map!![manX][manY + 1] == BOX || map!![manX][manY + 1] == BOX_AT_GOAL) {
            //看箱子或目标区域的箱子前面是不是目标或路，是，则移动
            if (map!![manX][manY + 2] == GOAL || map!![manX][manY + 2] == ROAD) {
                /*
				 * 三目运算符
				 * 如果箱子前是路，则用箱子代替
				 * 如果是目标，则用目标区域的箱子代替
				 */
                map!![manX][manY + 2] = if (map!![manX][manY + 2] == GOAL) BOX_AT_GOAL else BOX
                //箱子移动后的位置由人来代替
                map!![manX][manY + 1] = MAN
                //人所在的位置是恢复为路还是目标区域
                map!![manX][manY] = roadOrGoal(manX, manY)
                manY++
            }
        } else {
            if (map!![manX][manY + 1] == ROAD || map!![manX][manY + 1] == GOAL) {
                map!![manX][manY + 1] = MAN
                map!![manX][manY] = roadOrGoal(manX, manY)
                manY++
            }
        }
        //判断本关是否结束
        if (gameFinished()) {
            nextGate()
        }
        this.invalidate()
    }

    //人物向左移动←
    fun moveLeft() {
        //如果人前面是箱子或者是目标区域的箱子则进行下一步
        if (map!![manX][manY - 1] == BOX || map!![manX][manY - 1] == BOX_AT_GOAL) {
            //看箱子或目标区域的箱子前面是不是目标或路，是，则移动
            if (map!![manX][manY - 2] == GOAL || map!![manX][manY - 2] == ROAD) {
                /*
				 * 三目运算符
				 * 如果箱子前是路，则用箱子代替
				 * 如果是目标，则用目标区域的箱子代替
				 */
                map!![manX][manY - 2] = if (map!![manX][manY - 2] == GOAL) BOX_AT_GOAL else BOX
                //箱子移动后的位置由人来代替
                map!![manX][manY - 1] = MAN
                //人所在的位置是恢复为路还是目标区域
                map!![manX][manY] = roadOrGoal(manX, manY)
                manY--
            }
        } else {
            if (map!![manX][manY - 1] == ROAD || map!![manX][manY - 1] == GOAL) {
                map!![manX][manY - 1] = MAN
                map!![manX][manY] = roadOrGoal(manX, manY)
                manY--
            }
        }
        //判断本关是否结束
        if (gameFinished()) {
            nextGate()
        }
        this.invalidate()
    }

    //人物向上移动↑
    fun moveUp() {
        //如果人前面是箱子或者是目标区域的箱子则进行下一步
        if (map!![manX - 1][manY] == BOX || map!![manX - 1][manY] == BOX_AT_GOAL) {
            //看箱子或目标区域的箱子前面是不是目标或路，是，则移动
            if (map!![manX - 2][manY] == GOAL || map!![manX - 2][manY] == ROAD) {
                /*
				 * 三目运算符
				 * 如果箱子前是路，则用箱子代替
				 * 如果是目标，则用目标区域的箱子代替
				 */
                map!![manX - 2][manY] = if (map!![manX - 2][manY] == GOAL) BOX_AT_GOAL else BOX
                //箱子移动后的位置由人来代替
                map!![manX - 1][manY] = MAN
                //人所在的位置是恢复为路还是目标区域
                map!![manX][manY] = roadOrGoal(manX, manY)
                manX--
            }
        } else {
            if (map!![manX - 1][manY] == ROAD || map!![manX - 1][manY] == GOAL) {
                map!![manX - 1][manY] = MAN
                map!![manX][manY] = roadOrGoal(manX, manY)
                manX--
            }
        }
        //判断本关是否结束
        if (gameFinished()) {
            nextGate()
        }
        this.invalidate()
    }

    //人物向下移动↓
    fun moveDown() {
        //如果人前面是箱子或者是目标区域的箱子则进行下一步
        if (map!![manX + 1][manY] == BOX || map!![manX + 1][manY] == BOX_AT_GOAL) {
            //看箱子或目标区域的箱子前面是不是目标或路，是，则移动
            if (map!![manX + 2][manY] == GOAL || map!![manX + 2][manY] == ROAD) {
                /*
                 * 三目运算符
                 * 如果箱子前是路，则用箱子代替
                 * 如果是目标，则用目标区域的箱子代替
                 */
                map!![manX + 2][manY] = if (map!![manX + 2][manY] == GOAL) BOX_AT_GOAL else BOX
                //箱子移动后的位置由人来代替
                map!![manX + 1][manY] = MAN
                //人所在的位置是恢复为路还是目标区域
                map!![manX][manY] = roadOrGoal(manX, manY)
                manX++
            }
        } else {
            if (map!![manX + 1][manY] == ROAD || map!![manX + 1][manY] == GOAL) {
                map!![manX + 1][manY] = MAN
                map!![manX][manY] = roadOrGoal(manX, manY)
                manX++
            }
        }
        //判断本关是否结束
        if (gameFinished()) {
            nextGate()
        }
        this.invalidate()
    }

    /*
	 * 人所在的位置原来是路还是目标区域
	 * 使用原始地图tem来判断
	 * 看新地图人所在的位置在原始地图是什么角色
	 */
    private fun roadOrGoal(x: Int, y: Int): Int {
        var result = ROAD
        if (tem[x][y] == GOAL) {
            result = GOAL
        }
        return result
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (i in 0 until mapRow) {
            for (j in 0 until mapColumn) {
                if (map!![i][j] != 0) {
                    canvas.drawBitmap(
                        pic!![map!![i][j]]!!,
                        xoff + j * tileSize,
                        yoff + i * tileSize,
                        paint
                    )
                }
            }
        }
    }
}