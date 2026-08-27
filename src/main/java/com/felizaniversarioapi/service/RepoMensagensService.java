package com.felizaniversarioapi.service;

import com.felizaniversarioapi.entity.RepoMensagens;
import com.felizaniversarioapi.repository.RepoMensagensRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class RepoMensagensService {

    @Autowired
    private RepoMensagensRepository repoMensagensRepository;

    Random rd = new Random();

    public RepoMensagens buscarMensagem() {
        List<RepoMensagens> lista = repoMensagensRepository.findAll();
        int index = rd.nextInt(lista.size());
        if (lista.size() > 0) {
            return lista.get(index);
        }
        return null;
    }

}
