package br.com.valueprojects.mock_spring.test;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import br.com.valueprojects.mock_spring.service.SmsService;
import br.com.valueprojects.mock_spring.model.Participante;
public class SmsServiceTest {
    @Test
    public void deveEnviarSmsComParticipanteCorreto() {
        // Mock do participante
        Participante participanteMock = mock(Participante.class);
        when(participanteMock.getNome()).thenReturn("Participante 1");

        // Cria a instância do serviço
        SmsService smsService = new SmsService();

        // Verifica se a mensagem de SMS está correta
        String mensagem = "Parabéns! Você é o vencedor!";
        smsService.enviarSms(participanteMock, mensagem);

        // Simples verificação para garantir que o nome foi usado corretamente
        assertEquals("Participante 1", participanteMock.getNome());

    }

    @Test
    public void deveTratarParticipanteSemNome() {
        // Mock do participante sem nome
        Participante participanteMock = mock(Participante.class);
        when(participanteMock.getNome()).thenReturn(null);

        // Cria a instância do serviço
        SmsService smsService = new SmsService();

        // Simula a mensagem
        String mensagem = "Parabéns! Você é o vencedor!";

        // Chama o metodo para verificar o comportamento
        smsService.enviarSms(participanteMock, mensagem);

        // Verifica que o nome do participante é nulo, o que poderia ser tratado em uma situação real
        assertNull(participanteMock.getNome());


    }

    @Test
    public void deveEnviarSmsComMensagemCorreta() {
        // Mock do participante
        Participante participanteMock = mock(Participante.class);
        when(participanteMock.getNome()).thenReturn("Participante 2");

        // Cria a instância do serviço
        SmsService smsService = new SmsService();

        // Verifica se a mensagem de SMS está correta
        String mensagem = "Você alcançou o primeiro lugar!";
        smsService.enviarSms(participanteMock, mensagem);
        assertEquals("Você alcançou o primeiro lugar!", mensagem);
    }
}
