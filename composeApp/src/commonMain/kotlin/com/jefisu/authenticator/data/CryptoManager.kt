@file:OptIn(ExperimentalEncodingApi::class, InternalCoroutinesApi::class)

package com.jefisu.authenticator.data

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.algorithms.AES
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

object CryptoManager {

    private const val KEY = "KEY"
    private val aesGcm = CryptographyProvider.Default.get(AES.GCM)
    private val settings = Settings()

    private var cachedKey: AES.GCM.Key? = null
    private val mutex = Mutex()

    private suspend fun createKey(): AES.GCM.Key {
        val keyGenerator = aesGcm.keyGenerator(AES.Key.Size.B256)
        val key = keyGenerator.generateKey()
        val encodedKey = key.encodeToByteArray(AES.Key.Format.RAW)
        settings[KEY] = Base64.encode(encodedKey)
        return key
    }

    private suspend fun getKey(): AES.GCM.Key {
        cachedKey?.let { return it }

        return mutex.withLock {
            val encodedBase64Key = settings.getStringOrNull(KEY) ?: return createKey()
            val encodedBytesKey = Base64.decode(encodedBase64Key)
            val keyDecoder = aesGcm.keyDecoder()
            keyDecoder
                .decodeFromByteArray(AES.Key.Format.RAW, encodedBytesKey)
                .also { cachedKey = it }
        }
    }

    suspend fun encrypt(plaintext: String): String {
        val cipher = getKey().cipher()
        val encryptedBytes = cipher.encrypt(plaintext.encodeToByteArray())
        val base64 = Base64.encode(encryptedBytes)
        return base64
    }

    suspend fun decrypt(cipherBase64: String): String {
        val cipher = getKey().cipher()
        val decodedBytes = Base64.decode(cipherBase64)
        val decryptedBytes = cipher.decrypt(decodedBytes)
        return decryptedBytes.decodeToString()
    }
}
