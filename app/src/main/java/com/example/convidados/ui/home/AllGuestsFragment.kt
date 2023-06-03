package com.example.convidados.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.convidados.GuestFormActivity
import com.example.convidados.databinding.FragmentAllGuestsBinding
import com.example.convidados.ui.listener.OnGuestListener
import constants.DataBaseConstants

class AllGuestsFragment : Fragment() {

    private var _binding: FragmentAllGuestsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel : GuestsViewModel
    private val adapter = GuestsAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
         viewModel = ViewModelProvider(this).get(GuestsViewModel::class.java)

        _binding = FragmentAllGuestsBinding.inflate(inflater, container, false)

        binding.recyclerGuests.layoutManager = LinearLayoutManager(context)

        binding.recyclerGuests.adapter = adapter

        val listener = object : OnGuestListener() {
            override fun onClick(id : Int) {
                val intent = Intent(context, GuestFormActivity::class.java)
                val bundle = Bundle()
                bundle.putInt(DataBaseConstants.GUEST.COLUMNS.ID, id)
                intent.putExtras(bundle)

                startActivity(intent)
            }

            override fun onDelete(id : Int) {
                viewModel.delete(id)
                viewModel.getAll()
            }
        }
        adapter.attachListener(listener)
        viewModel.getAll()
        observe()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAll()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observe() {
        viewModel.guests.observe(viewLifecycleOwner) {
            adapter.updatedGuests(it)
        }
    }


}