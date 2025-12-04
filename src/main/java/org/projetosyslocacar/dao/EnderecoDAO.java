package org.projetosyslocacar.dao;

import org.projetosyslocacar.model.Endereco;

public class EnderecoDAO extends GenericDAO<Endereco, Long>{
    public EnderecoDAO(Class<Endereco> entityClass) {
        super(entityClass);
    }
}
