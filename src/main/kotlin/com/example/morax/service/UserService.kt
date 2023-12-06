package com.example.morax.service

import com.example.morax.model.*
import reactor.core.publisher.Mono

interface UserService {
    fun createUser(userReq: UserReq): Mono<UserResp>
    fun authenticate(loginReq: LoginReq): Mono<LoginResp>
    fun updateUser(userReq: UserReq, userId: String): UserResp
    fun getCurrentUser(): Mono<UserResp>
    fun getUserById(id: String): Mono<UserResp>
    fun searchUser(): Mono<List<UserResp>>
    fun changePassword(changePasswordReq: ChangePasswordReq): UserResp
}