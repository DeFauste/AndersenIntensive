package com.example.contactsreader.database

import android.content.ContentUris
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.StructuredName
import androidx.core.content.ContextCompat
import com.example.contactsreader.model.UserContact
import com.example.contactsreader.utilits.APP_ACTIVITY
import com.example.contactsreader.utilits.READ_WRITE

class DatabaseContact {

    /*Функция для редактирования контакта*/
    fun editContact(lastContactUri: Uri, newName: String, number: String) {
        /*удаляем контакт*/
        APP_ACTIVITY.contentResolver.delete(lastContactUri, null, null)

        /*создаем новый uri для контакта*/
        val rawContactUri: Uri? =
            APP_ACTIVITY.contentResolver.insert(
                ContactsContract.RawContacts.CONTENT_URI,
                ContentValues()
            )

        /* Получаем id добавленного контакта */
        /* Получаем id добавленного контакта */
        val rawContactId = ContentUris.parseId(rawContactUri!!)
        val values = ContentValues()

        /* Связываем наш аккаунт с данными */
        /* Связываем наш аккаунт с данными */
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
        /* Устанавливаем MIMETYPE для поля данных */
        /* Устанавливаем MIMETYPE для поля данных */
        values.put(
            ContactsContract.Data.MIMETYPE,
            StructuredName.CONTENT_ITEM_TYPE
        )
        /* Имя для нашего аккаунта */
        /* Имя для нашего аккаунта */
        values.put(StructuredName.DISPLAY_NAME, newName)
        APP_ACTIVITY.contentResolver.insert(ContactsContract.Data.CONTENT_URI, values)

        values.clear()

        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
        /* Тип данных – номер телефона */
        values.put(
            ContactsContract.Data.MIMETYPE,
            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
        )
        /* Номер телефона */
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, number)
        /* Тип – мобильный */
        values.put(
            ContactsContract.CommonDataKinds.Phone.TYPE,
            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
        )
        APP_ACTIVITY.contentResolver.insert(ContactsContract.Data.CONTENT_URI, values)
    }

    fun initContacts(): ArrayList<UserContact> {
        val arrayContacts = arrayListOf<UserContact>()

        arrayContacts.clear()
        /* Функция считывает контакты с телефонной книги, хаполняет массив arrayContacts моделями */
        if (ContextCompat.checkSelfPermission(
                APP_ACTIVITY,
                READ_WRITE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            /*Получаем доступ к контактам телефона*/
            val phones = APP_ACTIVITY.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null
            )
            /*если cursor не null, то производим получение данных*/
            phones?.let {
                while (it.moveToNext()) {
                    val lookupKey =
                        it.getString(it.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY))
                    val uri =
                        Uri.withAppendedPath(
                            ContactsContract.Contacts.CONTENT_LOOKUP_URI,
                            lookupKey
                        )

                    val name: String =
                        it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    val phoneNumber: String =
                        it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    arrayContacts.add(UserContact(uri, name, phoneNumber))
                }
            }
            /*закрываем отработавший курсов*/
            phones?.close()
        }
        return arrayContacts
    }
}
