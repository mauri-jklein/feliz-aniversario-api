package com.felizaniversarioapi.entity.dtos;

import java.time.LocalDate;

public record MensagemListagemResponse(
        Long id,
        Long alunoId,
        String nomeAluno,
        LocalDate dataAniversario,
        LocalDate dataEnvio,
        String texto
) {
}
