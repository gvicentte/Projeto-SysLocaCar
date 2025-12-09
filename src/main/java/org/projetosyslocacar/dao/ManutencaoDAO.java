package org.projetosyslocacar.dao;

import org.hibernate.Session;
import org.projetosyslocacar.model.Manutencao;
import org.projetosyslocacar.utils.HibernateUtil;

import java.util.List;

public class ManutencaoDAO extends GenericDAO<Manutencao,Long>{

    public ManutencaoDAO(Class<Manutencao> entityClass) {
        super(entityClass);
    }

    public List<Manutencao> buscarTodosComVeiculo() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            // JOIN FETCH garante que o objeto 'veiculo' seja carregado
            // na mesma consulta SQL, enquanto a sessão está aberta.
            return session.createQuery(
                            "SELECT m FROM Manutencao m JOIN FETCH m.veiculo", Manutencao.class)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar Manutenções com Veículos: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }
}
