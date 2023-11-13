package com.example.morax.repo

import com.example.morax.model.Artifact
import com.example.morax.model.ArtifactReq

interface ArtifactRepo {
    fun addArtifact(artifact: Artifact): Artifact
    fun updateArtifact(artifactReq: ArtifactReq, id:String): Artifact
    fun artifactById(id: String): Artifact
    fun listArtifact(): List<Artifact>
    fun listArtifact(searchStr: String?): List<Artifact>
}