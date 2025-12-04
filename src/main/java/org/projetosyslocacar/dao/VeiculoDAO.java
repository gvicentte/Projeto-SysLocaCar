package org.projetosyslocacar.dao;

import org.hibernate.Session;
import org.projetosyslocacar.model.Veiculo;
import org.projetosyslocacar.utils.HibernateUtil;

import java.util.List;

public class VeiculoDAO extends GenericDAO<Veiculo,Long>{
    public VeiculoDAO(Class<Veiculo> entityClass) {
        super(entityClass);
    }
    @Override
    public List<Veiculo> buscarTodos() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            // HQL com Múltiplos JOIN FETCH:
            // 1. Junta Veiculo com Modelo (v.modelo)
            // 2. Junta Modelo com Marca (v.modelo.marca)
            // 3. Junta Modelo com Categoria (v.modelo.categoria)
            // A utilização de apelidos (m para v.modelo) simplifica a consulta.

            return session.createQuery(
                            "SELECT v FROM Veiculo v " +
                                    "JOIN FETCH v.modelo m " +
                                    "JOIN FETCH m.marca " +
                                    "JOIN FETCH m.categoria",
                            Veiculo.class)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar Veículos com relacionamentos: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }
}
