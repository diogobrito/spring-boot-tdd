package com.example.demo.servico;

import com.example.demo.modelo.Pessoa;
import com.example.demo.modelo.Telefone;
import com.example.demo.servico.exception.TelefoneNaoEncontradoException;
import com.example.demo.servico.exception.UnicidadeCpfExeception;
import com.example.demo.servico.exception.UnicidadeTelefoneExcepiton;

public interface PessoaService {

    Pessoa salvar(Pessoa pessoa) throws UnicidadeCpfExeception, UnicidadeTelefoneExcepiton;

    Pessoa buscarPorTelefone(Telefone telefone) throws TelefoneNaoEncontradoException;
}
