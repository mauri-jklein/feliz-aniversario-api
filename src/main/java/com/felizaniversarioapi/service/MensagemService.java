package com.felizaniversarioapi.service;

import com.felizaniversarioapi.entity.Mensagem;
import com.felizaniversarioapi.repository.MensagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MensagemService {

    @Autowired
    private MensagemRepository mensagemRepository;

    public List<Mensagem> listarTodas() {
        return mensagemRepository.findAll();
    }

    public Optional<Mensagem> buscarPorId(Long id) {
        return mensagemRepository.findById(id);
    }

    public Mensagem save(Mensagem mensagem) {
        return mensagemRepository.save(mensagem);
    }

    public Mensagem atualizar(Mensagem mensagemExistente, Mensagem mensagemAtualizada) {
        mensagemExistente.setTexto(mensagemAtualizada.getTexto());
        mensagemExistente.setDataEnvio(mensagemAtualizada.getDataEnvio());
        mensagemExistente.setAluno(mensagemAtualizada.getAluno());
        return mensagemRepository.save(mensagemExistente);
    }

    public void remover(Mensagem mensagem) {
        mensagemRepository.delete(mensagem);
    }

}
