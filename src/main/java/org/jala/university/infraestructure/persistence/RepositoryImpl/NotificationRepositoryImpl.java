package org.jala.university.infraestructure.persistence.RepositoryImpl;

import jakarta.persistence.EntityManager;
import org.jala.university.commons.infrastructure.persistance.CrudRepository;
import org.jala.university.domain.entity.Customer;
import org.jala.university.domain.entity.Notification;
import org.jala.university.domain.repository.NotificationRepository;

import java.util.List;
import java.util.UUID;

public class NotificationRepositoryImpl extends CrudRepository<Notification, UUID> implements NotificationRepository {
    public NotificationRepositoryImpl(EntityManager entityManager) {
        super(Notification.class, entityManager);
    }

    @Override
    public Notification findById(UUID id) {
        String sql = "SELECT * FROM Notification WHERE id = UUID_TO_BIN(:id)";
        return (Notification) entityManager.createNativeQuery(sql, Notification.class)
                .setParameter("id", id.toString())
                .getSingleResult();
    }

    @Override
    public void deleteAllByUser(Customer customer) {
        try {
            entityManager.getTransaction().begin();
            int deletedCount = entityManager.createQuery("DELETE FROM Notification n WHERE n.customer = :customer")
                    .setParameter("customer", customer)
                    .executeUpdate();

            entityManager.getTransaction().commit();

            if (deletedCount > 0) {
                System.out.println("Notifications with customer " + customer.getFullName() + " deleted successfully.");
            } else {
                System.out.println("No notification found with customer: " + customer.getFullName());
            }
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error deleting notification with customer: " + customer.getFullName(), e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Notification> listByUser(Customer customer) {
        List<Notification> notifications = null;
        try {
            notifications = entityManager.createQuery(
                            "SELECT n FROM Notification n WHERE n.customer = :customer",
                            Notification.class)
                    .setParameter("customer", customer)
                    .getResultList();
        } finally {
            entityManager.close();
        }

        return notifications;
    }
}
