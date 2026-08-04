package com.example.dinopath.domain.scoring

fun calculateAccuracy(
    score: Int,
    totalQuestions: Int,
): Int {
    if (totalQuestions <= 0) {
        return 0
    }

    return score * 100 / totalQuestions
}

fun calculateStars(
    score: Int,
    totalQuestions: Int,
): Int {
    if (totalQuestions <= 0) {
        return 0
    }

    return when {
        score >= totalQuestions -> 3
        score == totalQuestions - 1 -> 2
        else -> 1
    }
}