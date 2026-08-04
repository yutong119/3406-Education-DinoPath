package com.example.dinopath.data.repository

import com.example.dinopath.domain.model.QuizQuestion
import com.example.dinopath.domain.repository.QuizRepository
import javax.inject.Inject

class StaticQuizRepository @Inject constructor() : QuizRepository {

    override fun getJurassicQuestions(): List<QuizQuestion> {
        return listOf(
            QuizQuestion(
                id = 1,
                question =
                    "Which dinosaur lived during the Jurassic Period?",
                options = listOf(
                    "Tyrannosaurus rex",
                    "Brachiosaurus",
                    "Triceratops",
                    "Velociraptor",
                ),
                correctAnswer = "Brachiosaurus",
                explanation =
                    "Brachiosaurus lived during the Late Jurassic. " +
                            "The other three appeared during the Cretaceous.",
            ),
            QuizQuestion(
                id = 2,
                question =
                    "What was the general Jurassic climate like?",
                options = listOf(
                    "Mostly warm and humid",
                    "Permanently frozen",
                    "Completely dry worldwide",
                    "Identical to today's climate",
                ),
                correctAnswer = "Mostly warm and humid",
                explanation =
                    "The Jurassic climate was generally warm and humid, " +
                            "supporting extensive forests and large herbivores.",
            ),
            QuizQuestion(
                id = 3,
                question =
                    "Which animal shows features linking dinosaurs and birds?",
                options = listOf(
                    "Archaeopteryx",
                    "Triceratops",
                    "Diplodocus",
                    "Mosasaurus",
                ),
                correctAnswer = "Archaeopteryx",
                explanation =
                    "Archaeopteryx had feathers and wings as well as " +
                            "several dinosaur-like skeletal features.",
            ),
        )
    }
}