package com.example.contactsreader.ui.model

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.contactsreader.database.DatabaseContact
import com.example.contactsreader.model.UserContact
import com.example.contactsreader.utilits.APP_ACTIVITY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    /*экземляр бызы данных пользователей*/
    private val databaseContact = DatabaseContact()

    /*если пользователь решил редактировать контакты, то создаем пользователя*/
    val currentUser: MutableLiveData<UserContact> by lazy {
        MutableLiveData<UserContact>()
    }
    /*аргумент для сохранения проверки на планшет*/
    var isTablet: Boolean = false

    init {
        /*при создании ViewModel, сразу инициализируем первичные данные*/
        isTablet()
        currentUser.value = null
    }
    /*создаем список пользователей полученных из базы контактов*/
    val contactsLiveData: MutableLiveData<ArrayList<UserContact>> by lazy {
        MutableLiveData<ArrayList<UserContact>>()
    }
    /*получаем список контактов и записываем его в contactsLiveData*/
    var listIsUpdate = false
    fun getAllPhoneContacts() {
        CoroutineScope(Dispatchers.IO).launch {
            contactsLiveData.postValue(databaseContact.initContacts())
        }
    }
    /*передаем базе данных информацию о том какого пользователя хотим удалить*/
    fun editContact(lastContactUri: Uri, newName: String, number: String) {
        databaseContact.editContact(lastContactUri, newName, number)
        getAllPhoneContacts()
        currentUser.value = null
        contactsLiveData.value?.clear()
    }
    /*функция проверки на планшет*/
    fun isTablet() {
        isTablet = APP_ACTIVITY.resources
            .configuration.smallestScreenWidthDp >= 600
    }
}
