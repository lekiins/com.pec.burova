package com.pec.burova.data.models

import com.google.gson.annotations.SerializedName

data class Student(
    @SerializedName("id") val id: String,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("organization") val organization: String,
    @SerializedName("issue_date") val issueDate: String,
    @SerializedName("specialty") val specialty: String,
    @SerializedName("course") val course: String,
    @SerializedName("hash") val hash: String? = null,
    @SerializedName("avatar_base64") val avatarBase64: String? = null
) {
    fun getAvatarBytes(): ByteArray? {
        if (avatarBase64.isNullOrBlank() || !avatarBase64.contains("base64,")) return null
        return try {
            android.util.Base64.decode(avatarBase64.substringAfter("base64,"), android.util.Base64.DEFAULT)
        } catch (e: Exception) {
            null
        }
    }
}

data class LoginRequest(
    @SerializedName("code") val code: String,
    @SerializedName("mood") val mood: String = "neutral"
)
