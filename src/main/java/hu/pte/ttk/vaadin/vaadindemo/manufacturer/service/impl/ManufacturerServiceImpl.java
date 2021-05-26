package hu.pte.ttk.vaadin.vaadindemo.manufacturer.service.impl;

import hu.pte.ttk.vaadin.vaadindemo.manufacturer.entity.ManufacturerEntity;
import hu.pte.ttk.vaadin.vaadindemo.manufacturer.service.ManufacturerService;
import hu.pte.ttk.vaadin.vaadindemo.core.service.impl.CoreCRUDServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ManufacturerServiceImpl extends CoreCRUDServiceImpl<ManufacturerEntity> implements ManufacturerService {

    @Override
    protected void updateCore(ManufacturerEntity persistedEntity, ManufacturerEntity entity) {
        persistedEntity.setName(entity.getName());
    }

    @Override
    protected Class<ManufacturerEntity> getManagedClass() {
        return ManufacturerEntity.class;
    }
}
