package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.practicum.playlistmaker.itunes.ItunesApiService
import com.practicum.playlistmaker.itunes.ItunesResponse
import com.practicum.playlistmaker.itunes.SearchHistoryAdapter
import com.practicum.playlistmaker.itunes.TracksAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    var searchText: String = ""
    private val searchRunnable = Runnable { search() }
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true

    private var toolbar: Toolbar? = null
    private var linearLayout: LinearLayout? = null
    private var inputEditText: EditText? = null
    private var recyclerView: RecyclerView? = null
    private var clearButton: ImageView? = null
    private var messageImg: ImageView? = null
    private var messageText: TextView? = null
    private var messageBtn: Button? = null
    private var searchHistory: TextView? = null
    private var searchHistoryBtn: Button? = null
    private var searchPgb: ProgressBar? = null

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        toolbar = findViewById(R.id.search_toolbar)
        linearLayout = findViewById(R.id.container)
        inputEditText = findViewById(R.id.inputEditText)
        clearButton = findViewById(R.id.clearIcon)
        recyclerView = findViewById(R.id.trackRecyclerView)
        messageImg = findViewById(R.id.message_img)
        messageText = findViewById(R.id.message_text)
        messageBtn = findViewById(R.id.message_btn)
        searchHistory = findViewById(R.id.search_history)
        searchHistoryBtn = findViewById(R.id.search_history_btn)
        searchPgb = findViewById(R.id.search_pgb)

        toolbar?.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        inputEditText?.setText(searchText)
        inputEditText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
            }
            false
        }
        clearButton?.setOnClickListener {
            recyclerView?.adapter = TracksAdapter(this@SearchActivity)
            inputEditText?.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(linearLayout?.windowToken, 0)
        }
        messageBtn?.setOnClickListener { search() }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton?.visibility = clearButtonVisibility(s)
                if (inputEditText?.hasFocus() == true && s?.isEmpty() == true) showHistory() else goneHistory()
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
                searchText = s.toString()
            }
        }
        inputEditText?.addTextChangedListener(simpleTextWatcher)
        inputEditText?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && inputEditText?.text!!.isEmpty()) showHistory() else goneHistory()
        }
        searchHistoryBtn?.setOnClickListener {
            (applicationContext as App).clearHistory()
            goneHistory()
        }
        showHistory()
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true },
                CLICK_DEBOUNCE_DELAY
            )
        }
        return current
    }

    private fun search() {
        goneHistory()
        recyclerView?.adapter = TracksAdapter(this@SearchActivity)
        ItunesApiService.build.search(inputEditText?.text.toString())
            .enqueue(object : Callback<ItunesResponse> {
                override fun onResponse(
                    call: Call<ItunesResponse>,
                    response: Response<ItunesResponse>
                ) {
                    val list = response.body()?.results
                    if (response.isSuccessful) {
                        if (list?.isNotEmpty() == true) {
                            messageOk()
                            val adapter = TracksAdapter(this@SearchActivity)
                            recyclerView?.layoutManager =
                                LinearLayoutManager(this@SearchActivity)
                            recyclerView?.adapter = adapter
                            adapter.updateList(list)
                        } else {
                            messageEmpty()
                        }
                    } else {
                        messageFail()
                    }
                }

                override fun onFailure(call: Call<ItunesResponse>, t: Throwable) {
                    messageFail()
                }
            })
    }

    private fun messageFail() {
        recyclerView?.visibility = View.INVISIBLE
        messageImg?.visibility = View.VISIBLE
        messageText?.visibility = View.VISIBLE
        messageBtn?.visibility = View.VISIBLE
        searchPgb?.visibility = View.GONE
        messageImg?.setImageResource(
            if (Configuration.UI_MODE_NIGHT_YES == resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) R.drawable.ic_message_fail_dark
            else R.drawable.ic_message_fail
        )
        messageText?.text = getString(R.string.message_fail)
    }

    private fun messageEmpty() {
        recyclerView?.visibility = View.INVISIBLE
        messageImg?.visibility = View.VISIBLE
        messageText?.visibility = View.VISIBLE
        messageBtn?.visibility = View.INVISIBLE
        searchPgb?.visibility = View.GONE
        messageImg?.setImageResource(
            if (Configuration.UI_MODE_NIGHT_YES == resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) R.drawable.ic_message_empty_dark
            else R.drawable.ic_message_empty
        )
        messageText?.text = getString(R.string.message_empty)
    }

    private fun messageOk() {
        recyclerView?.visibility = View.VISIBLE
        messageImg?.visibility = View.INVISIBLE
        messageBtn?.visibility = View.INVISIBLE
        searchPgb?.visibility = View.GONE
        messageText?.text = ""
    }

    private fun showHistory() {
        messageOk()
        val searchAdapter = SearchHistoryAdapter(this@SearchActivity) {
            if (clickDebounce()) {
                val intent = Intent(this, MediaActivity::class.java)
                intent.putExtra("track", Gson().toJson(it))
                startActivity(intent)
            }
        }
        recyclerView?.layoutManager = LinearLayoutManager(this@SearchActivity)
        recyclerView?.adapter = searchAdapter
        if (searchAdapter.itemCount != 0) {
            searchHistory?.visibility = View.VISIBLE
            searchHistoryBtn?.visibility = View.VISIBLE
        }
    }

    private fun goneHistory() {
        searchHistory?.visibility = View.GONE
        searchHistoryBtn?.visibility = View.GONE
        recyclerView?.visibility = View.GONE
        messageImg?.visibility = View.GONE
        messageText?.visibility = View.GONE
        searchPgb?.visibility = View.VISIBLE
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchText)
    }

    @SuppressLint("MissingInflatedId")
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(SEARCH_TEXT).toString()
    }

}