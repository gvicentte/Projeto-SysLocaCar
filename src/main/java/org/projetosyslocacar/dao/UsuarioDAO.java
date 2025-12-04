package org.projetosyslocacar.dao;

import org.projetosyslocacar.model.Usuario;

public class UsuarioDAO extends GenericDAO<Usuario, Long>{

    public UsuarioDAO(Class<Usuario> entityClass) {
        super(entityClass);
    }
}
