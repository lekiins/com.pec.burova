package com.pec.burova.data.models

import com.google.gson.annotations.SerializedName

data class Student(
    @SerializedName("id") val id: String,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("organization") val organization: String,
    @SerializedName("issue_date") val issueDate: String,
    @SerializedName("specialty") val specialty: String,
    @SerializedName("course") val course: String,
    @SerializedName("hash") val hash: String? = null
)

data class LoginRequest(
    @SerializedName("code") val code: String,
    @SerializedName("mood") val mood: String = "neutral"
)
