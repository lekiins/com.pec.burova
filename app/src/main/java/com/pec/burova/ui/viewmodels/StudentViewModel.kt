package com.pec.burova.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pec.burova.data.api.RetrofitInstance
import com.pec.burova.data.models.LoginRequest
import com.pec.burova.data.models.Student
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
}
