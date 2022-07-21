@file:Suppress("BlockingMethodInNonBlockingContext")

package com.sti.sticanteen.data.local.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.sti.sticanteen.AppUserPreference
import java.io.InputStream
import java.io.OutputStream

object AppUserSerializer : Serializer<AppUserPreference> {
    override val defaultValue: AppUserPreference
        get() = AppUserPreference.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): AppUserPreference {
        return try {
            AppUserPreference.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("cannot read protobuf corrupted version", e)
        }
    }


    override suspend fun writeTo(t: AppUserPreference, output: OutputStream) = t.writeTo(output)
}