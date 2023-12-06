package com.example.morax.util

import com.example.morax.model.ChangePasswordReq
import com.example.morax.model.User
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException

@Component
class Validator(val passwordEncoder: PasswordEncoder) {
    fun validChangePassword(changePasswordReq: ChangePasswordReq, userPassword: String){
        if(changePasswordReq.newPassword != changePasswordReq.rePassword) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Confirm password do not match with new password, please try again")
        }

        if(!passwordEncoder.matches(changePasswordReq.oldPassword, userPassword)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is incorrect")
        }
    }
}