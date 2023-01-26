package com.coding.task.rewards.dao;

import com.coding.task.rewards.model.Customer;
import com.coding.task.rewards.model.Transaction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class TransactionDAOTest {

    @InjectMocks
    private TransactionDAO transactionDAO;

    @Mock
    private EntityManager entityManager;

    private static SessionFactory sessionFactory;
    private static Session session;

    @BeforeAll
    public static void before() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Transaction.class)
                .addAnnotatedClass(Customer.class);
        configuration.setProperty("hibernate.dialect",
                "org.hibernate.dialect.H2Dialect");
        configuration.setProperty("hibernate.connection.driver_class",
                "org.h2.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:mem:testdb");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create");
        configuration.setProperty("hibernate.hbm2ddl.import_files", "/test-data.sql");

        sessionFactory = configuration.buildSessionFactory();
        session = sessionFactory.openSession();
    }

    @DisplayName("Test get transaction by customer id")
    @Test
    public void getTransactionsByCustomerId() throws Exception {
        when(entityManager.unwrap(Session.class)).thenReturn(session);
        List<Transaction> transactions = transactionDAO.getTransactionsByCustomerId(2001);
        assertEquals(2, transactions.size());
        assertEquals(120, transactions.stream()
                .filter(transaction -> transaction.getTransactionId() == 7001).findFirst()
                .get().getAmount());
    }

}