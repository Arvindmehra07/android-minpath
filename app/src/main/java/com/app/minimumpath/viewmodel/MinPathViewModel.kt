package com.app.minimumpath.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.minimumpath.R
import com.app.minimumpath.model.GridCell
import com.app.minimumpath.util.GridService

class MinPathViewModel:ViewModel() {
    private val grid =  MutableLiveData<List<GridCell>>()
    private val isNoPathPossible = MutableLiveData<Boolean>()

    private var gridService: GridService? = null

    init{
        gridService = GridService.getInstance()
        isNoPathPossible.value = false
        setGrid()
    }

    fun getGrid() : LiveData<List<GridCell>>{
        return grid
    }

    fun getIsNoPathPossible() : MutableLiveData<Boolean>{
        return isNoPathPossible
    }

    fun findPath(matrix: List<GridCell>) {
        grid.value = matrix
        val updatedGrid = gridService?.fillPath(grid.value!!)
        if (updatedGrid == null) isNoPathPossible.postValue(true)
        else grid.value = updatedGrid!!
    }

    fun resetBoard(){
        val updatedGrid = gridService?.resetBoard(grid.value!!)!!
        grid.value = updatedGrid
    }
    private fun setGrid (){
        val matrix = List<GridCell>(64, init = { GridCell("0", R.color.white) })
        matrix[0].value ="S"
        matrix[0].color = R.color.teal_700
        matrix[63].value = "F"
        matrix[63].color = R.color.finish_red
        grid.value = matrix
    }
}