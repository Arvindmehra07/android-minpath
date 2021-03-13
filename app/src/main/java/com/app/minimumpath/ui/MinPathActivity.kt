package com.app.minimumpath.ui

import android.content.Context
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.minimumpath.R
import com.app.minimumpath.model.GridCell
import com.app.minimumpath.viewmodel.MinPathViewModel
import kotlinx.android.synthetic.main.activity_minpath.*

class MinPathActivity : AppCompatActivity(){
    private lateinit var minPathViewModel : MinPathViewModel
    private lateinit var vibe : Vibrator
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_minpath)
        init()
    }

    private fun init(){
        vibe = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        minPathViewModel = ViewModelProvider(this).get(MinPathViewModel::class.java)
        minPathViewModel.getGrid().observe(this, Observer{grid ->
            val len = grid.size
            for( i in 0 until len){
                val gridCell = matrixGridView.getChildAt(i) as TextView
                gridCell.text = grid[i].value
                gridCell.setBackgroundColor(resources.getColor(grid[i].color))
                gridCell.setTextColor(resources.getColor(grid[i].color))
            }
        })
        minPathViewModel.getIsNoPathPossible().observe(this, {isNoPathAvailable->
            if (isNoPathAvailable) {
                showNoPathFoundDialog()
            }
        })

        setGridOnClickListener()

        resetImageView.setOnClickListener {
            minPathViewModel.resetBoard()
        }

        helpImageView.setOnClickListener {
            showHelpDialog()
        }

        findPathTextView.setOnClickListener {
            minPathViewModel.findPath(getObstacleMatrix())
        }
    }

    private fun setGridOnClickListener(){
        count = matrixGridView.childCount
        for (i in 0 until count){
            val grid = matrixGridView.getChildAt(i) as TextView
            grid.setOnClickListener{
                vibe.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
                when(grid.text){
                    "0","1" ->{
                        grid.setBackgroundColor(resources.getColor(R.color.black_overlay))
                        grid.text = "-1"
                        grid.setTextColor(resources.getColor(R.color.black_overlay))
                    }
                    "-1"->{
                        grid.setBackgroundColor(resources.getColor(R.color.white))
                        grid.text = "0"
                        grid.setTextColor(resources.getColor(R.color.white))
                    }
                    "S"->{
                        grid.setBackgroundColor(resources.getColor(R.color.finish_red))
                        grid.text = "F"
                        grid.setTextColor(resources.getColor(R.color.finish_red))
                    }
                    "F"->{
                        grid.setBackgroundColor(resources.getColor(R.color.teal_700))
                        grid.text = "S"
                        grid.setTextColor(resources.getColor(R.color.teal_700))
                    }
                }
            }
            grid.setOnLongClickListener {
                if((grid.text == "F") || (grid.text == "S")){
                    grid.setBackgroundColor(resources.getColor(R.color.white))
                    grid.text = "0"
                    grid.setTextColor(resources.getColor(R.color.white))
                }else{
                    grid.setBackgroundColor(resources.getColor(R.color.teal_700))
                    grid.text = "S"
                    grid.setTextColor(resources.getColor(R.color.teal_700))
                }
                true
            }
        }
    }

    private fun getObstacleMatrix(): List<GridCell>{
        val matrix = List(64, init = { GridCell("0", R.color.white) })
        for (i in 0 until count){
            val grid = matrixGridView.getChildAt(i) as TextView
            val value = grid.text.toString().trim()
            matrix[i].value = value
            when (value){
                "-1" -> matrix[i].color = R.color.black_overlay
                "F" ->  matrix[i].color = R.color.finish_red
                "S" -> matrix[i].color = R.color.teal_700
            }
        }
        return matrix
    }

    private fun showHelpDialog(){
        val fragment = HelpDialogFragment.newInstance(getString(R.string.help_dialog_msg))
        supportFragmentManager.beginTransaction().add(fragment, HelpDialogFragment::class.java.name).commit()
    }

    private fun showNoPathFoundDialog(){
        val fragment = HelpDialogFragment.newInstance(getString(R.string.no_path_found_msg))
        supportFragmentManager.beginTransaction().add(fragment, HelpDialogFragment::class.java.name).commit()
    }

}