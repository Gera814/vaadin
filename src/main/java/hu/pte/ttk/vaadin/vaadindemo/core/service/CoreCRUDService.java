package hu.pte.ttk.vaadin.vaadindemo.core.service;

import hu.pte.ttk.vaadin.vaadindemo.core.entity.CoreEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CoreCRUDService<T extends CoreEntity> {

    @Transactional
    void add(T entity);

    @Transactional
    void remove(T entity);

    List<T> getAll();

    List<T> findAll(String t);

    @Transactional
    void update(T entity);

    T findById(Long id);
}
