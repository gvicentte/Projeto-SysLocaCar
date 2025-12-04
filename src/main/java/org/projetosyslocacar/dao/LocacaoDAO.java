package org.projetosyslocacar.dao;

import org.projetosyslocacar.model.Locacao;

public class LocacaoDAO extends GenericDAO<Locacao,Long>{

    public LocacaoDAO(Class<Locacao> entityClass) {
        super(entityClass);
    }
}
