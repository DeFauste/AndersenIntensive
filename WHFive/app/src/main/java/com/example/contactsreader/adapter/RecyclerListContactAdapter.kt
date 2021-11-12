package com.example.contactsreader.adapter

import androidx.recyclerview.widget.AsyncListDiffer
import com.example.contactsreader.R
import com.example.contactsreader.model.UserContact
import kotlinx.android.synthetic.main.item_contact.view.*

class RecyclerListContactAdapter : BaseListContactAdapter(R.layout.item_contact) {
    override var differ: AsyncListDiffer<UserContact> = AsyncListDiffer(this, diffCallBack)

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.itemView.apply {
            contact_name.text = contact.name
            contact_number.text = contact.number
            setOnClickListener {
                onItemClickListener?.let { click ->
                    click(contact)
                }
            }
        }
    }
}
