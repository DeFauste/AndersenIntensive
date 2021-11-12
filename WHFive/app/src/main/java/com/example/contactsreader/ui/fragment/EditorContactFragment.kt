package com.example.contactsreader.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.contactsreader.R
import com.example.contactsreader.databinding.FragmentEditorContactBinding
import com.example.contactsreader.ui.model.MainViewModel
import com.example.contactsreader.utilits.READ_WRITE
import com.example.contactsreader.utilits.checkPermission

class EditorContactFragment : Fragment() {
    private var _binding: FragmentEditorContactBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    private var uri: Uri? = null
    private var name: String? = null
    private var number: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditorContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*Проверем изменения текужего редактируемуго пользователя, если такой есть, то производим его редактирвоание*/
        viewModel.currentUser.observe(viewLifecycleOwner) {
            binding.saveBt.isEnabled = it != null
            if (it != null) {
                uri = it.uri
                name = it.name
                number = it.number
                binding.contactName.setText(name)
                binding.contactNumber.setText(number)
            }
        }

        /*слушаем нажатие кнопки сохранения*/
        binding.saveBt.setOnClickListener {
            /*если EditText пустые, то не производим действий*/
            if (binding.contactName.text.isNotEmpty() && binding.contactNumber.text.isNotEmpty()) {
                /*Проверяем планшет перед нами или смартфон*/
                if (!viewModel.isTablet) {
                    /*Если смартфон то производим переключение фрагментов*/
                    findNavController().navigate(
                        R.id.action_editorContactFragment_to_listContactsFragment,
                        null
                    )
                }
                /*передаем новые данные контакта*/
                viewModel.editContact(
                    uri!!,
                    binding.contactName.text.toString(),
                    binding.contactNumber.text.toString()
                )
                /*очищаем поля и убираем с них фокус*/
                binding.apply {
                    contactName.clearFocus()
                    contactName.text.clear()

                    contactNumber.clearFocus()
                    contactNumber.text.clear()
                }
            }
        }
    }
}
