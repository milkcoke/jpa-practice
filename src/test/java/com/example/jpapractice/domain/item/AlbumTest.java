package com.example.jpapractice.domain.item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlbumTest {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-practice");
    private EntityManager em;

    @Test
    void albumCreateTest() {
        var tx = getEntityTransaction();
        tx.begin();

        try {

            Album album1 = new Album();
            album1.changeAlbumInfo("Json Mraz");
            album1.updateItemInfo(10_000, 30_000);

            em.persist(album1);

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

    }

    private EntityTransaction getEntityTransaction() {
        this.em = emf.createEntityManager();
        return em.getTransaction();
    }
}