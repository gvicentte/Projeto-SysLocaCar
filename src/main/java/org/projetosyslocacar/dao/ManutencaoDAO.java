package org.projetosyslocacar.dao;

import org.projetosyslocacar.model.Manutencao;

public class ManutencaoDAO extends GenericDAO<Manutencao,Long>{

    public ManutencaoDAO(Class<Manutencao> entityClass) {
        super(entityClass);
    }
}
