package com.nutrehogar.sistemacontable.application.repository;

import com.nutrehogar.sistemacontable.exception.RepositoryException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.OptimisticLockException;
import org.hibernate.ObjectDeletedException;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Interfaz base para repositorios que gestionan operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * para una entidad específica.
 *
 * @param <T>  El tipo de la entidad gestionada por este repositorio.
 * @param <ID> El tipo del identificador único de la entidad.
 */
public interface CRUDRepository<T, ID> extends SimpleRepository<T> {

    /**
     * Busca una entidad por su identificador único.
     *
     * @param id El identificador único de la entidad.
     * @return Un {@link Optional} que contiene la entidad si se encuentra, o vacío si no existe.
     * @throws IllegalArgumentException Si el argumento {@code id} o {@code entityClass} es nulo.
     */
    Optional<T> findById(ID id) throws RepositoryException;

    /**
     * Obtiene todas las entidades gestionadas por este repositorio.
     *
     * @return Una lista de todas las entidades. Si no hay entidades, devuelve una lista vacía.
     */
    List<T> findAll() throws RepositoryException;

    /**
     * Guarda una entidad en el repositorio.
     * Si la entidad ya existe, puede lanzar una excepción o actualizarla, dependiendo de la implementación.
     *
     * @param entity La entidad a guardar.
     * @throws IllegalArgumentException     Si la entidad es null o no es una entidad gestionada.
     * @throws EntityExistsException        Si ya existe una entidad con el mismo identificador en la base de datos.
     * @throws ConstraintViolationException Si se viola una restricción de la base de datos (clave única, not null, etc.).
     */
    void save(T entity) throws RepositoryException;

    /**
     * Guarda una lista de entidades en el repositorio.
     * Si alguna entidad ya existe, puede lanzar una excepción o actualizarla, dependiendo de la implementación.
     *
     * @param entities La lista de entidades a guardar.
     * @throws IllegalArgumentException     Si la entidad es null o no es una entidad gestionada.
     * @throws EntityExistsException        Si ya existe una entidad con el mismo identificador en la base de datos.
     * @throws ConstraintViolationException Si se viola una restricción de la base de datos (clave única, not null, etc.).
     */
    void saveAll(List<T> entities) throws RepositoryException;

    /**
     * Actualiza una entidad existente en el repositorio.
     * Si la entidad no existe, puede lanzar una excepción o crearla, dependiendo de la implementación.
     *
     * @param entity La entidad a actualizar.
     * @throws IllegalArgumentException     Si la entidad es null o no es válida.
     * @throws ObjectDeletedException       Si se intenta actualizar una entidad que ha sido eliminada.
     * @throws OptimisticLockException      Si hay problemas de concurrencia (bloqueos o versiones desactualizadas).
     * @throws ConstraintViolationException Si se violan restricciones de la base de datos.
     */
    T update(T entity) throws RepositoryException, ExecutionException, InterruptedException;

    /**
     * Elimina una entidad del repositorio.
     * Si la entidad no existe, puede lanzar una excepción o no hacer nada, dependiendo de la implementación.
     *
     * @param entity La entidad a eliminar.
     * @throws IllegalArgumentException Si la entidad es nula.
     */
    void delete(T entity) throws RepositoryException;

    /**
     * Elimina una entidad del repositorio por su identificador único.
     * Si la entidad no existe, puede lanzar una excepción o no hacer nada, dependiendo de la implementación.
     *
     * @param id El identificador único de la entidad a eliminar.
     * @throws IllegalArgumentException Si el identificador es nulo.
     */
    void deleteById(ID id) throws RepositoryException;

    /**
     * Cuenta el número total de entidades gestionadas por este repositorio.
     *
     * @return El número total de entidades.
     */
    long count() throws RepositoryException;

    boolean existsById(ID id);
}