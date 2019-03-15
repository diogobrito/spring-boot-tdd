package com.example.demo.servico.impl;

import com.example.demo.modelo.Pessoa;
import com.example.demo.modelo.Telefone;
import com.example.demo.repository.PessoaRepository;
import com.example.demo.servico.PessoaService;
import com.example.demo.servico.exception.TelefoneNaoEncontradoException;
import com.example.demo.servico.exception.UnicidadeCpfExeception;
import com.example.demo.servico.exception.UnicidadeTelefoneExcepiton;

import java.util.Optional;

public class PessoaServeImpl implements PessoaService {
    private final PessoaRepository pessoaRepositoy;

    public PessoaServeImpl(PessoaRepository pessoaRepository) {
        this.pessoaRepositoy = pessoaRepository;
    }

    @Override
    public Pessoa salvar(Pessoa pessoa) throws UnicidadeCpfExeception, UnicidadeTelefoneExcepiton {
        Optional<Pessoa> optional = pessoaRepositoy.findByCpf(pessoa.getCpf());

        if( optional.isPresent() ) {
            throw new UnicidadeCpfExeception();
        }

        final String ddd = pessoa.getTelefones().get(0).getDdd();
        final String numero = pessoa.getTelefones().get(0).getNumero();

        optional = pessoaRepositoy.findByTelefoneDddAndTelefoneNumero(ddd, numero);

        if( optional.isPresent() ) {
            throw new UnicidadeTelefoneExcepiton();
        }

        return pessoaRepositoy.save(pessoa);
    }

    @Override
    public Pessoa buscarPorTelefone(Telefone telefone) throws TelefoneNaoEncontradoException {
        Optional<Pessoa> optional = pessoaRepositoy.findByTelefoneDddAndTelefoneNumero(telefone.getDdd(), telefone.getNumero());
        return optional.orElseThrow(() -> new TelefoneNaoEncontradoException());
    }
}
