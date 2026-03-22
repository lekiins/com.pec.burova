package com.pec.burova.data.api

import com.pec.burova.data.models.LoginRequest
import com.pec.burova.data.models.Student
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("api/qr/login")
    suspend fun login(@Body request: LoginRequest): Response<Student>

    @GET("api/qr/student/{code}")
    suspend fun getStudent(@Path("code") code: String): Response<Student>
}
