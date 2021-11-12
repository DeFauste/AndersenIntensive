package com.example.contactsreader.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.contactsreader.model.UserContact

abstract class BaseListContactAdapter(private val layoutId: Int) :
    RecyclerView.Adapter<BaseListContactAdapter.ContactViewHolder>() {
    class ContactViewHolder(item: View) : RecyclerView.ViewHolder(item)

    protected val diffCallBack = object : DiffUtil.ItemCallback<UserContact>() {
        override fun areItemsTheSame(oldItem: UserContact, newItem: UserContact): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: UserContact, newItem: UserContact): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    protected abstract var differ: AsyncListDiffer<UserContact>

    var contacts: List<UserContact>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(
            LayoutInflater.from(parent.context).inflate(
                layoutId,
                parent,
                false
            )
        )
    }
    protected var onItemClickListener: ((UserContact) -> Unit)? = null

    fun setItemClickListener(listener: (UserContact) -> Unit) {
        onItemClickListener = listener
    }
    override fun getItemCount(): Int {
        return contacts.size
    }
}
