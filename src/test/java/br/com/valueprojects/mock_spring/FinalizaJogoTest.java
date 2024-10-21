package br.com.valueprojects.mock_spring;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import br.com.valueprojects.mock_spring.model.Participante;
import br.com.valueprojects.mock_spring.model.Resultado;
import br.com.valueprojects.mock_spring.service.SmsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.valueprojects.mock_spring.builder.CriadorDeJogo;
import br.com.valueprojects.mock_spring.model.FinalizaJogo;
import br.com.valueprojects.mock_spring.model.Jogo;
import infra.JogoDao;
import org.mockito.InOrder;
import org.mockito.Mockito;


public class FinalizaJogoTest {


    @Test
    public void deveFinalizarJogosDaSemanaAnterior() {

        Calendar antiga = Calendar.getInstance();
        antiga.set(1999, 1, 20);

        Jogo jogo1 = new CriadorDeJogo().para("Ca�a moedas")
                .naData(antiga).constroi();
        Jogo jogo2 = new CriadorDeJogo().para("Derruba barreiras")
                .naData(antiga).constroi();

        // mock no lugar de dao falso

        List<Jogo> jogosAnteriores = Arrays.asList(jogo1, jogo2);

        JogoDao daoFalso = mock(JogoDao.class);

        when(daoFalso.emAndamento()).thenReturn(jogosAnteriores);

        FinalizaJogo finalizador = new FinalizaJogo(daoFalso);
        finalizador.finaliza();

        assertTrue(jogo1.isFinalizado());
        assertTrue(jogo2.isFinalizado());
        assertEquals(2, finalizador.getTotalFinalizados());
    }

    @Test
    public void naoDeveEnviarSmsSeNenhumJogoForFinalizado() {
        // Mock do JogoDao
        JogoDao daoMock = mock(JogoDao.class);

        // Simula que não há jogos finalizados
        when(daoMock.finalizados()).thenReturn(Arrays.asList());

        // Instancia a classe que será testada com o mock do DAO
        FinalizaJogo finalizador = new FinalizaJogo(daoMock);

        // Verifica que não houve interações com o DAO já que não havia jogos
        verifyNoInteractions(daoMock);
    }

    @Test
    public void deveEnviarSmsParaVencedor() {

        Calendar antiga = Calendar.getInstance();
        antiga.set(1999, 1, 20);

        //Jogo jogo1 = new CriadorDeJogo().para("Ca�a moedas")
                //.naData(antiga).constroi();
        //Jogo jogo2 = new CriadorDeJogo().para("Derruba barreiras")
                //.naData(antiga).constroi();

        // mock no lugar de dao falso

        // Mock dos jogos
        Jogo jogo1 = mock(Jogo.class);
        Jogo jogo2 = mock(Jogo.class);

        // Configura os mocks para retornar as datas corretas
        when(jogo1.getData()).thenReturn(antiga);
        when(jogo2.getData()).thenReturn(antiga);

        List<Jogo> jogosAnteriores = Arrays.asList(jogo1, jogo2);

        // Mock do JogoDao
        JogoDao daoMock = mock(JogoDao.class);

        // Configura o mock para retornar a lista de jogos em andamento
        when(daoMock.emAndamento()).thenReturn(jogosAnteriores);

        // Instancia a classe que será testada com o mock do DAO
        FinalizaJogo finalizador = new FinalizaJogo(daoMock);

        // Chama o metodo que finaliza os jogos
        finalizador.finaliza();

        // Verifica se os jogos foram finalizados
        verify(jogo1).finaliza();
        verify(jogo2).finaliza();
        assertEquals(2, finalizador.getTotalFinalizados());
    }

    @Test
    public void deveVerificarSeMetodoAtualizaFoiInvocado() {
        Calendar antiga = Calendar.getInstance();
        antiga.set(1999, 1, 20);

        // Mock dos jogos
        Jogo jogo1 = mock(Jogo.class);
        Jogo jogo2 = mock(Jogo.class);

        // Configura os mocks para retornar as datas corretas
        when(jogo1.getData()).thenReturn(antiga);
        when(jogo2.getData()).thenReturn(antiga);

        // Configura os mocks para indicar que os jogos estão em andamento
        when(jogo1.isFinalizado()).thenReturn(false);
        when(jogo2.isFinalizado()).thenReturn(false);

        List<Jogo> jogosAnteriores = Arrays.asList(jogo1, jogo2);

        // Mock do JogoDao
        JogoDao daoMock = mock(JogoDao.class);

        // Configura o mock para retornar a lista de jogos em andamento
        when(daoMock.emAndamento()).thenReturn(jogosAnteriores);

        // Instancia a classe que será testada com o mock do DAO
        FinalizaJogo finalizador = new FinalizaJogo(daoMock);

        finalizador.finaliza();

        // Verifica se o método atualiza() foi chamado para jogo1 e jogo2
        verify(daoMock, times(1)).atualiza(jogo1);
        verify(daoMock, times(1)).atualiza(jogo2);

        // Verifica que os jogos foram finalizados
        verify(jogo1).finaliza();
        verify(jogo2).finaliza();

        verify(daoMock, times(1)).atualiza(jogo1);
    }

    @Test
    public void deveSalvarVencedorComMaiorMetrica() {
        // Cria alguns participantes
        Participante p1 = new Participante("Participante 1");
        Participante p2 = new Participante("Participante 2");
        Participante p3 = new Participante("Participante 3");

        // Cria um jogo
        Jogo jogo = new Jogo("Jogo Teste", Calendar.getInstance());

        // Cria alguns resultados com métricas
        Resultado r1 = new Resultado(p1, 75); // Participante 1 com métrica 75
        Resultado r2 = new Resultado(p2, 85); // Participante 2 com métrica 85
        Resultado r3 = new Resultado(p3, 65); // Participante 3 com métrica 65

        // Adiciona os resultados ao jogo
        jogo.anota(r1);
        jogo.anota(r2);
        jogo.anota(r3);

        // Chama o método getVencedor() para verificar o resultado
        Participante vencedor = jogo.getVencedor();

        // Verifica se o vencedor é o participante esperado (Participante 2, com métrica 85)
        assertEquals(p2, vencedor);
    }

}

 

	
	

	
