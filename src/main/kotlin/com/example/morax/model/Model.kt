package com.example.morax.model

import com.example.morax.util.MoraxUtils
import org.bson.BsonBinarySubType
import org.bson.types.Binary
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.*
import java.util.stream.Collectors

interface FoocationIdentity{
    var id: String
    val username: String
    val email: String
    val displayName: String
    val password: String
    val role: Role
}

@Document
data class User(
    @Id override var id: String = "",
    @Indexed(unique = true) override val username: String = "",
    @Indexed(unique = true) override val email: String = "",
    override val displayName: String = "",
    override val password: String = "",
    override val role: Role = Role.USER,
    val rankingPoint: Int = 0,
    val balance: Int = 0
): FoocationIdentity {
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
                role = Role.USER
            )
        }
    }
}

data class Staff(
    override var id: String,
    override val username: String,
    override val email: String,
    override val displayName: String,
    override val password: String,
    override val role: Role = Role.MANAGER,
    val phoneNumber: String
): FoocationIdentity

data class Artifact(
    val id: String,
    val name: String,
    val time: String,
    val locationId: String,
    val image: Binary,
    val description: String
){
    constructor(artifactReq: ArtifactReq): this(
        MoraxUtils.newUUID(),
        artifactReq.name,
        artifactReq.time,
        artifactReq.locationId,
        Binary(BsonBinarySubType.BINARY, artifactReq.image.bytes),
        artifactReq.description
    )

    fun update(artifactReq: ArtifactReq): Artifact {
        return this.copy(
            id = this.id,
            name = artifactReq.name,
            time = artifactReq.time,
            locationId = artifactReq.locationId,
            image = Binary(BsonBinarySubType.BINARY, artifactReq.image.bytes),
            description = artifactReq.description
        )
    }
}

data class Fact(
    val id: String,
    val content: String,
    val quizId: String?,
    val artifactId: String?
)

data class Location(
    val id: String,
    val name: String,
    val nameInMap: String,
    val latitude: Double,
    val longitude: Double,
    val image: Binary,
    val description: String
) {
    constructor(locationReq: LocationReq): this(
        MoraxUtils.newUUID(),
        locationReq.name,
        locationReq.nameInMap,
        locationReq.latitude,
        locationReq.longitude,
        Binary(BsonBinarySubType.BINARY, locationReq.image.bytes),
        locationReq.description
    )

    fun update(locationReq: LocationReq): Location {
        return this.copy(
            id = this.id,
            name = locationReq.name,
            nameInMap = locationReq.nameInMap,
            latitude = locationReq.latitude,
            longitude = locationReq.longitude,
            image = Binary(BsonBinarySubType.BINARY, locationReq.image.bytes),
            description = locationReq.description
        )
    }
}

data class Quiz(
    val id: String,
    val question: String,
    val correctAnswer: String,
    val point: Int
)

data class Answer(
    val id: String,
    val quizId: String,
    val content: String
)


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

