package org.projetosyslocacar.dao;

import org.projetosyslocacar.model.Pagamento;

public class PagamentoDAO extends GenericDAO<Pagamento,Long>{

    public PagamentoDAO(Class<Pagamento> entityClass) {
        super(entityClass);
    }
}
