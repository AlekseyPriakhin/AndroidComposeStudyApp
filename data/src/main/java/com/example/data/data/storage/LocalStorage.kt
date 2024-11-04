package com.example.data.data.storage

import android.content.SharedPreferences
import androidx.core.content.edit

class LocalStorage(private val sharedPreferences: SharedPreferences): ILocalStorage {
    override fun markAsRead(id: Int) {
        if (!isRead(id)) sharedPreferences.edit {
            putBoolean("read_$id", true)
        }
    }

    override fun isRead(id: Int): Boolean {
        return sharedPreferences.getBoolean("read_${id}", false)
    }

}