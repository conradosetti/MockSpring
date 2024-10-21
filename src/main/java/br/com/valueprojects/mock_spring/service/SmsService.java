package br.com.valueprojects.mock_spring.service;

import br.com.valueprojects.mock_spring.model.Participante;
import br.com.valueprojects.mock_spring.model.Sms;

public class SmsService {

    public void enviarSms(Participante participante, String mensagem) {
        Sms sms = new Sms(mensagem);

        System.out.println("Enviando SMS para: " + participante.getNome() + " - Mensagem: " + sms.Mensagem);
    }
}
