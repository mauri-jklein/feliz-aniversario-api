package com.felizaniversarioapi.scheduled;

import com.felizaniversarioapi.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class RotinaDiariaScheduler {

    @Autowired
    private EmailService emailService;

    //Rodar uma vez por dia às 12:00
    @Scheduled(fixedRate = 1000000)
//    @Scheduled(cron = "0 0 12 * * *")
    public void executar() {
        System.out.println("Executando rotina diária de envio de emails...");
        emailService.executarRotina();
    }
}
