package com.example.morax.service

import com.example.morax.model.LoginReq
import com.example.morax.model.LoginResp
import com.example.morax.model.UserReq
import com.example.morax.model.UserResp
import reactor.core.publisher.Mono

interface UserService {
    fun createUser(userReq: UserReq): Mono<UserResp>
    fun authenticate(loginReq: LoginReq): Mono<LoginResp>
    fun getCurrentUser(): Mono<UserResp>
    fun getUserById(id: String): Mono<UserResp>
    fun searchUser(): Mono<List<UserResp>>
}