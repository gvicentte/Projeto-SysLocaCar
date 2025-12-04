package org.projetosyslocacar.dao;

import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.projetosyslocacar.model.Modelo;
import org.projetosyslocacar.model.Veiculo;
import jakarta.persistence.EntityManager.*;
import org.projetosyslocacar.utils.HibernateUtil;

import java.util.List;

public class ModeloDAO extends GenericDAO<Modelo, Long>{

    public ModeloDAO(Class<Modelo> entityClass) {
        super(entityClass);
    }

    @Override
    public List<Modelo> buscarTodos() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            // HQL com JOIN FETCH para Marca e Categoria
            return session.createQuery(
                            "SELECT m FROM Modelo m JOIN FETCH m.marca JOIN FETCH m.categoria",
                            Modelo.class)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar Modelos com Marca e Categoria: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }
}
