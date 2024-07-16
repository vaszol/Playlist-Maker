package com.practicum.playlistmaker.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.domain.Creator
import com.practicum.playlistmaker.domain.api.TrackInteractor
import com.practicum.playlistmaker.domain.models.Resource
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.media.MediaActivity

class SearchActivity : AppCompatActivity() {
    lateinit var searchBinding: ActivitySearchBinding
    private var searchText: String = ""
    private val searchRunnable = Runnable { search() }
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true
    private lateinit var adapter: TracksAdapter
    private var creator = Creator.provideTracksInteractor()
    private var sharedPreferences = Creator.provideSharedPreferencesInteractor()

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(searchBinding.root)

        adapter = TracksAdapter(mutableListOf()) {
            if (clickDebounce()) {
                sharedPreferences.addHistory(it)
                val intent = Intent(this, MediaActivity::class.java)
                intent.putExtra("track", Gson().toJson(it))
                startActivity(intent)
            }
        }

        searchBinding.searchToolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        searchBinding.inputEditText.setText(searchText)
        searchBinding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
            }
            false
        }
        searchBinding.inputEditText.addTextChangedListener(
            onTextChanged = { s, _, _, _ ->
                run {
                    searchBinding.clearIcon.isVisible = s?.isNotEmpty() == true
                    if (searchBinding.inputEditText.hasFocus() && s?.isEmpty() == true)
                        showHistory()
                    else
                        searchDebounce()
                }
            }, afterTextChanged = { s -> searchText = s.toString() }
        )
        searchBinding.inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && searchBinding.inputEditText.text.isEmpty()) showHistory() else goneHistory()
        }
        searchBinding.clearIcon.setOnClickListener {
            searchBinding.inputEditText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchBinding.container.windowToken, 0)
        }
        searchBinding.messageBtn.setOnClickListener { search() }
        searchBinding.searchHistoryBtn.setOnClickListener {
            sharedPreferences.clearSharedPreference()
            goneHistory()
        }
    }

    public override fun onStart() {
        super.onStart()
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
            handler.postDelayed(
                { isClickAllowed = true },
                CLICK_DEBOUNCE_DELAY
            )
        }
        return current
    }

    private fun search() {
        goneHistory()
        searchBinding.searchPgb.visibility = View.VISIBLE
        adapter.updateList(mutableListOf())
        creator.searchTracks(
            searchBinding.inputEditText.text.toString(),
            object : TrackInteractor.TracksConsumer {
                override fun consume(foundTracks: Resource<List<Track>>) {
                    runOnUiThread {
                        when (foundTracks) {
                            is Resource.Error -> messageFail()
                            is Resource.Success ->
                                if (foundTracks.data.isNullOrEmpty())
                                    messageEmpty()
                                else {
                                    messageOk()
                                    searchBinding.trackRecyclerView.layoutManager =
                                        LinearLayoutManager(this@SearchActivity)
                                    searchBinding.trackRecyclerView.adapter = adapter
                                    adapter.updateList(foundTracks.data)
                                }
                        }
                    }
                }
            }
        )
    }

    private fun messageFail() {
        searchBinding.trackRecyclerView.visibility = View.INVISIBLE
        searchBinding.messageImg.visibility = View.VISIBLE
        searchBinding.messageText.visibility = View.VISIBLE
        searchBinding.messageBtn.visibility = View.VISIBLE
        searchBinding.searchPgb.visibility = View.GONE
        searchBinding.messageImg.setImageResource(
            if (Configuration.UI_MODE_NIGHT_YES == resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) R.drawable.ic_message_fail_dark
            else R.drawable.ic_message_fail
        )
        searchBinding.messageText.text = getString(R.string.message_fail)
    }

    private fun messageEmpty() {
        searchBinding.trackRecyclerView.visibility = View.INVISIBLE
        searchBinding.messageImg.visibility = View.VISIBLE
        searchBinding.messageText.visibility = View.VISIBLE
        searchBinding.messageBtn.visibility = View.INVISIBLE
        searchBinding.searchPgb.visibility = View.GONE
        searchBinding.messageImg.setImageResource(
            if (Configuration.UI_MODE_NIGHT_YES == resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) R.drawable.ic_message_empty_dark
            else R.drawable.ic_message_empty
        )
        searchBinding.messageText.text = getString(R.string.message_empty)
    }

    @SuppressLint("SetTextI18n")
    private fun messageOk() {
        searchBinding.trackRecyclerView.visibility = View.VISIBLE
        searchBinding.messageImg.visibility = View.INVISIBLE
        searchBinding.messageBtn.visibility = View.INVISIBLE
        searchBinding.searchPgb.visibility = View.GONE
        searchBinding.messageText.text = ""
    }

    private fun showHistory() {
        messageOk()
        searchBinding.trackRecyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)
        searchBinding.trackRecyclerView.adapter = adapter
        adapter.updateList(
            sharedPreferences.getFromHistory().reversed().toMutableList()
        )
        if (adapter.itemCount != 0) {
            searchBinding.searchHistory.visibility = View.VISIBLE
            searchBinding.searchHistoryBtn.visibility = View.VISIBLE
        }
    }

    private fun goneHistory() {
        searchBinding.searchHistory.visibility = View.GONE
        searchBinding.searchHistoryBtn.visibility = View.GONE
        searchBinding.trackRecyclerView.visibility = View.GONE
        searchBinding.messageImg.visibility = View.GONE
        searchBinding.messageText.visibility = View.GONE
        searchBinding.searchPgb.visibility = View.GONE
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