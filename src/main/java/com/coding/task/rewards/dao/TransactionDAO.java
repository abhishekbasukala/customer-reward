package com.coding.task.rewards.dao;

import com.coding.task.rewards.model.Transaction;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
public class TransactionDAO {

    @Autowired
    private EntityManager entityManager;

    public List<Transaction> getTransactionsByCustomerId(int customerId) throws Exception {
        Session session = entityManager.unwrap(Session.class);
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Transaction> criteriaQuery = criteriaBuilder.createQuery(Transaction.class);
        Root<Transaction> root = criteriaQuery.from(Transaction.class);
        criteriaQuery.select(root).where(root.get("customer").get("customerId").in(customerId));
        Query<Transaction> query = session.createQuery(criteriaQuery);
        List<Transaction> transactions = query.getResultList();
        return transactions;
    }

}
