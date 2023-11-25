package com.example.morax.controller

import com.example.morax.model.AnswerReq
import com.example.morax.model.AnswerResp
import com.example.morax.model.QuizReq
import com.example.morax.model.QuizResp
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
    @PutMapping("", MediaType.MULTIPART_FORM_DATA_VALUE)
    fun updateQuiz(@ModelAttribute quizReq: QuizReq): Mono<QuizResp> {
        return quizService.addQuiz(quizReq)
    }

    @Operation(
        summary = "Get quizzes",
        description = "Get all quizzes or quizzes in a location with locationId",
    )
    @GetMapping("")
    fun getQuizzesWithLocationId(@RequestParam locationId: String?): Mono<List<QuizResp>> {
        return if(locationId != null) quizService.getQuizzesByLocationId(locationId)
        else quizService.getQuizzes()
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

    @PostMapping("/{quizId}/answer/correct")
    fun addCorrectAnswer(@PathVariable quizId: String) {
        return
    }
}