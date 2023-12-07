package com.example.morax.service

import com.example.morax.model.Location
import com.example.morax.model.LocationReq
import com.example.morax.model.LocationResp
import com.example.morax.repo.LocationsRepoImpl
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class LocationsServiceImpl(
    val locationsRepo: LocationsRepoImpl,
    private val quizService: QuizServiceImpl): LocationsService {
    override fun createLocation(locationReq: LocationReq): Mono<LocationResp> {
        val newLocation = locationsRepo.addLocation(Location(locationReq))
        val locationResp = LocationResp(newLocation)
        return Mono.just(locationResp)
    }

    override fun updateLocation(locationReq: LocationReq, id: String): Mono<LocationResp> {
        val newLocation = locationsRepo.updateLocation(locationReq, id)
        val locationResp = LocationResp(newLocation)
        return Mono.just(locationResp)
    }

    override fun getLocations(): Mono<List<LocationResp>> {
        val locationsResp = locationsRepo.getLocations().map { location -> LocationResp(location) }
        return Mono.just(locationsResp)
    }

    override fun locationWithId(id: String): Mono<LocationResp> {
        val location = locationsRepo.locationById(id)
        val locationResp = LocationResp(location)
        return Mono.just(locationResp)
    }

    override fun locationByName(name: String?): Mono<List<LocationResp>> {
        val locationsResp = locationsRepo.getLocations(name).map { location -> LocationResp(location) }
        return Mono.just(locationsResp)
    }

    private fun getLocationResp(location: Location): LocationResp {
        val quizNum = quizService.getQuizzesByLocationId(location.id)
        return TODO("Provide the return value")
    }
}