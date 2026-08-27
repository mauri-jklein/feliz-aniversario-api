package com.felizaniversarioapi.controller;

import com.felizaniversarioapi.entity.Aluno;
import com.felizaniversarioapi.service.AlunoService;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/aluno")
@CrossOrigin(origins = "*")
public class AlunoController {
    @Autowired
    private AlunoService alunoService;

    @GetMapping
    public List<Aluno> listarAlunos() {
        return alunoService.listarTodos();
    }

    @PostMapping
    public Aluno criarAluno(@RequestBody Aluno aluno) {
        return alunoService.save(aluno);
    }

    @PutMapping("/{id}")
    public Aluno atualizarAluno(@PathVariable Long id, @RequestBody Aluno alunoAtualizado) {
        Aluno alunoExistente = alunoService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado"));
        return alunoService.atualizar(alunoExistente, alunoAtualizado);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerAluno(@PathVariable Long id) {
        Aluno alunoExistente = alunoService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado"));
        alunoService.remover(alunoExistente);
    }

    @GetMapping("/aniversariantes")
    public List<Aluno> buscarAniversariantesDoDia() {
        LocalDate dataAtual = LocalDate.now();
        return alunoService.buscarAniversariantesDoDia(dataAtual);
    }

}
