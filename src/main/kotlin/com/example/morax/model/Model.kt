package com.example.morax.model

import com.example.morax.util.MoraxUtils
import org.bson.BsonBinarySubType
import org.bson.types.Binary
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.time.LocalDate
import java.util.*
import java.util.stream.Collectors

interface FoocationIdentity {
    var id: String
    val username: String
    val email: String
    val displayName: String
    val password: String
    val role: Role
    val avatar: Binary
}

@Document
data class User(
    @Id override var id: String = "",
    @Indexed(unique = true) override val username: String = "",
    @Indexed(unique = true) override val email: String = "",
    override val displayName: String = "",
    override var password: String = "",
    override val role: Role = Role.USER,
    override val avatar: Binary = Binary(byteArrayOf())

) : FoocationIdentity {
    companion object {
        val currentUser: User = User()

        fun newUser(userReq: UserReq): User {
            val id = MoraxUtils.newUUID()
            return User(
                id,
                username = userReq.username,
                email = userReq.email,
                displayName = userReq.displayName,
                password = userReq.password,
                role = Role.USER,
                if (userReq.avatar != null) Binary(BsonBinarySubType.BINARY, userReq.avatar.bytes) else Binary(
                    byteArrayOf()
                ),
            )
        }
    }

    fun updatePassword(newPassword: String): User {
        return User(
            id,
            username,
            email,
            displayName,
            newPassword,
            role,
            avatar
        )
    }

    fun update(userReq: UserReq): User {
        return User(
            this.id,
            userReq.username,
            userReq.email,
            userReq.displayName,
            userReq.password,
            this.role,
            if (userReq.avatar != null) Binary(BsonBinarySubType.BINARY, userReq.avatar.bytes) else Binary(
                byteArrayOf()
            ),
        )
    }
}

data class Staff(
    override var id: String,
    override val username: String,
    override val email: String,
    override val displayName: String,
    override val password: String,
    override val role: Role = Role.MANAGER,
    val phoneNumber: String,
    override val avatar: Binary
) : FoocationIdentity

data class Artifact(
    val id: String,
    val name: String,
    val time: String,
    val locationId: String,
    val image: Binary,
    val description: String
) {
    constructor(artifactReq: ArtifactReq, locationId: String) : this(
        MoraxUtils.newUUID(),
        artifactReq.name,
        artifactReq.time,
        locationId,
        Binary(BsonBinarySubType.BINARY, artifactReq.image.bytes),
        artifactReq.description
    )

    fun update(artifactReq: ArtifactReq, locationId: String): Artifact {
        return this.copy(
            id = this.id,
            name = artifactReq.name,
            time = artifactReq.time,
            locationId = locationId,
            image = Binary(BsonBinarySubType.BINARY, artifactReq.image.bytes),
            description = artifactReq.description
        )
    }
}

data class Fact(
    val id: String,
    val content: String,
    val type: OriginalFactType,
    val originalId: String
) {
    constructor(factReq: FactReq) : this(
        MoraxUtils.newUUID(),
        factReq.content,
        factReq.type,
        factReq.originalId
    )
}

data class Location(
    val id: String,
    val name: String,
    val nameInMap: String,
    val latitude: Double,
    val longitude: Double,
    val image: Binary,
    val description: String,
    val fact: String?,
) {
    constructor(locationReq: LocationReq) : this(
        MoraxUtils.newUUID(),
        locationReq.name,
        locationReq.nameInMap,
        locationReq.latitude,
        locationReq.longitude,
        Binary(BsonBinarySubType.BINARY, locationReq.image.bytes),
        locationReq.description,
        locationReq.fact
    )

    fun update(locationReq: LocationReq): Location {
        return this.copy(
            id = this.id,
            name = locationReq.name,
            nameInMap = locationReq.nameInMap,
            latitude = locationReq.latitude,
            longitude = locationReq.longitude,
            image = Binary(BsonBinarySubType.BINARY, locationReq.image.bytes),
            description = locationReq.description,
            fact = locationReq.fact
        )
    }
}

data class Event(
    val id: String,
    val eventName: String,
    val time: String,
    val address: String,
    val image: Binary,
) {
    constructor(eventReq: EventReq) : this(
        id = MoraxUtils.newUUID(),
        eventReq.eventName,
        eventReq.time,
        eventReq.address,
        Binary(BsonBinarySubType.BINARY, eventReq.image.bytes),
    )
}

data class Quiz(
    val id: String,
    val question: String,
    val locationId: String,
    val correctAnswer: String,
    val point: Int,
    val image: Binary,
    val description: String?
) {
    constructor(quizReq: QuizReq) : this(
        id = MoraxUtils.newUUID(),
        question = quizReq.question,
        locationId = quizReq.locationId,
        correctAnswer = quizReq.correctAnswer,
        point = quizReq.point,
        Binary(BsonBinarySubType.BINARY, quizReq.image.bytes),
        description = quizReq.description
    )

    fun update(quizReq: QuizReq, quizId: String): Quiz {
        return Quiz(
            id = quizId,
            question = quizReq.question,
            locationId = quizReq.locationId,
            correctAnswer = quizReq.correctAnswer,
            point = quizReq.point,
            Binary(BsonBinarySubType.BINARY, quizReq.image.bytes),
            description = quizReq.description
        )
    }
}

data class TrueQuizHistory(
    val id: String,
    val quizId: String,
    val userId: String,
) {
    constructor(quizId: String, userId: String) : this(
        MoraxUtils.newUUID(),
        quizId,
        userId
    )
}

data class Answer(
    val id: String,
    val quizId: String,
    val answer: String
) {
    constructor(answer: AnswerReq) : this(
        id = MoraxUtils.newUUID(),
        answer.quizId,
        answer.answer
    )
}

data class MysteryItem(
    val id: String,
    val name: String,
    val locationId: String,
    val point: Int,
    val foundImage: Binary,
    val unfoundedImage: Binary,
    val description: String?,
) {
    constructor(mysteryItemReq: MysteryItemReq) : this(
        MoraxUtils.newUUID(),
        mysteryItemReq.name,
        mysteryItemReq.locationId,
        mysteryItemReq.point,
        Binary(BsonBinarySubType.BINARY, mysteryItemReq.foundImage.bytes),
        Binary(BsonBinarySubType.BINARY, mysteryItemReq.unfoundedImage.bytes),
        mysteryItemReq.description
    )

    fun update(mysteryItemReq: MysteryItemReq): MysteryItem {
        return MysteryItem(
            this.id,
            mysteryItemReq.name,
            mysteryItemReq.locationId,
            mysteryItemReq.point,
            Binary(BsonBinarySubType.BINARY, mysteryItemReq.foundImage.bytes),
            Binary(BsonBinarySubType.BINARY, mysteryItemReq.unfoundedImage.bytes),
            mysteryItemReq.description
        )
    }
}

data class Point(
    val id: String,
    val userId: String,
    val point: Int
) {
    constructor(userId: String, point: Int) : this(
        id = MoraxUtils.newUUID(),
        userId, point
    )
}

data class UserHistoryMysteryItem(
    val id: String,
    val userId: String,
    val itemId: String,
    val foundAt: LocalDate
) {
    constructor(userId: String, itemId: String) : this(
        id = MoraxUtils.newUUID(),
        userId,
        itemId,
        LocalDate.now()
    )
}

enum class OriginalFactType {
    LOCATION, QUIZ, MYSTERY_ITEM
}


enum class Role(emptySet: MutableSet<Permission>) {
    USER(Collections.emptySet()),
    MANAGER(
        setOf(
            Permission.MANAGER_READ,
            Permission.MANAGER_UPDATE,
            Permission.MANAGER_DELETE,
            Permission.MANAGER_CREATE
        ).toMutableSet()
    );

    private val permissions: Set<Permission> = setOf()
    val authorities: List<SimpleGrantedAuthority>
        get() {
            val authorities = permissions
                .stream()
                .map { permission -> SimpleGrantedAuthority(permission.permission) }
                .collect(Collectors.toList())
            authorities.add(SimpleGrantedAuthority("ROLE_$name"))
            return authorities

        }
}

enum class Permission(val permission: String) {
    MANAGER_READ("management:read"),
    MANAGER_UPDATE("management:update"),
    MANAGER_CREATE("management:create"),
    MANAGER_DELETE("management:delete");
}

data class Token(
    val id: String,
    val token: String,
    var revoked: Boolean = false,
    var expired: Boolean = false,
    val userId: String
)

