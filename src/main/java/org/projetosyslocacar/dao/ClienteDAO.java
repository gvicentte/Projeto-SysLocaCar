package org.projetosyslocacar.dao;

import org.projetosyslocacar.model.Cliente;

public class ClienteDAO extends GenericDAO<Cliente,Long>{

    public ClienteDAO(Class<Cliente> entityClass) {
        super(entityClass);
    }
}
