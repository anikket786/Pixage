package com.app.general.pixage.images.presentation.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.app.general.pixage.R
import com.app.general.pixage.common.util.ext.executeWithAction
import com.app.general.pixage.databinding.FragmentImagePostDetailBinding
import com.app.general.pixage.images.presentation.state.ImagePostsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImagePostDetailFragment : Fragment() {

    private var binding: FragmentImagePostDetailBinding? = null

    private val viewModel: ImagePostsViewModel by hiltNavGraphViewModels(R.id.image_posts_nav_graph)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupOnBackPressInterceptor()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_image_post_detail, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
        binding?.executeWithAction {
            imagePost = viewModel.selectedPost.value
        }
    }

    private fun setupListener() {
        binding?.toolBar?.setNavigationOnClickListener {
            // Reset the selected post state before exiting this fragment
            resetAndPopCurrentFragment()
        }
    }

    private fun setupOnBackPressInterceptor() {
        activity?.onBackPressedDispatcher?.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Reset the selected post state before exiting this fragment
                    resetAndPopCurrentFragment()
                }
            })
    }

    private fun resetAndPopCurrentFragment() {
        viewModel.setSelectedImagePost(null)
        findNavController().popBackStack()
    }
}