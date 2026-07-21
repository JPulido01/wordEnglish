package com.example.wordenglish.domain.usecase

import com.example.wordenglish.domain.model.Word
import com.example.wordenglish.domain.repository.WordQueueRepository
import javax.inject.Inject

/**
 * Devuelve la palabra actual de la cola enriquecida con detalles de Wordnik
 * (IPA, ejemplos, sinónimos, antónimos) bajo demanda si aún no los tiene.
 *
 * A diferencia de [GetCurrentWordUseCase] (lectura pura para widget/worker),
 * este use case puede disparar una llamada de red, por lo que solo se usa al
 * abrir la pantalla de detalle. Cubre el hueco de cold-start en el que la
 * primera palabra sembrada todavía no ha sido pre-cacheada por el worker.
 */
class GetCurrentWordDetailUseCase @Inject constructor(
    private val repository: WordQueueRepository
) {
    suspend operator fun invoke(): Word? = repository.getCurrentWordEnriched()
}
