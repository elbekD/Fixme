package com.yandex.dev.fixme.controller

import android.view.View
import com.yandex.dev.fixme.base.BaseItem
import java.util.*
import kotlin.math.exp

class Controller(val items: List<BaseItem>) {

    companion object {
        const val BUGS_EXPECT = 80
        const val KOSTIL_EXPECT = 20


        const val LIVE_TIME = 1_000L
        const val STEP_TIME = 1_000
        const val MAX_LIVES = 5
    }
    private val random = Random()
    private val timer = Timer()

    private var score = 0
    private var lives = MAX_LIVES

    init {
//        ItemFactory.createBug()
    }


    private fun List<BaseItem>.getInvisibleItem(): BaseItem {
        val list = this.filter { it.view.visibility == View.INVISIBLE }
        return list.get(random.nextInt(list.size))
    }
    private fun BaseItem.isVisible(): Boolean {
        return this.view.visibility == View.VISIBLE
    }
    private fun getType() {
        val expect = random.nextInt(BUGS_EXPECT + KOSTIL_EXPECT)
        return when {
            expect > BUGS_EXPECT -> 1
            expect < BUGS_EXPECT -> 1
            else 0
        }
    }

    private fun makeStep() {
        val item = items.getInvisibleItem()
        item.appear()
        item.view.setOnClickListener {
            if (item.isVisible()) {
                ++score
                item.disappear()
            }
        }

        timer.schedule(
                object : TimerTask() {
                    override fun run() {
                        if (item.isVisible()) {
                            --lives
                            item.destroy()
                            map.removeItem(item.position)
                        }
                    }
                },
                LIVE_TIME
        )
    }

    fun startGame() {
        timer.schedule(Step(),0L)
    }

    inner class Step : TimerTask() {

        override fun run() {
            this@Controller.makeStep()
            this@Controller.timer.schedule(
                    this@Step,
                    this@Controller.random.nextInt(STEP_TIME).toLong()
            )
        }
    }

}