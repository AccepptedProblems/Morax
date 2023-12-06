package com.example.morax.service

import com.example.morax.config.security.JwtProvider
import com.example.morax.model.*
import com.example.morax.repo.PointRepoImpl
import com.example.morax.repo.TokenRepoImpl
import com.example.morax.repo.UserRepoImpl
import com.example.morax.util.MoraxUtils
import com.example.morax.util.Validator
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

@Service
class UserServiceImpl(
    val userRepo: UserRepoImpl,
    val tokenRepo: TokenRepoImpl,
    val pointRepo: PointRepoImpl,
    val authenticationManager: AuthenticationManager,
    val jwtProvider: JwtProvider,
    val passwordEncoder: PasswordEncoder,
    val validator: Validator
) : UserService {
    override fun createUser(@RequestBody userReq: UserReq): Mono<UserResp> {
        userReq.password = passwordEncoder.encode(userReq.password)
        val user = userRepo.addUser(userReq)
        return Mono.just(UserResp(user))
    }

    override fun authenticate(loginReq: LoginReq): Mono<LoginResp> {
        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    loginReq.email,
                    loginReq.password
                )
            )
            val user = userRepo.findUserByEmail(loginReq.email)
            val jwtToken = jwtProvider.generateToken(user)
            val refreshToken = jwtProvider.generateRefreshToken(user)
            saveToken(user, jwtToken)
            return Mono.just(LoginResp(UserResp(user), jwtToken, refreshToken))
        } catch (e: BadCredentialsException) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Wrong username or password")
        }
    }

    override fun updateUser(userReq: UserReq, userId: String): UserResp {
        userReq.password = passwordEncoder.encode(userReq.password)
        val user = userRepo.findUserById(userId)
        val updatedUser = user.update(userReq)
        return UserResp(userRepo.updateUser(updatedUser))
    }

    override fun getCurrentUser(): Mono<UserResp> {
        val id = User.currentUser.id
        return getUserById(id)
    }

    override fun getUserById(id: String): Mono<UserResp> {
        val user = userRepo.findUserById(id)
        val userPoints: Int = pointRepo.pointsByUserId(user.id).sumOf { it.point }
        val balance = 0 //TODO: Add balance
        val userResp = UserResp(user, rankingPoint = userPoints, balance = balance)
        return Mono.just(userResp)
    }

    override fun searchUser(): Mono<List<UserResp>> {
        TODO("Not yet implemented")
    }

    override fun changePassword(changePasswordReq: ChangePasswordReq): UserResp {
        val userId = User.currentUser.id
        val user = userRepo.findUserById(userId)
        validator.validChangePassword(changePasswordReq, user.password)
        val newPassword = passwordEncoder.encode(changePasswordReq.newPassword)
        val updatedUser = user.updatePassword(newPassword)
        return UserResp(userRepo.updateUser(updatedUser))
    }

    fun saveToken(user: User, jwtToken: String) {
        val tokenId = MoraxUtils.newUUID()
        val token = Token(id = tokenId, token = jwtToken, userId = user.id)
        tokenRepo.addToken(token)
    }
}