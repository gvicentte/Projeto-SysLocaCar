package org.projetosyslocacar.dao;

import org.projetosyslocacar.model.Funcionario;

public class FuncionarioDAO extends GenericDAO<Funcionario, Long>{
    public FuncionarioDAO(Class<Funcionario> entityClass) {
        super(entityClass);
    }
}
