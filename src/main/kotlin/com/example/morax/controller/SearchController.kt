package com.example.morax.controller

import com.example.morax.model.SearchResp
import com.example.morax.service.SearchService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono


@RestController
@RequestMapping("api/v1/search")
@Tag(name = "Search")
class SearchController(
    val searchService: SearchService
) {
    @Operation(
        summary = "Search data",
        description = "Search data for location and artifact",
    )
    @GetMapping
    fun searchData(@RequestParam searchText: String): Mono<SearchResp> {
        return Mono.just( searchService.searchData(searchText))
    }
}