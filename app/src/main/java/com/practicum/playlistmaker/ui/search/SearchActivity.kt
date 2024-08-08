package com.practicum.playlistmaker.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.ui.media.MediaActivity
import com.practicum.playlistmaker.ui.search.viewModel.SearchViewModel

class SearchActivity : AppCompatActivity() {
    private lateinit var searchBinding: ActivitySearchBinding
    private lateinit var adapter: TracksAdapter
    private lateinit var viewModel: SearchViewModel

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(searchBinding.root)
        viewModel = ViewModelProvider(
            this, SearchViewModel.getViewModelFactory(application)
        )[SearchViewModel::class.java]

        adapter = TracksAdapter(viewModel::onTrackClick)

        searchBinding.apply {
            trackRecyclerView.adapter = adapter
            searchToolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
            clearIcon.setOnClickListener { viewModel.clearText() }
            messageBtn.setOnClickListener { viewModel.search() }
            searchHistoryBtn.setOnClickListener { viewModel.clearHistory() }
            inputEditText.apply {
                doOnTextChanged { searchText, _, _, _ ->
                    run {
                        searchBinding.clearIcon.isVisible = searchText?.isNotEmpty() == true
                        if (searchBinding.inputEditText.hasFocus() && searchText?.isEmpty() == true)
                            viewModel.showHistory()
                        else
                            viewModel.searchDebounce()
                    }
                }
                doAfterTextChanged { searchText -> viewModel.doAfterTextChanged(searchText.toString()) }
                setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        viewModel.search()
                    }
                    false
                }
                setOnFocusChangeListener { _, hasFocus -> viewModel.focusChange(hasFocus) }
            }
        }

        viewModel.state.observe(this) {
            searchBinding.apply {
                searchHistory.isVisible = it.searchHistoryVisible && it.tracks.isNotEmpty()
                searchHistoryBtn.isVisible = it.searchHistoryVisible && it.tracks.isNotEmpty()
                messageImg.isVisible = it.messageVisible
                messageText.isVisible = it.messageVisible
                messageBtn.isVisible = it.messageVisible
                searchPgb.isVisible = it.searchPgbVisible
                adapter.updateList(it.tracks)
                trackRecyclerView.isVisible = it.tracks.isNotEmpty()

                if (it.messageFail) {
                    messageImg.setImageResource(
                        if (Configuration.UI_MODE_NIGHT_YES == resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) R.drawable.ic_message_fail_dark
                        else R.drawable.ic_message_fail
                    )
                    searchBinding.messageText.text = getString(R.string.message_fail)
                } else {
                    messageImg.setImageResource(
                        if (Configuration.UI_MODE_NIGHT_YES == resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) R.drawable.ic_message_empty_dark
                        else R.drawable.ic_message_empty
                    )
                    searchBinding.messageText.text = getString(R.string.message_empty)
                }
            }
        }

        viewModel.event.observe(this) {
            when (it) {
                is SearchScreenEvent.OpenPlayerScreen -> openPlayer()
                is SearchScreenEvent.ClearSearch -> searchBinding.inputEditText.text.clear()
                else -> hideKeyboard()
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        viewModel.showHistory()
    }

    private fun openPlayer() {
        Intent(this, MediaActivity::class.java).apply {
            putExtra("track", Creator.getGson().toJson(viewModel.state.value?.trackSelected))
            startActivity(this)
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchBinding.clearIcon.windowToken, 0)
    }
}