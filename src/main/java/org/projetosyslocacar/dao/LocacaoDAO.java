package org.projetosyslocacar.dao;

import org.hibernate.Session;
import org.projetosyslocacar.model.Locacao;
import org.projetosyslocacar.utils.HibernateUtil;

import java.util.List;

public class LocacaoDAO extends GenericDAO<Locacao,Long>{

    public LocacaoDAO(Class<Locacao> entityClass) {
        super(entityClass);
    }
    public List<Locacao> buscarTodosComVeiculo() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            // JOIN FETCH garante que o objeto 'veiculo' seja inicializado
            // antes que a sessão seja fechada.
            return session.createQuery(
                            "SELECT l FROM Locacao l JOIN FETCH l.veiculo", Locacao.class)
                    .list();
        } catch (Exception e) {
            // Tratar ou relançar o erro
            throw new RuntimeException("Erro ao buscar Locações com Veículos: " + e.getMessage(), e);
        } finally {
            session.close(); // Fecha a Session
        }
    }

    public List<Locacao> buscarTodosComRelacionamentos() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            // Usa DOIS JOIN FETCH para carregar ambos os relacionamentos LAZY
            return session.createQuery(
                            "SELECT l FROM Locacao l JOIN FETCH l.veiculo JOIN FETCH l.contratoLocacao", Locacao.class)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar Locações com relacionamentos: " + e.getMessage(), e);
        } finally {
            session.close();
        }
    }
}
