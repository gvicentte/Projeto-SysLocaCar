package org.projetosyslocacar.dao;

import org.hibernate.Session;
import org.projetosyslocacar.model.Ocorrencia;
import org.projetosyslocacar.utils.HibernateUtil;

import java.util.List;

public class OcorrenciaDAO extends GenericDAO<Ocorrencia, Long>{

    public OcorrenciaDAO(Class<Ocorrencia> entityClass) {
        super(entityClass);
    }

    public List<Ocorrencia> buscarTodosComLocacao() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            // Usa JOIN FETCH no campo 'locacao'
            return session.createQuery(
                            "SELECT o FROM Ocorrencia o JOIN FETCH o.locacao", Ocorrencia.class)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar Ocorrências com Locações: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }
}
