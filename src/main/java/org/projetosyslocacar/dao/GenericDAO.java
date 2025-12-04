package org.projetosyslocacar.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import org.projetosyslocacar.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.projetosyslocacar.utils.HibernateUtil;
import java.io.Serializable;
import java.util.List;

public abstract class GenericDAO<T, ID extends Serializable> {
    private final Class<T> entityClass;
    private EntityManager entityManager; // Você deve injetar/obter este objeto

    public GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
        // O EntityManager deve ser definido ou injetado aqui.
        // Exemplo: this.entityManager = JpaUtil.getEntityManager();
    }

    // --- C (Create) e U (Update) ---
    // O método 'salvar' é geralmente usado para criação e atualização (merge).
    public T salvar(T entity) {
        Session session = HibernateUtil.getSessionFactory().openSession(); // Abre a Session
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.merge(entity); // Usa merge para CUD/Update
            transaction.commit();
            return entity;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Erro ao salvar entidade: " + e.getMessage(), e);
        } finally {
            session.close(); // Fecha a Session sempre
        }
    }

    // --- R (Read) - Buscar por ID ---
    public T buscarPorId(ID id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            // O método get() retorna a entidade ou null se não for encontrada.
            // Não é necessário iniciar uma transação para uma simples leitura.
            return session.find(entityClass, id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar entidade por ID: " + e.getMessage(), e);
        } finally {
            session.close(); // Sempre fechar a Session
        }
    }

    // --- R (Read) - Buscar todos ---
    public List<T> buscarTodos() {
        Session session = HibernateUtil.getSessionFactory().openSession(); // Abre a Session
        try {
            // Usa HQL ou Criteria (mantenha sua lógica se for Criteria)
            return session.createQuery("FROM " + entityClass.getSimpleName(), entityClass)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar entidades: " + e.getMessage(), e);
        } finally {
            session.close(); // Fecha a Session sempre
        }
    }

    // --- D (Delete) ---
    public void excluir(T entity) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            // 1. Reassociação (Crucial para entidades detached)
            // Se a entidade 'entity' veio de outro lugar (ex: tabela JavaFX), ela está 'detached'.
            // Precisamos reassociá-la à sessão (usamos merge ou find/load, mas o delete é direto).
            // Melhor prática: primeiro busca a entidade pelo ID e depois a deleta,
            // ou usa session.remove(session.merge(entity)) se a entidade for detached.

            // Vamos usar o método delete, que funciona se a entidade for managed ou se a classe for correta.
            session.remove(session.contains(entity) ? entity : session.merge(entity));

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Erro ao excluir entidade: " + e.getMessage(), e);
        } finally {
            session.close(); // Sempre fechar a Session
        }
    }

    // Método auxiliar para contar registros (opcional)
    public long contar() {
        return (Long) entityManager.createQuery("select count(e) from " + entityClass.getSimpleName() + " e").getSingleResult();
    }

    // Você pode adicionar um método para definir o EntityManager, se necessário
    // public void setEntityManager(EntityManager entityManager) {
    //     this.entityManager = entityManager;
    // }
}