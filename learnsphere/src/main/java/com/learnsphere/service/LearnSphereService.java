package com.learnsphere.service;

import com.learnsphere.entity.*;
import com.learnsphere.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.*;

public class LearnSphereService {

    /* ======== Generic helpers ======== */
    private <T> T tx(SessionWork<T> work) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            T res = work.apply(session);
            tx.commit();
            return res;
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }
    @FunctionalInterface private interface SessionWork<T> { T apply(Session s); }

    /* ======== CRUD: Category ======== */
    public Category createCategory(String name) {
        return tx(s -> { 
            Category c = new Category(name.trim());
            s.save(c);
            return c;
        });
    }
    public Category getCategory(Long id) { return tx(s -> s.get(Category.class, id)); }
    public Category findCategoryByName(String name) {
        return tx(s -> (Category) s.createQuery("from Category c where lower(c.name)=:n")
                .setParameter("n", name.toLowerCase()).uniqueResult());
    }
    public List<Category> listCategories() { return tx(s -> s.createQuery("from Category", Category.class).list()); }
    public void updateCategory(Long id, String newName) {
        tx(s -> { Category c = s.get(Category.class, id); if (c!=null) c.setName(newName.trim()); return null; });
    }
    public void deleteCategory(Long id) { tx(s -> { Category c = s.get(Category.class, id); if (c!=null) s.delete(c); return null; }); }

    /* ======== CRUD: Course & add course ======== */
    public Course addCourse(String name, String instructor, int durationHours, List<String> categoryNames) {
        return tx(s -> {
            Course course = new Course(name.trim(), instructor.trim(), durationHours);
            // attach categories (create if not exists)
            for (String cn : categoryNames) {
                String normalized = cn.trim();
                Category cat = (Category) s.createQuery("from Category c where lower(c.name)=:n")
                        .setParameter("n", normalized.toLowerCase())
                        .uniqueResult();
                if (cat == null) {
                    cat = new Category(normalized);
                    s.save(cat);
                }
                course.getCategories().add(cat);
                cat.getCourses().add(course);
            }
            s.save(course);
            return course;
        });
    }
    public Course getCourse(Long id) { return tx(s -> s.get(Course.class, id)); }
    public List<Course> listCourses() { return tx(s -> s.createQuery("from Course", Course.class).list()); }
    public void updateCourse(Long id, String name, String instructor, Integer durationHours) {
        tx(s -> {
            Course c = s.get(Course.class, id);
            if (c != null) {
                if (name != null) c.setName(name.trim());
                if (instructor != null) c.setInstructor(instructor.trim());
                if (durationHours != null) c.setDurationHours(durationHours);
            }
            return null;
        });
    }
    public void deleteCourse(Long id) { tx(s -> { Course c = s.get(Course.class, id); if (c!=null) s.delete(c); return null; }); }

    /* ======== CRUD: User & register user ======== */
    public User registerUser(String name, String email) {
        return tx(s -> {
            // ensure unique email
            User existing = (User) s.createQuery("from User u where lower(u.email)=:e")
                    .setParameter("e", email.toLowerCase())
                    .uniqueResult();
            if (existing != null) throw new RuntimeException("Email already exists: " + email);
            User u = new User(name.trim(), email.trim().toLowerCase());
            s.save(u);
            return u;
        });
    }
    public User getUser(Long id) { return tx(s -> s.get(User.class, id)); }
    public User findUserByEmail(String email) {
        return tx(s -> (User) s.createQuery("from User u where lower(u.email)=:e")
                .setParameter("e", email.toLowerCase()).uniqueResult());
    }
    public List<User> listUsers() { return tx(s -> s.createQuery("from User", User.class).list()); }
    public void updateUser(Long id, String name, String email) {
        tx(s -> { User u = s.get(User.class, id); if (u!=null) { if (name!=null) u.setName(name.trim()); if (email!=null) u.setEmail(email.trim().toLowerCase()); } return null; });
    }
    public void deleteUser(Long id) { tx(s -> { User u = s.get(User.class, id); if (u!=null) s.delete(u); return null; }); }

    /* ======== Enrollments ======== */
    public Enrollment enrollUserInCourse(Long userId, Long courseId) {
        return tx(s -> {
            User u = s.get(User.class, userId);
            Course c = s.get(Course.class, courseId);
            if (u == null || c == null) throw new RuntimeException("User or Course not found.");
            // check unique
            Enrollment existing = (Enrollment) s.createQuery("from Enrollment e where e.user.id=:uid and e.course.id=:cid")
                    .setParameter("uid", userId).setParameter("cid", courseId).uniqueResult();
            if (existing != null) return existing;
            Enrollment e = new Enrollment(u, c);
            s.save(e);
            return e;
        });
    }
    public List<Course> listCoursesByUser(Long userId) {
        return tx(s -> {
            Query<Course> q = s.createQuery("select e.course from Enrollment e where e.user.id=:uid", Course.class);
            q.setParameter("uid", userId);
            return q.list();
        });
    }

    /* ======== Reviews ======== */
    public Review addReview(Long userId, Long courseId, int rating, String text) {
        if (rating < 0 || rating > 10) throw new IllegalArgumentException("Rating must be 0..10");
        return tx(s -> {
            User u = s.get(User.class, userId);
            Course c = s.get(Course.class, courseId);
            if (u==null || c==null) throw new RuntimeException("User or Course not found.");
            // ensure the user is enrolled
            Long count = (Long) s.createQuery("select count(e.id) from Enrollment e where e.user.id=:uid and e.course.id=:cid")
                    .setParameter("uid", userId).setParameter("cid", courseId).uniqueResult();
            if (count == 0) throw new RuntimeException("User must be enrolled to review this course.");

            // upsert review (unique user+course)
            Review existing = (Review) s.createQuery("from Review r where r.user.id=:uid and r.course.id=:cid")
                    .setParameter("uid", userId).setParameter("cid", courseId).uniqueResult();
            if (existing != null) {
                existing.setRating(rating);
                existing.setText(text);
                return existing;
            } else {
                Review r = new Review(u, c, rating, text);
                s.save(r);
                return r;
            }
        });
    }

    public Double getAverageRatingForCourse(Long courseId) {
        return tx(s -> {
            Double avg = (Double) s.createQuery("select avg(r.rating) from Review r where r.course.id=:cid")
                    .setParameter("cid", courseId).uniqueResult();
            return (avg == null) ? null : avg; // null if no reviews
        });
    }

    /* ======== Search: by keyword (name) or category ======== */
    public List<Course> searchCourses(String keywordOrCategory) {
        String key = keywordOrCategory.trim().toLowerCase();
        return tx(s -> {
            // union: by name OR by category name (distinct)
            List<Course> byName = s.createQuery(
                    "from Course c where lower(c.name) like :k", Course.class)
                    .setParameter("k", "%" + key + "%").list();
            List<Course> byCat = s.createQuery(
                    "select distinct c from Course c join c.categories cat where lower(cat.name) like :k", Course.class)
                    .setParameter("k", "%" + key + "%").list();

            // merge distinct
            Map<Long, Course> map = new LinkedHashMap<>();
            for (Course c : byName) map.put(c.getId(), c);
            for (Course c : byCat) map.putIfAbsent(c.getId(), c);
            return new ArrayList<>(map.values());
        });
    }

    /* ======== Admin view: list users with enrolled courses ======== */
    public Map<User, List<Course>> adminUsersWithEnrollments() {
        return tx(s -> {
            List<User> users = s.createQuery("from User", User.class).list();
            Map<User, List<Course>> out = new LinkedHashMap<>();
            for (User u : users) {
                List<Course> cs = s.createQuery("select e.course from Enrollment e where e.user.id=:uid", Course.class)
                        .setParameter("uid", u.getId()).list();
                out.put(u, cs);
            }
            return out;
        });
    }
}
