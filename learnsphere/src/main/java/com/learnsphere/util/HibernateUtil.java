package com.learnsphere.util;

import com.learnsphere.entity.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure() // reads hibernate.cfg.xml
                    .build();

            MetadataSources sources = new MetadataSources(registry)
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(Category.class)
                    .addAnnotatedClass(Course.class)
                    .addAnnotatedClass(Enrollment.class)
                    .addAnnotatedClass(Review.class);

            return sources.buildMetadata().buildSessionFactory();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("SessionFactory build failed: " + ex.getMessage());
        }
    }

    public static SessionFactory getSessionFactory() { return sessionFactory; }
}
