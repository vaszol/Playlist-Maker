package com.practicum.playlistmaker.ui.search

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.ui.search.viewModel.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private lateinit var searchBinding: FragmentSearchBinding
    private lateinit var adapter: TracksAdapter
    private val viewModel by viewModel<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        searchBinding = FragmentSearchBinding.inflate(layoutInflater)
        return searchBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = TracksAdapter(viewModel::onTrackClick)
        searchBinding.apply {
            trackRecyclerView.adapter = adapter
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
        viewModel.state.observe(viewLifecycleOwner) {
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

        viewModel.event.observe(viewLifecycleOwner) {
            when (it) {
                SearchScreenEvent.OpenPlayerScreen -> findNavController().navigate(R.id.action_searchFragment_to_playerFragment)
                SearchScreenEvent.ClearSearch -> searchBinding.inputEditText.text.clear()
                else -> hideKeyboard()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.showTracks()
    }

    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchBinding.clearIcon.windowToken, 0)
    }
}