package com.example.morax.repo

import com.example.morax.model.Answer
import com.example.morax.model.Quiz
import com.example.morax.model.TrueQuizHistory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.findAll
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException

@Component
class QuizRepoImpl(
    private val mongoTemplate: MongoTemplate,
    @Value("\${data.mongodb.table.quizzes}") val quizCol: String,
    @Value("\${data.mongodb.table.answers}") val answerCol: String,
    @Value("\${data.mongodb.table.true-quiz-history}") val quizHistoryCol: String,
) : QuizRepo {
    override fun addQuiz(quiz: Quiz): Quiz {
        return mongoTemplate.save(quiz, quizCol)
    }

    override fun updateQuiz(quiz: Quiz): Quiz {
        return mongoTemplate.save(quiz, quizCol)
    }

    override fun getQuizzes(): List<Quiz> {
        return mongoTemplate.findAll<Quiz>(quizCol)
    }

    override fun quizById(quizId: String): Quiz {
        return mongoTemplate.findById(quizId, Quiz::class.java, quizCol) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Cannot find any quiz with id $quizId"
        )
    }

    override fun getQuizzesByLocationId(locationId: String): List<Quiz> {
        val query = Query()
        query.addCriteria(Criteria.where("locationId").isEqualTo(locationId))
        return mongoTemplate.find(query, Quiz::class.java, quizCol)
    }

    override fun deleteQuiz(id: String): Quiz {
        val query = Query()
        query.addCriteria(Criteria.where("id").isEqualTo(id))
        return mongoTemplate.findAndRemove(query, Quiz::class.java, quizCol)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Cannot find any quiz with id $id to delete"
            )
    }

    override fun addAnswers(answers: List<Answer>): List<Answer> {
        for (answer in answers) {
            mongoTemplate.save(answer, answerCol)
        }
        return answers
    }

    override fun updateAnswer(answers: List<Answer>, quizId: String): List<Answer> {
        val query = Query()
        query.addCriteria(Criteria.where("quizId").isEqualTo(quizId))
        mongoTemplate.findAndRemove(query, Answer::class.java, answerCol)

        for (answer in answers) {
            mongoTemplate.save(answer, answerCol)
        }
        return answers
    }

    override fun getQuizAnswer(quizId: String): List<Answer> {
        val query = Query()
        query.addCriteria(Criteria.where("quizId").isEqualTo(quizId))
        return mongoTemplate.find(query, Answer::class.java, answerCol)
    }

    override fun answerById(answerId: String): Answer {
        return mongoTemplate.findById(answerId, Answer::class.java, answerCol) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Cannot find any answer with id $answerId"
        )
    }

    override fun saveTrueQuiz(trueQuiz: TrueQuizHistory): TrueQuizHistory {
        return mongoTemplate.save(trueQuiz, quizHistoryCol)
    }

    override fun getTrueQuiz(quizId: String): List<TrueQuizHistory> {
        val query = Query()
        query.addCriteria(Criteria.where("quizId").isEqualTo(quizId))
        return mongoTemplate.find(query, TrueQuizHistory::class.java, quizHistoryCol)
    }

    override fun getTrueQuiz(quizId: String, userId: String): TrueQuizHistory? {
        val query = Query()
        query.addCriteria(Criteria.where("quizId").isEqualTo(quizId))
        query.addCriteria(Criteria.where("userId").isEqualTo(userId))
        val trueQuizzes = mongoTemplate.find(query, TrueQuizHistory::class.java, quizHistoryCol)
        return if (trueQuizzes.size == 0) null
        else trueQuizzes[0]
    }

    override fun getUserNumberTrueQuiz(userId: String): Int {
        val query = Query()
        query.addCriteria(Criteria.where("userId").isEqualTo(userId))
        return mongoTemplate.find(query, TrueQuizHistory::class.java, quizHistoryCol).size
    }
}