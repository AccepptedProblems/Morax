package com.example.morax.repo

import com.example.morax.model.Answer
import com.example.morax.model.Quiz
import com.example.morax.model.TrueQuizHistory

interface QuizRepo {
    fun addQuiz(quiz: Quiz): Quiz
    fun updateQuiz(quiz: Quiz): Quiz
    fun getQuizzes(): List<Quiz>
    fun quizById(quizId: String): Quiz
    fun getQuizzesByLocationId(locationId: String): List<Quiz>
    fun deleteQuiz(id: String): Quiz
    fun addAnswers(answers: List<Answer>): List<Answer>
    fun updateAnswer(answers: List<Answer>, quizId: String): List<Answer>
    fun getAllAnswer(): List<Answer>
    fun getQuizAnswer(quizId: String): List<Answer>
    fun answerById(answerId: String): Answer
    fun saveTrueQuiz(trueQuiz: TrueQuizHistory): TrueQuizHistory
    fun getTrueQuiz(quizId: String): List<TrueQuizHistory>
    fun getTrueQuiz(quizId: String, userId: String): TrueQuizHistory?
    fun getUserNumberTrueQuiz(userId: String): Int
}