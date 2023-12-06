package com.example.morax.controller

import com.example.morax.model.*
import com.example.morax.service.ArtifactServiceImpl
import com.example.morax.service.LocationsServiceImpl
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("api/v1/locations")
@Tag(name = "Location")
class LocationController(
    val locationsService: LocationsServiceImpl,
    val artifactService: ArtifactServiceImpl
) {

    @Operation(
        summary = "Add new location",
        description = "Add new location",
    )
    @PostMapping("", MediaType.MULTIPART_FORM_DATA_VALUE)
    fun createLocation(@ModelAttribute locationReq: LocationReq): Mono<LocationResp> {
        return locationsService.createLocation(locationReq)
    }

    @Operation(
        summary = "Update location",
        description = "Update location with id",
    )
    @PutMapping("/{id}", MediaType.MULTIPART_FORM_DATA_VALUE)
    fun updateLocation(
        @ModelAttribute locationReq: LocationReq,
        @PathVariable id: String
    )
        : Mono<LocationResp> {
        return locationsService.updateLocation(locationReq, id)
    }

    @Operation(
        summary = "Search location",
        description = "Search location with optional search string"
    )
    @GetMapping
    fun searchLocation(@RequestParam(required = false) name: String?): Mono<List<LocationResp>> {
        return locationsService.locationByName(name)
    }

    @Operation(
        summary = "get location detail with locationId",
        description = "get location with id"
    )
    @GetMapping("/{id}")
    fun locationDetail(@PathVariable id: String): Mono<LocationResp> {
        return locationsService.locationWithId(id)
    }

    @Operation(
        summary = "Add artifact",
        description = "Add artifact in the location"
    )
    @PostMapping("/{locationId}/artifacts", MediaType.MULTIPART_FORM_DATA_VALUE)
    fun addArtifact(
        @PathVariable("locationId") locationId: String,
        @ModelAttribute artifactReq: ArtifactReq
    ): Mono<ArtifactResp> {
        return artifactService.addArtifact(artifactReq, locationId)
    }

    @Operation(
        summary = "Update artifact",
        description = "Update artifact with the id"
    )
    @PutMapping("/{locationId}/artifacts/{artifactId}", MediaType.MULTIPART_FORM_DATA_VALUE)
    fun updateArtifact(
        @PathVariable("locationId") locationId: String,
        @PathVariable("artifactId") artifactId: String,
        @ModelAttribute artifactReq: ArtifactReq
    ): Mono<ArtifactResp> {
        return artifactService.updateArtifact(artifactReq, artifactId, locationId)
    }

    @Operation(
        summary = "Get artifacts in location",
        description = "Search artifact in a location with optional search text"
    )
    @GetMapping("/{locationId}/artifacts")
    fun searchArtifact(
        @RequestParam(required = false) searchText: String?,
        @PathVariable locationId: String
    ): Mono<List<ArtifactResp>> {
        return Mono.just(artifactService.artifactsByLocationId(locationId, searchText))
    }


}