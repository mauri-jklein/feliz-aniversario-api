package com.felizaniversarioapi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long matricula;
    private String nome;
    private String email;
    private String turma;
    private LocalDate dataNascimento;


    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL)
    private List<Mensagem> mensagens;
}
