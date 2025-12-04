package org.projetosyslocacar.dao;

import jakarta.persistence.Query;
import org.hibernate.Session;
import org.projetosyslocacar.model.Marca;
import org.projetosyslocacar.utils.HibernateUtil;

import java.util.List;

public class MarcaDAO extends GenericDAO<Marca, Long>{
    public MarcaDAO(Class<Marca> entityClass) {
        super(entityClass);
    }

    public List<String> GetNomes(Class<Marca> teste){
        Session session = null;
        try {
            // Certifique-se de que seu HibernateUtil está sendo usado aqui
            session = HibernateUtil.getSessionFactory().openSession();

            // HQL com FETCH JOIN para carregar Marca e Categoria na mesma query
            List<String> nomes = session.createQuery(
                    "SELECT nome FROM Marca",
                    String.class
            ).getResultList();

            return nomes;

        } catch (Exception e) {
            e.printStackTrace();
            return null; // ou throw new RuntimeException("Erro ao buscar modelos", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public Marca FindNome(String nome) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();

            // 1. Cria a consulta HQL (Hibernate Query Language)
            // 'm' é um alias para a entidade Marca.
            // 'nomeParam' é o parâmetro que será substituído pelo nome fornecido.
            Query query = session.createQuery(
                    "SELECT m FROM Marca m WHERE m.nome = :nomeParam",
                    Marca.class
            );

            // 2. Define o parâmetro da consulta
            query.setParameter("nomeParam", nome);

            // 3. Executa a consulta e tenta retornar um único resultado
            // getSingleResultOrNull() é o ideal, mas se não estiver disponível,
            // usamos getResultList().stream().findFirst().orElse(null)

            // Tenta pegar o primeiro (ou único) resultado.
            // Se não houver resultado, retorna null.
            return (Marca) query.getResultList().stream().findFirst().orElse(null);

        } catch (Exception e) {
            System.err.println("Erro ao buscar Marca pelo nome: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
