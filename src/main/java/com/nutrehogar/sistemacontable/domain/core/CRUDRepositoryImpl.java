package com.nutrehogar.sistemacontable.domain.core;

import com.nutrehogar.sistemacontable.application.repository.CRUDRepository;
import com.nutrehogar.sistemacontable.exception.RepositoryException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class CRUDRepositoryImpl<T, ID> implements CRUDRepository<T, ID> {

    private final Class<T> entityClass;

    public CRUDRepositoryImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }


    @Override
    public List<T> findAll() throws RepositoryException {
        return TransactionManager.executeInTransaction(session ->
                session.createQuery("from " + entityClass.getSimpleName() + " order by id", entityClass).list()
        );
    }

    // ... (implementar otros métodos de manera similar)
    @Override
    public void save(T entity) throws RepositoryException {
//        WriteExecutor.submitWrite(() -> {
            TransactionManager.executeInTransaction(session -> {
                session.persist(entity);
                return null;
            });
//            return null;
//        });
    }

    @Override
    public void saveAll(List<T> entities) throws RepositoryException {
//        WriteExecutor.submitWrite(() -> {
            TransactionManager.executeInTransaction(session -> {
                entities.forEach(session::persist);
                return null;
            });
//            return null;
//        });
    }

    @Override
    public Optional<T> findById(ID id) throws RepositoryException {
        return Optional.ofNullable(
                TransactionManager.executeInTransaction(session -> session.get(entityClass, id))
        );
    }

    @Override
    public T update(T entity) throws RepositoryException, ExecutionException, InterruptedException {
//        return WriteExecutor.submitWrite(() ->
               return TransactionManager.executeInTransaction(session -> session.merge(entity));
//        ).get(); // Usar .get() para obtener el resultado del Future
    }

    @Override
    public void delete(T entity) throws RepositoryException {
//        WriteExecutor.submitWrite(() -> {
            TransactionManager.executeInTransaction(session -> {
                session.remove(session.contains(entity) ? entity : session.merge(entity));
                return null;
            });
//            return null;
//        });
    }

    @Override
    public void deleteById(ID id) throws RepositoryException {
//        WriteExecutor.submitWrite(() -> {
            TransactionManager.executeInTransaction(session -> {
                T entity = session.get(entityClass, id);
                if (entity != null) {
                    session.remove(entity);
                }
                return null;
            });
//            return null;
//        });
    }

    @Override
    public long count() throws RepositoryException {
        return TransactionManager.executeInTransaction(session -> {
            try {
                Long count = session.createQuery("select count(*) from " + entityClass.getSimpleName(), Long.class)
                        .uniqueResult();
                return count != null ? count : 0L; // Devuelve 0 si count es null
            } catch (Exception e) {
                throw new RepositoryException("Error al contar entidades", e);
            }
        });
    }

    public boolean existsById(ID id) throws RepositoryException {
        return findById(id).isPresent();
    }
}