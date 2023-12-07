package com.example.morax.controller

import com.example.morax.model.*
import com.example.morax.service.QuizServiceImpl
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("api/v1/quizzes")
@Tag(name = "Quiz")
class QuizController(private val quizService: QuizServiceImpl) {
    @Operation(
        summary = "Add new quiz",
        description = "Add new quiz",
    )
    @PostMapping("", MediaType.MULTIPART_FORM_DATA_VALUE)
    fun addQuiz(@ModelAttribute quizReq: QuizReq): Mono<QuizResp> {
        return quizService.addQuiz(quizReq)
    }

    @Operation(
        summary = "Update quiz",
        description = "Update quiz",
    )
    @PutMapping("/{quizId}", MediaType.MULTIPART_FORM_DATA_VALUE)
    fun updateQuiz(@ModelAttribute quizReq: QuizReq, @PathVariable quizId: String): Mono<QuizResp> {
        return Mono.just(quizService.updateQuiz(quizReq, quizId))
    }

    @Operation(
        summary = "Get quizzes",
        description = "Get all quizzes or quizzes in a location with locationId",
    )
    @GetMapping("")
    fun getQuizzesWithLocationId(@RequestParam locationId: String?): Mono<List<QuizResp>> {
        return if(locationId != null) Mono.just(quizService.getQuizzesByLocationId(locationId))
        else quizService.getQuizzes()
    }

    @Operation(
        summary = "Get quiz by id",
        description = "Get quiz by id",
    )
    @GetMapping("/{quizId}", MediaType.MULTIPART_FORM_DATA_VALUE)
    fun getQuizById(@ModelAttribute quizReq: QuizReq, @PathVariable quizId: String): Mono<QuizResp> {
        return Mono.just(quizService.getQuizById(quizId))
    }

    @Operation(
        summary = "Add answers for quiz",
        description = "Add answers for quiz",
    )
    @PostMapping("/{quizId}/answer")
    fun addAnswer(@PathVariable quizId: String, @RequestBody answerReq: List<AnswerReq>): Mono<List<AnswerResp>> {
        return quizService.addAnswer(answerReq)
    }

    @Operation(
        summary = "Update answers for quiz",
        description = "Update answers for quiz",
    )
    @PutMapping("/{quizId}/answer")
    fun updateAnswer(@PathVariable quizId: String, @RequestBody answerReq: List<AnswerReq>): Mono<List<AnswerResp>> {
        return quizService.updateAnswer(answerReq, quizId)
    }

    @Operation(
        summary = "Answers quiz",
        description = "Answer the quiz and get the result",
    )
    @PostMapping("/{quizId}/answer/{answerId}")
    fun answerQuiz(@PathVariable quizId: String, @PathVariable answerId: String): Mono<AnswerQuizResp> {
        return Mono.just(quizService.answerQuiz(quizId, answerId))
    }
}