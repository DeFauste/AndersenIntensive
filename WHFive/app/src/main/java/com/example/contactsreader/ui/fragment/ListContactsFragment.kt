package com.example.contactsreader.ui.fragment

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contactsreader.R
import com.example.contactsreader.adapter.RecyclerListContactAdapter
import com.example.contactsreader.databinding.FragmentListContactsBinding
import com.example.contactsreader.model.UserContact
import com.example.contactsreader.ui.model.MainViewModel
import com.example.contactsreader.utilits.APP_ACTIVITY
import com.example.contactsreader.utilits.READ_WRITE
import com.example.contactsreader.utilits.checkPermission

class ListContactsFragment : Fragment() {
    private var _binding: FragmentListContactsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    private val adapterRecycler = RecyclerListContactAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*добавляем слушателя на изменение данных списка контактов*/
        subscribeToObservers()

        adapterRecycler.setItemClickListener {
            viewModel.currentUser.value = UserContact(it.uri, it.name, it.number)
            when (!viewModel.isTablet) {
                true -> findNavController().navigate(
                    R.id.editorContactFragment
                )
            }
        }
    }

    /*добавлем адаптер в recycler*/
    private fun setupRecyclerView() = binding.recycler.apply {
        adapter = adapterRecycler
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun subscribeToObservers() {
        viewModel.contactsLiveData.observe(viewLifecycleOwner) { result ->
            setupRecyclerView()
            adapterRecycler.contacts = result
        }
    }
}
