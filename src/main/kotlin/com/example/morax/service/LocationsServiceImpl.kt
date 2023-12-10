package com.example.morax.service

import com.example.morax.model.Location
import com.example.morax.model.LocationReq
import com.example.morax.model.LocationResp
import com.example.morax.model.User
import com.example.morax.repo.LocationsRepoImpl
import com.example.morax.repo.QuizRepo
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class LocationsServiceImpl(
    val locationsRepo: LocationsRepoImpl,
    private val quizRepo: QuizRepo): LocationsService {
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
        val locationsResp = getLocationRespList(locationsRepo.getLocations())
        return Mono.just(locationsResp)
    }

    override fun locationWithId(id: String): Mono<LocationResp> {
        val location = locationsRepo.locationById(id)
        val locationResp = LocationResp(location)
        return Mono.just(locationResp)
    }

    override fun locationByName(name: String?): Mono<List<LocationResp>> {
        val locationsResp = getLocationRespList(locationsRepo.getLocations(name))
        return Mono.just(locationsResp)
    }

    private fun getLocationRespList(locations: List<Location>): List<LocationResp> {
        val userId = User.currentUser.id
        val quizzes = quizRepo.getQuizzes()
        val trueQuizzes = quizRepo.getTrueQuizByUser(userId)
        return locations.map { location ->
            val quizIdsByLocation = quizzes.filter { it.locationId == location.id }.map { it.id }
            val quizNum = quizIdsByLocation.size
            val trueQuizNum = trueQuizzes.filter { quizIdsByLocation.contains(it.quizId) }.size
            LocationResp(location, quizNum, trueQuizNum)
        }
    }
}