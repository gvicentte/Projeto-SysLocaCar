package org.projetosyslocacar.utils;

import lombok.Getter;
import org.projetosyslocacar.model.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    @Getter
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // 1. Cria a configuração
            Configuration configuration = new Configuration();

            // 2. Adiciona TODAS as classes de Entidade mapeadas
            configuration.addAnnotatedClass(Marca.class);
            configuration.addAnnotatedClass(Modelo.class);
            configuration.addAnnotatedClass(Veiculo.class);
            configuration.addAnnotatedClass(Manutencao.class);
            configuration.addAnnotatedClass(Categoria.class);
            configuration.addAnnotatedClass(Locacao.class);
            configuration.addAnnotatedClass(Ocorrencia.class);
            configuration.addAnnotatedClass(ContratoLocacao.class);
            configuration.addAnnotatedClass(Cliente.class);
            configuration.addAnnotatedClass(Contato.class);
            configuration.addAnnotatedClass(Usuario.class);
            configuration.addAnnotatedClass(Endereco.class);
            configuration.addAnnotatedClass(Pagamento.class);
            configuration.addAnnotatedClass(Funcionario.class);

            // 🚨 REMOVA ESTA LINHA: Ela está forçando a busca pelo hibernate.cfg.xml
            // configuration.configure();

            // O Hibernate já carregou as propriedades do hibernate.properties
            // e tem as classes mapeadas. Agora, ele pode construir a SessionFactory.

            return configuration.buildSessionFactory();

        } catch (Throwable ex) {
            System.err.println("Falha na criação inicial da SessionFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static void shutdown() {
        // Fecha caches e pools de conexão
        if (getSessionFactory() != null) {
            getSessionFactory().close();
        }
    }
}