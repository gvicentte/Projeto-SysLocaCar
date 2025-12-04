package org.projetosyslocacar.dao;

import org.projetosyslocacar.model.Ocorrencia;

public class OcorrenciaDAO extends GenericDAO<Ocorrencia, Long>{

    public OcorrenciaDAO(Class<Ocorrencia> entityClass) {
        super(entityClass);
    }
}
