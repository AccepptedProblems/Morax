package com.example.morax.repo

import com.example.morax.model.Artifact
import com.example.morax.model.ArtifactReq
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionTemplate
import org.springframework.web.server.ResponseStatusException

@Component
class ArtifactRepoImpl(
    val mongoTemplate: MongoTemplate,
    val transactionTemplate: TransactionTemplate,
    @Value("\${data.mongodb.table.artifacts}") val artifactCol: String
) : ArtifactRepo {
    override fun addArtifact(artifact: Artifact): Artifact {
        return mongoTemplate.save(artifact, artifactCol)
    }

    override fun updateArtifact(artifactReq: ArtifactReq, artifactId: String, locationId: String): Artifact {
        return transactionTemplate.execute { _ ->
            val artifact = artifactById(artifactId)
            val updatedArtifact = artifact.update(artifactReq, locationId)
            return@execute mongoTemplate.save(updatedArtifact, artifactCol)
        } ?: throw ResponseStatusException(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Cannot update artifact now. Please try again"
        )
    }

    override fun artifactById(id: String): Artifact {
        return mongoTemplate.findById(id, Artifact::class.java, artifactCol) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Cannot find any artifact with id $id"
        )
    }

    override fun listArtifact(): List<Artifact> {
        return mongoTemplate.findAll(Artifact::class.java, artifactCol)
    }

    override fun listArtifact(searchStr: String?): List<Artifact> {
        var artifacts = listArtifact()
        if (searchStr != null) artifacts = artifacts.filter { artifact: Artifact -> artifact.name.contains(searchStr) }
        return artifacts
    }

    override fun artifactsByLocationId(locationId: String, searchStr: String?): List<Artifact> {
        return listArtifact(searchStr).filter { artifact: Artifact -> artifact.locationId == locationId }
    }

}