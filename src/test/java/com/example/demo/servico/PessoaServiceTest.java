package com.example.demo.servico;

import com.example.demo.modelo.Pessoa;
import com.example.demo.modelo.Telefone;
import com.example.demo.repository.PessoaRepository;
import com.example.demo.servico.exception.TelefoneNaoEncontradoException;
import com.example.demo.servico.exception.UnicidadeCpfExeception;
import com.example.demo.servico.exception.UnicidadeTelefoneExcepiton;
import com.example.demo.servico.impl.PessoaServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
public class PessoaServiceTest {

    private static final String NOME = "Diogo Brito";
    private static final String CPF = "00000000000";
    private static final String DDD = "11";
    private static final String NUMERO = "97626-4751";

    @MockBean
    private PessoaRepository pessoaRepository;

    private PessoaService sut;

    private Pessoa pessoa;

    private Telefone telefone = new Telefone();

    @Before
    public void setUp() throws Exception {

        sut = new PessoaServiceImpl(pessoaRepository);

        pessoa = new Pessoa();
        pessoa.setNome(NOME);
        pessoa.setCpf(CPF);


        telefone.setDdd(DDD);
        telefone.setNumero(NUMERO);

        pessoa.setTelefones(Arrays.asList(telefone));

        when(pessoaRepository.findByCpf(CPF)).thenReturn(Optional.empty());
        when(pessoaRepository.findByTelefoneDddAndTelefoneNumero(DDD, NUMERO)).thenReturn(Optional.empty());

    }

    @Test
    public void deve_salvar_pessoa_no_repositorio() throws Exception {
        sut.salvar(pessoa);

        verify(pessoaRepository).save(pessoa);
    }

    @Test(expected = UnicidadeCpfExeception.class)
    public void nao_deve_salvar_pessoa_com_o_mesmo_cpf() throws Exception {
        when(pessoaRepository.findByCpf(CPF)).thenReturn(Optional.of(pessoa));

        sut.salvar(pessoa);
    }

    @Test(expected = UnicidadeTelefoneExcepiton.class)
    public void nao_deve_salvar_a_mesma_pessoa_com_o_mesmo_telefone() throws Exception {
        when(pessoaRepository.findByTelefoneDddAndTelefoneNumero(DDD,NUMERO)).thenReturn(Optional.of(pessoa));

        sut.salvar(pessoa);
    }


    @Test(expected = TelefoneNaoEncontradoException.class)
    public void deve_retornar_excecao_de_nao_encontrado_quando_nao_existir_pessoa_com_ddd_e_numero_de_telefone() throws Exception {

        sut.buscarPorTelefone(telefone);
    }

    @Test
    public void deve_procurar_pessoa_pelo_ddd_e_numero_de_telefone() throws Exception {
        when(pessoaRepository.findByTelefoneDddAndTelefoneNumero(DDD, NUMERO)).thenReturn(Optional.of(pessoa));

        Pessoa pessoaTeste = sut.buscarPorTelefone(telefone);

        verify(pessoaRepository).findByTelefoneDddAndTelefoneNumero(DDD, NUMERO);

        assertThat(pessoaTeste).isNotNull();
        assertThat(pessoaTeste.getNome()).isEqualTo(NOME);
        assertThat(pessoaTeste.getCpf()).isEqualTo(CPF);

    }
}
