package com.app.general.pixage.images.presentation.screen

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.app.general.pixage.R
import com.app.general.pixage.common.util.ext.collect
import com.app.general.pixage.common.util.ext.collectLast
import com.app.general.pixage.common.util.ext.executeWithAction
import com.app.general.pixage.common.util.ext.safeNavigate
import com.app.general.pixage.common.util.isNetworkAvailable
import com.app.general.pixage.databinding.FragmentImagePostListBinding
import com.app.general.pixage.images.domain.entity.ImagePost
import com.app.general.pixage.images.presentation.components.ImagePostsAdapter
import com.app.general.pixage.images.presentation.components.ImagePostsUiState
import com.app.general.pixage.images.presentation.state.ImagePostsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
class ImagePostListFragment : Fragment() {

    private var binding: FragmentImagePostListBinding? = null

    private val viewModel: ImagePostsViewModel by hiltNavGraphViewModels(R.id.image_posts_nav_graph)

    lateinit var imagePostsAdapter: ImagePostsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_image_post_list,
            container,
            false,
        )
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setImagePostSelectionObserver()
        setAdapter()
        setListener()
        collectLast(viewModel.imagePosts, ::setImagePosts)
    }

    private fun setAdapter() {
        imagePostsAdapter = ImagePostsAdapter(viewModel)
        collect(
            flow = imagePostsAdapter.loadStateFlow
                .distinctUntilChangedBy { it.source.refresh }
                .map { it.refresh },
            action = ::setImagePostsUiState
        )
        binding?.rvImagePosts?.adapter = imagePostsAdapter
    }

    private fun setImagePostsUiState(loadState: LoadState) {
        binding?.executeWithAction {
            imagePostsUiState = ImagePostsUiState(loadState)
        }
    }

    private suspend fun setImagePosts(imagePostsPagingData: PagingData<ImagePost>) {
        imagePostsAdapter.submitData(imagePostsPagingData)
    }

    private fun setImagePostSelectionObserver() {
        viewModel.selectedPost.observe(viewLifecycleOwner) { imagePost ->
            if (imagePost != null) {
                showImageDetailsConfirmationDialog()
            }
        }
    }

    private fun setListener() {
        setupSearchViewListener()
    }

    private fun setupSearchViewListener() {
        val listener = Toolbar.OnMenuItemClickListener { item ->
            when (item?.itemId) {
                R.id.item_menu_search -> {
                    setupSearchViewListeners(item)
                    true
                }
                else -> false
            }
        }
        binding?.toolBar?.setOnMenuItemClickListener(listener)
    }

    private fun setupSearchViewListeners(item: MenuItem) {
        setupOnSearchClickedListener(item)
        setupOnBackPressListener(item)
    }

    private fun setupOnSearchClickedListener(item: MenuItem) {
        val searchView = item.actionView as? SearchView
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    context?.let { ctx ->
                        if (isNetworkAvailable(ctx)) {
                            refreshImageList(it)
                        } else {
                            Toast.makeText(
                                context, R.string.network_error_message, Toast.LENGTH_LONG,
                            ).show()
                        }
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun refreshImageList(query: String) {
        viewModel.searchText = query
        imagePostsAdapter.refresh()
    }

    private fun setupOnBackPressListener(item: MenuItem) {
        val searchView = item.actionView as? SearchView
        searchView?.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if(!hasFocus) {
                item.collapseActionView()
            }
        }
    }

    private fun showImageDetailsConfirmationDialog() {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setMessage(R.string.image_details_dialog_message)
        dialogBuilder.setCancelable(false)
        dialogBuilder.setPositiveButton(R.string.yes) { dialog, _ ->
            dialog.dismiss()
            val action = ImagePostListFragmentDirections
                .actionImagePostListFragmentToImagePostDetailFragment()
            findNavController().safeNavigate(action)
        }
        dialogBuilder.setNegativeButton(R.string.no) { dialog, _ ->
            dialog.dismiss()
        }
        dialogBuilder.create().show()
    }
}