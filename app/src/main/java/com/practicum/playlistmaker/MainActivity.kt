package com.practicum.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        через реализацию анонимного класса,
        val title = findViewById<TextView>(R.id.toolbar_title)
        val titleClickListener: View.OnClickListener = object : View.OnClickListener { override fun onClick(v: View?) {
            Toast.makeText(this@MainActivity, getString(R.string.app_name), Toast.LENGTH_SHORT).show()
        } }
        title.setOnClickListener(titleClickListener)

//        с помощью лямбда-выражения.
        val search = findViewById<Button>(R.id.search)
        search.setOnClickListener {
            Toast.makeText(this@MainActivity, getString(R.string.menu_search), Toast.LENGTH_SHORT).show()
        }

        val media = findViewById<Button>(R.id.media)
        media.setOnClickListener(this@MainActivity)
        val settings = findViewById<Button>(R.id.settings)
        settings.setOnClickListener(this@MainActivity)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.media -> {
                Toast.makeText(this, getString(R.string.menu_media), Toast.LENGTH_SHORT).show()
            }
            R.id.settings -> {
                Toast.makeText(this, getString(R.string.menu_setting), Toast.LENGTH_SHORT).show()
            }
        }
    }
}