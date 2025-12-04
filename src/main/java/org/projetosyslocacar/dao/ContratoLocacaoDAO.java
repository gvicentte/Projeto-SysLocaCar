package org.projetosyslocacar.dao;

import org.projetosyslocacar.model.ContratoLocacao;

public class ContratoLocacaoDAO extends GenericDAO<ContratoLocacao, Long>{
    public ContratoLocacaoDAO(Class<ContratoLocacao> entityClass) {
        super(entityClass);
    }
}
