package com.app.minimumpath.util

import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import com.app.minimumpath.R
import com.app.minimumpath.model.GridCell
import com.app.minimumpath.ui.MinPathActivity
import kotlinx.android.synthetic.main.activity_minpath.*

class GridService {

    companion object{
        @Volatile private var instance : GridService? = null
        fun getInstance() =
            instance?: synchronized(this){
                instance ?: GridService().also { instance = it }
            }
    }

    private var start = 0
    private var end = 0

    /**
     * Utility function to get snapshot of 2d grid with it's obstacles
     */

    private fun getObstacleMatrix(grid: List<GridCell>): ArrayList<ArrayList<String>>{
        val matrix = ArrayList<ArrayList<String>>()
        var str = ""
        var rowChange = 0
        for (i in grid){
            str += i.value + ","
            rowChange++
            if (rowChange == 8){
                val row = str.split(",").toMutableList()
                row.removeAt(8)
                matrix.add(row as ArrayList<String>)
                str = ""
                rowChange = 0
            }
        }
        return matrix
    }

    /**
     * Utility function to get Adjacency matrix from Obstacle matrix, since only 4 directions of movement
     * considered, up and left movements are assigned higher costs.
     */

    private fun getAdjacencyMatrix(grid: List<GridCell>): ArrayList<ArrayList<String>>{
        val matrix = getObstacleMatrix(grid)
        val lenRow =  matrix.size
        val lenCol = matrix[0].size
        val adjacencyMatrix = ArrayList<ArrayList<String>>()
        val lenMat =  lenRow*lenCol
        var row =  ""
        for (i in 0 until lenMat){
            row += "0,"
        }
        for (i in 0 until lenMat){
            val rowMat =  row.split(",").toMutableList() as ArrayList<String>
            rowMat.removeAt(64)
            adjacencyMatrix.add(rowMat)
        }

        for (i in 0 until lenRow){
            for (j in 0 until lenCol){
                if (matrix[i][j] != "-1"){
                    if(isValid(i, j - 1, lenRow, lenCol) && matrix[i][j - 1] != "-1")
                        adjacencyMatrix[(lenRow * i) + j][(lenRow * i) + (j - 1)] = "2"
                    if(isValid(i - 1, j, lenRow, lenCol) && matrix[i - 1][j] != "-1")
                        adjacencyMatrix[(lenRow * i) + j][(lenRow * (i - 1)) + j] = "2"
                    if(isValid(i, j + 1, lenRow, lenCol) && matrix[i][j + 1] != "-1")
                        adjacencyMatrix[(lenRow * i) + j][(lenRow * i) + (j + 1)] = "1"
                    if(isValid(i + 1, j, lenRow, lenCol) && matrix[i + 1][j] != "-1")
                        adjacencyMatrix[(lenRow * i) + j][(lenRow * (i + 1)) + j] = "1"
                }
                if(matrix[i][j] == "F"){
                    end = lenRow*i + j
                }else if(matrix[i][j] == "S"){
                    start = lenRow*i + j
                }
            }
        }

        return adjacencyMatrix
    }

    /**
     * Utility function to check if a grid lies within the boundaries
     */

    private fun isValid(x: Int, y: Int, xLim: Int, yLim: Int):Boolean{
        if (x>=xLim|| x<0 || y>=yLim || y<0) return false
        return true
    }


    /**
     * Utility function to get a cell with minimum distance among non visited cells
     */
    private fun getMinDist(len: Int, dist: Array<Int>, visited: Array<Int>):Int{
        var min = Int.MAX_VALUE
        var minIdx = -1
        for (i in 0 until len ){
            if (dist[i] < min && visited[i] == 0){
                min = dist[i]
                minIdx = i

            }
        }
        return minIdx
    }

    /**
     * Dijkstra's algorithm -> calculates the shortest distance from source cell to
     * finish cell. Returns an array of size total number of cells in the grid, each index denotes the cell itself,
     * and values it holds denotes the path algorithm chose to reach here.
     */
    private fun dijkstraUtil(grid : List<GridCell>): Array<Int>{
        val adjacencyMatrix = getAdjacencyMatrix(grid)
        val len = adjacencyMatrix.size
        val dist =  Array(len) { _ -> Int.MAX_VALUE }
        val fromArray =  Array(len) { _ -> -1 }
        val visited = Array(len) { _ -> 0 }
        dist[start] = 0
        visited[start] = 0
        fromArray[start] = 0
        for (x in 0 until len){
            val u = getMinDist(len, dist, visited)
            if (u < 0) continue
            visited[u] = 1
            for (v in 0 until len){
                if ((adjacencyMatrix[u][v]).toInt() > 0 && visited[v] == 0){
                    if (dist[v] > (adjacencyMatrix[u][v]).toInt()+ dist[u]){
                        dist[v] = (adjacencyMatrix[u][v]).toInt()+ dist[u]
                        fromArray[v] = u
                    }
                }
            }
        }
        return fromArray
    }

    /**
     * Utility function to generate a grid with shortest path using fromArray
     */
    fun fillPath(grid : List<GridCell>): List<GridCell>?{
        val fromArray = dijkstraUtil(grid)
        if (fromArray[end] == -1){
            return null
        }
        var x = end
        while(fromArray[x]!= start ){
            if (fromArray[x] == -1) continue
            grid[fromArray[x]].value = "1"
            grid[fromArray[x]].color = R.color.mustard_yellow
            x = fromArray[x]
        }
        return grid
    }

    /**
     * Resets the grid to no obstacles
     */
    fun resetBoard(grid : List<GridCell>) : List<GridCell> {
        for (i in grid) {
            i.value = "0"
            i.color = R.color.white
        }
        grid[0].value = "S"
        grid[0].color = R.color.teal_700
        grid[63].value = "F"
        grid[63].color = R.color.finish_red
        return grid
    }
}