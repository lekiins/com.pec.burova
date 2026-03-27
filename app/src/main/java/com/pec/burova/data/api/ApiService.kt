package com.pec.burova.data.api

import com.pec.burova.data.models.LoginRequest
import com.pec.burova.data.models.Student
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("api/qr/login")
    suspend fun login(@Body request: LoginRequest): Response<Student>

    @GET("api/qr/student/{code}")
    suspend fun getStudent(@Path("code") code: String): Response<Student>

    @Multipart
    @POST("api/qr/student/{code}/avatar")
    suspend fun uploadAvatar(
        @Path("code") code: String,
        @Part avatar: MultipartBody.Part
    ): Response<Map<String, Any>>
}
