package com.sti.sticanteen.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.sti.sticanteen.AppUserPreference
import com.sti.sticanteen.data.local.datastore.AppUserSerializer
import com.sti.sticanteen.utils.Constants.DATA_STORE_FILE_NAME


object DataStoreModule {

    val Context.appUserDataStore: DataStore<AppUserPreference> by dataStore(
        fileName = DATA_STORE_FILE_NAME,
        serializer = AppUserSerializer
    )
}