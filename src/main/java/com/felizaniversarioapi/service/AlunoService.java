package com.felizaniversarioapi.service;

import com.felizaniversarioapi.entity.Aluno;
import com.felizaniversarioapi.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {

   @Autowired
   private AlunoRepository alunoRepository;

   public Aluno save(Aluno aluno) {
       return alunoRepository.save(aluno);
   }

   public List<Aluno> listarTodos() {
       return alunoRepository.findAll();
   }

   public Optional<Aluno> buscarPorId(Long id) {
       return alunoRepository.findById(id);
   }

   public Aluno atualizar(Aluno alunoExistente, Aluno dadosAtualizados) {
       alunoExistente.setMatricula(dadosAtualizados.getMatricula());
       alunoExistente.setNome(dadosAtualizados.getNome());
       alunoExistente.setEmail(dadosAtualizados.getEmail());
       alunoExistente.setTurma(dadosAtualizados.getTurma());
       alunoExistente.setDataNascimento(dadosAtualizados.getDataNascimento());
       return alunoRepository.save(alunoExistente);
   }

   public void remover(Aluno aluno) {
       alunoRepository.delete(aluno);
   }

   public List<Aluno> buscarAniversariantesDoDia(LocalDate dataAtual) {
       return alunoRepository.buscarAniversariantesDoDia(
               dataAtual.getMonthValue(),
               dataAtual.getDayOfMonth()
       );
   }

}
