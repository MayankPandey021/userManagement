package com.example.userManagement.service;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public abstract class GenericService<T, ID> {

    protected final JpaRepository<T, ID> repository;

    protected GenericService(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    public T create(T entity) {
        return repository.save(entity);
    }

    public T getById(ID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entity not found with ID: " + id));
    }

    public List<T> getAll() {
        return repository.findAll();
    }

    public T update(ID id, T updatedEntity) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Entity not found with ID: " + id);
        }
        return repository.save(updatedEntity);
    }

    public void delete(ID id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Entity not found with ID: " + id);
        }
        repository.deleteById(id);
    }
}