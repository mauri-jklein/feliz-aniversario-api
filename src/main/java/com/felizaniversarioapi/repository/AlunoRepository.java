package com.felizaniversarioapi.repository;

import com.felizaniversarioapi.entity.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    @Query("""
                SELECT a
                FROM Aluno a
                WHERE MONTH(a.dataNascimento) = :month
                  AND DAY(a.dataNascimento) = :day
            """)
    List<Aluno> buscarAniversariantesDoDia(@Param("month") int month, @Param("day") int day);
}
