package com.example.morax.model

import com.example.morax.util.MoraxUtils
import org.bson.types.Binary
import org.springframework.web.multipart.MultipartFile

data class UserResp(
    val id: String = "",
    val username: String = "",
    val displayName: String = "",
    val email: String = "",
    val rankingPoint: Int,
    val balance: Int,
    val avatar: Binary
){
    constructor(user: User, rankingPoint: Int = 0, balance: Int = 0) : this(
        id = user.id,
        username = user.username,
        email = user.email,
        displayName = user.displayName,
        rankingPoint = rankingPoint,
        balance = balance,
        avatar = user.avatar
    )
}


data class UserReq(
    val username: String,
    val displayName: String,
    val email: String,
    var password: String,
    var rePassword: String,
    val avatar: MultipartFile
)

data class LoginReq(
    val email: String,
    val password: String
)

data class LoginResp(
    val user: UserResp,
    val accessKey: String,
    val refreshKey: String,
)

data class LocationReq(
    val name: String,
    val nameInMap: String,
    val latitude: Double,
    val longitude: Double,
    val image: MultipartFile,
    val description: String,
    val fact: String,
)

data class LocationResp(
    val id: String,
    val name: String,
    val nameInMap: String,
    val latitude: Double,
    val longitude: Double,
    val image: Binary,
    val description: String,
    var artifacts: List<Artifact>,
    val fact: String?,
) {
    constructor(location: Location): this(
        location.id,
        location.name,
        location.nameInMap,
        location.latitude,
        location.longitude,
        location.image,
        location.description,
        listOf(),
        location.fact
    )
}

data class ArtifactResp(
    val id: String,
    val name: String,
    val time: String,
    val locationId: String,
    val image: Binary,
    val description: String
) {
    constructor(artifact: Artifact): this(
        artifact.id,
        artifact.name,
        artifact.time,
        artifact.locationId,
        artifact.image,
        artifact.description
    )
}


data class ArtifactReq(
    val name: String,
    val time: String,
    val image: MultipartFile,
    val description: String
)

data class EventReq(
    val eventName: String,
    val time: String,
    val address: String,
    val image: MultipartFile,
)

data class EventResp(
    val id: String,
    val eventName: String,
    val time: String,
    val address: String,
    val image: Binary,
) {
    constructor(event: Event): this(
        event.id,
        eventName = event.eventName,
        time = event.time,
        address = event.address,
        image = event.image
    )
}

data class QuizReq(
    val locationId: String,
    val question: String,
    val point: Int,
    val correctAnswer: String,
    val image: MultipartFile,
    val description: String
)


data class QuizResp(
    val id: String,
    val locationId: String,
    val question: String,
    val point: Int,
    val correctAnswer: String,
    val image: Binary,
    val answers: List<AnswerResp>,
    val description: String?,
) {
    constructor(quiz: Quiz, answers: List<AnswerResp>) : this(
        quiz.id,
        quiz.locationId,
        quiz.question,
        quiz.point,
        quiz.correctAnswer,
        quiz.image,
        answers,
        quiz.description
    )
}

data class AnswerReq(
    val quizId: String,
    val answer: String,
)

data class AnswerResp(
    val id: String,
    val quizId: String,
    val answer: String
) {
    constructor(answer: Answer): this(
        answer.id,
        answer.quizId,
        answer.answer
    )
}

data class AnswerQuizResp(
    val id: String,
    val quiz: QuizResp,
    val isCorrect: Boolean = true,
    val point: Int
){
    constructor(quiz: QuizResp, isCorrect: Boolean): this(
        id = MoraxUtils.newUUID(),
        quiz = quiz,
        isCorrect = isCorrect,
        point = quiz.point
    )
}

data class SearchResp(
    val locations: List<LocationResp>,
    val artifacts: List<ArtifactResp>
)

data class MysteryItemReq(
    val name: String,
    val locationId: String,
    val point: Int,
    val foundImage: MultipartFile,
    val unfoundedImage: MultipartFile,
    val description: String,
)

data class MysteryItemResp(
    val id: String,
    val name: String,
    val locationId: String,
    val point: Int,
    val foundImage: Binary,
    val unfoundedImage: Binary,
    val isFound: Boolean = false,
    val foundAt: String? = null,
    val description: String?,
) {
    constructor(mysteryItem: MysteryItem, isFound: Boolean = false, foundAt: String? = null): this(
        mysteryItem.id,
        mysteryItem.name,
        mysteryItem.locationId,
        mysteryItem.point,
        mysteryItem.foundImage,
        mysteryItem.unfoundedImage,
        isFound,
        foundAt,
        mysteryItem.description
    )
}

data class FactReq(
    val content: String,
    val type: OriginalFactType,
    val originalId: String
)

data class FactResp(
    val id: String,
    val content: String,
    val type: OriginalFactType,
    val originalId: String
) {
    constructor(fact: Fact): this(
        fact.id,
        fact.content,
        fact.type,
        fact.originalId
    )
}

data class ChangePasswordReq(
    val oldPassword: String,
    val newPassword: String,
    val rePassword: String
)