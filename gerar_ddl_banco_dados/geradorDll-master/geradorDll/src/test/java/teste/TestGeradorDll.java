package teste;

import javax.persistence.Persistence;

import org.junit.Test;

public class TestGeradorDll {

    @Test
    public void shouldFindABook() throws Exception {

        Persistence.createEntityManagerFactory("gerarDll");
    }
}