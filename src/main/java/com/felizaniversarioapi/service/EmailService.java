package com.felizaniversarioapi.service;

import com.felizaniversarioapi.entity.Aluno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmailService {

    @Autowired
    private AlunoService alunoService;

    private final JavaMailSender emailSender;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void executarRotina() {

        List<Aluno> aniversariantes = alunoService.buscarAniversariantesDoDia(LocalDate.now());

        aniversariantes.forEach(a -> {
            enviarEmail(
                    a.getEmail(),
                    "Parabéns!!!",
                    "Olá " + a.getNome() + ", parabéns pelo seu aniversário!"
            );
        });
    }

    public void enviarEmail(String destinatario, String assunto, String mensagem) {
//        SimpleMailMessage email = new SimpleMailMessage();
//        email.setTo(destinatario);
//        email.setSubject(assunto);
//        email.setText(mensagem);
//        email.setFrom("maurijklein@gmail.com");
//        emailSender.send(email);

        System.out.println("Email enviado para: " + destinatario + "\nAssunto: " + assunto + "\nMensagem: " + mensagem);
    }
}