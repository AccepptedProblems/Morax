package com.example.morax.service

import com.example.morax.model.*
import com.example.morax.repo.PointRepoImpl
import com.example.morax.repo.QuizRepoImpl
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class QuizServiceImpl(
    private val quizRepo: QuizRepoImpl,
    private val pointRepo: PointRepoImpl
    ):QuizService {
    override fun addQuiz(quizReq: QuizReq): Mono<QuizResp> {
        val newQuiz = Quiz(quizReq)
        return Mono.just(QuizResp(quizRepo.addQuiz(newQuiz), listOf()))
    }

    override fun updateQuiz(quizReq: QuizReq): Mono<QuizResp> {
        val newQuiz = Quiz(quizReq)
        return Mono.just(QuizResp(quizRepo.addQuiz(newQuiz), listOf()))
    }

    override fun getQuizById(quizId: String): QuizResp {
        val quiz = quizRepo.quizById(quizId)
        return QuizResp(quiz, listOf())
    }

    override fun getQuizzes(): Mono<List<QuizResp>> {
        val quizzes = quizRepo.getQuizzes()
        val quizzesResp = getQuizzesResp(quizzes)
        return Mono.just(quizzesResp)
    }

    override fun getQuizzesByLocationId(locationId: String): Mono<List<QuizResp>> {
        val quizzes = quizRepo.getQuizzesByLocationId(locationId)
        val quizzesResp = getQuizzesResp(quizzes)
        return Mono.just(quizzesResp)
    }

    private fun getQuizzesResp(quizzes: List<Quiz>): List<QuizResp> {
        return quizzes.map { quiz ->
            val quizId = quiz.id
            val answers = getQuizAnswer(quizId)
            return@map QuizResp(quiz, answers)
        }.take(10)
    }

    override fun addAnswer(answers: List<AnswerReq>): Mono<List<AnswerResp>> {
        val newAnswers = answers.map { answer -> Answer(answer) }
        val answersResp = quizRepo.addAnswers(newAnswers).map { answer -> AnswerResp(answer) }
        return Mono.just(answersResp)
    }

    override fun updateAnswer(answers: List<AnswerReq>, quizId: String): Mono<List<AnswerResp>> {
        val updateAnswer = answers.map { answer -> Answer(answer) }
        val answersResp = quizRepo.updateAnswer(updateAnswer, quizId).map { answer -> AnswerResp(answer) }
        return Mono.just(answersResp)
    }

    override fun getQuizAnswer(quizId: String): List<AnswerResp> {
        return quizRepo.getQuizAnswer(quizId).map { answer -> AnswerResp(answer) }
    }

    override fun answerQuiz(quizId: String, answerId: String): AnswerQuizResp {
        val userId = User.currentUser.id
        val quiz = getQuizById(quizId)
        val answer = quizRepo.answerById(answerId)
        if(quiz.correctAnswer == answer.answer) {
            val point = Point(userId, quiz.point)
            pointRepo.addPoint(point)
            return AnswerQuizResp(quiz, true)
        } else {
            return AnswerQuizResp(quiz, false)
        }
    }
}