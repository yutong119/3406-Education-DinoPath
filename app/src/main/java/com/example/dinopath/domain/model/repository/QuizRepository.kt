package com.example.dinopath.domain.repository

import com.example.dinopath.domain.model.QuizQuestion

interface QuizRepository {

    fun getJurassicQuestions(): List<QuizQuestion>
}