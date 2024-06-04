package com.rpl.sicfo.ui.voting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rpl.sicfo.R
import com.rpl.sicfo.databinding.FragmentVotingBinding


class VotingFragment : Fragment() {

    private lateinit var binding: FragmentVotingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVotingBinding.inflate(inflater, container, false)
        return binding.root
    }
}