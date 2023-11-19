package com.yourbynn.app_githubusers.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.yourbynn.app_githubusers.adapter.ListAdapter
import com.yourbynn.app_githubusers.data.remote.User
import com.yourbynn.app_githubusers.databinding.FragmentFollowingBinding
import com.yourbynn.app_githubusers.model.FollowingViewModel

class FollowingFragment : Fragment() {

    private val modelFollowingView by viewModels<FollowingViewModel>()
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private lateinit var listAdapter: ListAdapter

    private var username: String? = null

    companion object {
        const val ARG_USERNAME = "arg_username"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowingBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        username = arguments?.getString(ARG_USERNAME)

        listAdapter = ListAdapter()
        listAdapter.setOnItemClickCallback(object : ListAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                Intent(requireContext(), DetailsActivity::class.java).also {
                    it.putExtra(DetailsActivity.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailsActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailsActivity.EXTRA_HTML, data.htmlUrl)
                    it.putExtra(DetailsActivity.EXTRA_AVATAR, data.avatarUrl)
                    startActivity(it)
                }
            }
        })
        binding.rvFollowing.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            hasFixedSize()
            adapter = listAdapter
        }
        modelFollowingView.isLoadingFollowing.observe(viewLifecycleOwner) {
            toggleLoadingView(it)
        }

        modelFollowingView.getFollowingUser(username)
        modelFollowingView.followingUser.observe(requireActivity()) {
            if (it != null) {
                listAdapter.updateList(it)
                toggleLoadingView(false)
            }
        }

    }


    private fun toggleLoadingView(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarFollowing.visibility = View.VISIBLE
        } else {
            binding.progressBarFollowing.visibility = View.INVISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
