package com.pec.burova.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pec.burova.data.api.RetrofitInstance
import com.pec.burova.data.models.LoginRequest
import com.pec.burova.data.models.Student
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val student: Student) : LoginState()
    data class Error(val message: String) : LoginState()
}

class StudentViewModel : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    private val _currentStudent = MutableStateFlow<Student?>(null)
    val currentStudent: StateFlow<Student?> = _currentStudent

    private val _uploadStatus = MutableStateFlow<String?>(null)
    val uploadStatus: StateFlow<String?> = _uploadStatus

    fun clearUploadStatus() { _uploadStatus.value = null }

    fun login(code: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val response = RetrofitInstance.apiService.login(LoginRequest(code))
                if (response.isSuccessful && response.body() != null) {
                    val student = response.body()!!
                    _currentStudent.value = student
                    _loginState.value = LoginState.Success(student)
                } else {
                    _loginState.value = LoginState.Error("Неверный код доступа")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Ошибка сети: ${e.message}")
            }
        }
    }

    fun logout() {
        _currentStudent.value = null
        _loginState.value = LoginState.Idle
    }

    fun uploadAvatar(imageBytes: ByteArray, contentType: String) {
        val student = _currentStudent.value ?: return
        viewModelScope.launch {
            try {
                // Compress image if it's too large (server limit 1MB)
                var finalBytes = imageBytes
                if (imageBytes.size > 1024 * 1024) {
                    val bitmap = android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    val out = java.io.ByteArrayOutputStream()
                    // Resize to max 1024px if needed
                    val scale = 1024f / Math.max(bitmap.width, bitmap.height).coerceAtLeast(1)
                    val resized = if (scale < 1f) {
                        android.graphics.Bitmap.createScaledBitmap(
                            bitmap, 
                            (bitmap.width * scale).toInt(), 
                            (bitmap.height * scale).toInt(), 
                            true
                        )
                    } else bitmap
                    resized.compress(android.graphics.Bitmap.CompressFormat.JPEG, 80, out)
                    finalBytes = out.toByteArray()
                    if (resized != bitmap) resized.recycle()
                    bitmap.recycle()
                }

                val requestBody = finalBytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("file", "avatar.jpg", requestBody)
                
                val response = RetrofitInstance.apiService.uploadAvatar(student.id, body)
                if (response.isSuccessful) {
                    // Refresh student data to get new avatar
                    val updatedResponse = RetrofitInstance.apiService.getStudent(student.id)
                    if (updatedResponse.isSuccessful) {
                        _currentStudent.value = updatedResponse.body()
                        _uploadStatus.value = "Success"
                    } else {
                        _uploadStatus.value = "Error: Refetch failed"
                    }
                } else {
                    _uploadStatus.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _uploadStatus.value = "Error: ${e.message}"
            }
        }
    }

    fun uploadAvatarFromUrl(url: String) {
        val student = _currentStudent.value ?: return
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            try {
                val client = okhttp3.OkHttpClient()
                val request = okhttp3.Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                
                if (response.isSuccessful && response.body != null) {
                    val bytes = response.body!!.bytes()
                    val contentType = response.body!!.contentType()?.toString() ?: "image/png"
                    
                    // Call the existing upload logic
                    uploadAvatar(bytes, contentType)
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
