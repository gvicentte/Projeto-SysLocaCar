module org.projetosyslocacar {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.naming;

    // Adicione esses dois requires, que são comuns em projetos JPA/Hibernate
    requires java.sql;
    requires java.base;
    //requires org.projetosyslocacar;
    //requires org.projetosyslocacar;

    // ----------------------------------------------------
    // EXPORTS: Pacotes com a API pública (Classes/Interfaces)
    // ----------------------------------------------------
    exports org.projetosyslocacar.utils;
    exports org.projetosyslocacar.dao;
    exports org.projetosyslocacar.principal;
    exports org.projetosyslocacar.controller;

    // 🚨 NOVO: Exporte o pacote Model
    exports org.projetosyslocacar.model;

    // ----------------------------------------------------
    // OPENS: Pacotes que precisam ser acessados via Reflexão
    // ----------------------------------------------------

    // Abrir o principal e controller para o JavaFX:
    opens org.projetosyslocacar.principal to javafx.fxml;
    opens org.projetosyslocacar.controller to javafx.fxml;

    // 🚨 CRUCIAL: Abrir o pacote Model para o JavaFX e para o Hibernate
    opens org.projetosyslocacar.model to javafx.fxml, org.hibernate.orm.core;

    // Se você tiver uma pasta 'view' na raiz dos recursos e não em um pacote Java,
    // a inicialização do FXML já deve funcionar se o getResource for correto.
}