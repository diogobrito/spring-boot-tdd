package com.example.demo.repository.helper;

import com.example.demo.modelo.Pessoa;
import com.example.demo.repository.filtro.PessoaFiltro;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PessoaRepositoryImpl implements PessoaRepositoryQueries {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<Pessoa> filtrar(PessoaFiltro pessoaFiltro) {

        final StringBuilder sb = new StringBuilder();
        final Map<String, Object> params = new HashMap<>();

        sb.append("SELECT bean FROM Pessoa bean join bean.telefones tele WHERE 1=1 ");

        preencherNomeSeNecessario(pessoaFiltro, sb, params);

        preencherCpfSeNecessario(pessoaFiltro, sb, params);

        preencherDddSeNecessario(pessoaFiltro, sb, params);

         preencherTelefoneSeNecessario(pessoaFiltro, sb, params);

        Query query = manager.createQuery(sb.toString(), Pessoa.class);
        preencherParametroDaQuery(params, query);

        return query.getResultList();
    }

    private void preencherTelefoneSeNecessario(PessoaFiltro pessoaFiltro, StringBuilder sb, Map<String, Object> params) {
        if(StringUtils.hasText(pessoaFiltro.getTelefone())) {
            sb.append(" AND tele.numero = :numero ");
            params.put("numero", pessoaFiltro.getTelefone());
        }
    }

    private void preencherDddSeNecessario(PessoaFiltro pessoaFiltro, StringBuilder sb, Map<String, Object> params) {
        if(StringUtils.hasText(pessoaFiltro.getDdd())) {
            sb.append(" AND tele.ddd = :ddd");
            params.put("ddd", pessoaFiltro.getDdd());
        }
    }

    private void preencherCpfSeNecessario(PessoaFiltro pessoaFiltro, StringBuilder sb, Map<String, Object> params) {
        if(StringUtils.hasText(pessoaFiltro.getCpf())) {
            sb.append(" AND bean.cpf LIKE :cpf");
            params.put("cpf", "%" + pessoaFiltro.getCpf() + "%");
        }
    }

    private void preencherNomeSeNecessario(PessoaFiltro pessoaFiltro, StringBuilder sb, Map<String, Object> params) {
        if(StringUtils.hasText(pessoaFiltro.getNome())) {
            sb.append(" AND bean.nome LIKE :nome");
            params.put("nome", "%" + pessoaFiltro.getNome() + "%");
        }
    }

    private void preencherParametroDaQuery(Map<String, Object> params, Query query) {
        for(Map.Entry<String, Object> param : params.entrySet()) {
            query.setParameter(param.getKey(), param.getValue());
        }
    }
}
