package com.felizaniversarioapi.controller;

import com.felizaniversarioapi.entity.dtos.MensagemListagemResponse;
import com.felizaniversarioapi.entity.Aluno;
import com.felizaniversarioapi.entity.Mensagem;
import com.felizaniversarioapi.service.AlunoService;
import com.felizaniversarioapi.service.MensagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/mensagem")
@CrossOrigin(origins = "*")
public class MensagemController {

    @Autowired
    private MensagemService mensagemService;

    @Autowired
    private AlunoService alunoService;

    @GetMapping
    public List<Mensagem> listarMensagens() {
        return mensagemService.listarTodas();
    }

    @GetMapping("/{id}")
    public Mensagem buscarMensagemPorId(@PathVariable Long id) {
        return mensagemService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mensagem não encontrada"));
    }

    @GetMapping("/listagem")
    public List<MensagemListagemResponse> listarMensagensComDadosDoAluno() {
        return mensagemService.listarTodas().stream()
                .map(mensagem -> {
                    Aluno aluno = mensagem.getAluno();
                    return new MensagemListagemResponse(
                            mensagem.getId(),
                            aluno != null ? aluno.getId() : null,
                            aluno != null ? aluno.getNome() : null,
                            aluno != null ? aluno.getDataNascimento() : null,
                            mensagem.getDataEnvio(),
                            mensagem.getTexto()
                    );
                })
                .toList();
    }

    @PostMapping
    public Mensagem criarMensagem(@RequestBody Mensagem mensagem) {
        validarEVincularAluno(mensagem);
        return mensagemService.save(mensagem);
    }

    @PutMapping("/{id}")
    public Mensagem atualizarMensagem(@PathVariable Long id, @RequestBody Mensagem mensagemAtualizada) {
        Mensagem mensagemExistente = mensagemService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mensagem não encontrada"));
        validarEVincularAluno(mensagemAtualizada);
        return mensagemService.atualizar(mensagemExistente, mensagemAtualizada);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerMensagem(@PathVariable Long id) {
        Mensagem mensagemExistente = mensagemService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mensagem não encontrada"));
        mensagemService.remover(mensagemExistente);
    }

    private void validarEVincularAluno(Mensagem mensagem) {
        if (mensagem.getAluno() == null || mensagem.getAluno().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Aluno da mensagem é obrigatório");
        }

        Aluno aluno = alunoService.buscarPorId(mensagem.getAluno().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encontrado"));
        mensagem.setAluno(aluno);
    }

}
