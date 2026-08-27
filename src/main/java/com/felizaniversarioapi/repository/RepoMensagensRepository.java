package com.felizaniversarioapi.repository;

import com.felizaniversarioapi.entity.RepoMensagens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoMensagensRepository extends JpaRepository<RepoMensagens, Long> {


}
