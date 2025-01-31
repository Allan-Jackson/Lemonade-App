/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.lemonade

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import java.sql.Driver

class MainActivity : AppCompatActivity() {

    private val LEMONADE_STATE = "LEMONADE_STATE"
    private val LEMON_SIZE = "LEMON_SIZE"
    private val SQUEEZE_COUNT = "SQUEEZE_COUNT"
    // SELECT represents the "pick lemon" state
    private val SELECT = "select"
    // SQUEEZE represents the "squeeze lemon" state
    private val SQUEEZE = "squeeze"
    // DRINK represents the "drink lemonade" state
    private val DRINK = "drink"
    // RESTART represents the state where the lemonade has be drunk and the glass is empty
    private val RESTART = "restart"
    // Default the state to select
    private var lemonadeState = "select"
    // Default lemonSize to -1
    private var lemonSize = -1
    // Default the squeezeCount to -1
    private var squeezeCount = -1

    //Declare animations
    private val stb = AnimationUtils.loadAnimation(this,R.anim.stb)
    private val leftin = AnimationUtils.loadAnimation(this,R.anim.leftin)
    private val leftout = AnimationUtils.loadAnimation(this,R.anim.leftout)
    private val squeeze = AnimationUtils.loadAnimation(this,R.anim.squeeze)

    private var lemonTree = LemonTree()
    private var lemonImage: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // === DO NOT ALTER THE CODE IN THE FOLLOWING IF STATEMENT ===
        if (savedInstanceState != null) {
            lemonadeState = savedInstanceState.getString(LEMONADE_STATE, "select")
            lemonSize = savedInstanceState.getInt(LEMON_SIZE, -1)
            squeezeCount = savedInstanceState.getInt(SQUEEZE_COUNT, -1)
        }
        // === END IF STATEMENT ===

        lemonImage = findViewById(R.id.image_lemon_state)
        //a animação
        lemonImage!!.startAnimation(stb)
        setViewElements()
        lemonImage!!.setOnClickListener {
            clickLemonImage()
        }
        lemonImage!!.setOnLongClickListener {
            showSnackbar()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(LEMONADE_STATE, lemonadeState)
        outState.putInt(LEMON_SIZE, lemonSize)
        outState.putInt(SQUEEZE_COUNT, squeezeCount)
        super.onSaveInstanceState(outState)
    }


    private fun clickLemonImage() {
        if (this.lemonadeState == SELECT) {
            lemonadeState = SQUEEZE
            lemonSize = lemonTree.pick()
            squeezeCount = 0
        }
        else if (lemonadeState == SQUEEZE) {
            squeezeCount += 1
            lemonSize -= 1
            if (lemonSize == 0){
                lemonadeState = DRINK
            }
        }
        else if (lemonadeState == DRINK){
            lemonadeState = RESTART
            lemonSize = -1
        }
        else if (lemonadeState == RESTART){
            lemonadeState = SELECT
        }
        setViewElements()
    }


    private fun setViewElements() {
        val textAction: TextView = findViewById(R.id.text_action)

        when (lemonadeState) {
            SELECT -> {textAction.setText(R.string.lemon_select)
                lemonImage?.setImageResource(R.drawable.lemon_tree)}
            SQUEEZE -> {textAction.setText(R.string.lemon_squeeze)
                lemonImage?.setImageResource(R.drawable.lemon_squeeze)}
            DRINK -> {textAction.setText(R.string.lemon_drink)
                lemonImage?.setImageResource(R.drawable.lemon_drink)}
            RESTART -> {textAction.setText(R.string.lemon_empty_glass)
                lemonImage?.setImageResource(R.drawable.lemon_restart)}
        }
    }

    private fun showSnackbar(): Boolean {
        if (lemonadeState != SQUEEZE) {
            return false
        }
        val squeezeText = getString(R.string.squeeze_count, squeezeCount)
        Snackbar.make(
            findViewById(R.id.constraint_Layout),
            squeezeText,
            Snackbar.LENGTH_SHORT
        ).show()
        return true
    }
}

class LemonTree {
    fun pick(): Int {
        return (2..4).random()
    }
}
