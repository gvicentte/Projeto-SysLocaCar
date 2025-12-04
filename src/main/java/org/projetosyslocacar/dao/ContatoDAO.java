package org.projetosyslocacar.dao;

import org.projetosyslocacar.model.Contato;

public class ContatoDAO extends GenericDAO<Contato, Long>{
    public ContatoDAO(Class<Contato> entityClass) {
        super(entityClass);
    }
}
