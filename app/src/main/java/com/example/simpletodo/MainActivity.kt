package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lateinit var adapter: TaskItemAdapter

        //----Long Click to delete -----------
        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                // delete item at pos
                listOfTasks.removeAt(position)
                //notify adapter
                adapter.notifyDataSetChanged()

                saveItems()
            }
        }
        loadItems()

        // look up recycler view in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // create adapter passing in the sample data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // attach adapter to rv
        recyclerView.adapter = adapter
        // set layout manager
        recyclerView.layoutManager = LinearLayoutManager(this)

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        //----------- Add to list ----------------
        findViewById<Button>(R.id.button).setOnClickListener {
            //1. Grab the text user put into text field
            val userInputtedTask = inputTextField.text.toString()
            //2. Add string to list of task
            listOfTasks.add(userInputtedTask)

            // Notify adapter that data updated
            adapter.notifyItemInserted(listOfTasks.size - 1)

            //3. clear field after adding
            findViewById<EditText>(R.id.addTaskField).setText("")

            saveItems()
        }
    }

    //------------Save input data by writing and reading in a file-----------

    //1. create a method to get data file
    fun getDataFile() : File {
        return File(filesDir, "data.txt")
    }
    //2. load the items by reading all the lines
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
    //3. save items by writing to the file
    fun saveItems() {
        try{
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
}