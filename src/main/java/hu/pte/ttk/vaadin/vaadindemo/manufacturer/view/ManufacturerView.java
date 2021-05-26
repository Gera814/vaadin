package hu.pte.ttk.vaadin.vaadindemo.manufacturer.view;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import hu.pte.ttk.vaadin.vaadindemo.manufacturer.entity.ManufacturerEntity;
import hu.pte.ttk.vaadin.vaadindemo.manufacturer.service.ManufacturerService;
import hu.pte.ttk.vaadin.vaadindemo.menu.MenuComponent;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

// http://localhost:8080/author
@Route
public class ManufacturerView extends VerticalLayout {
    private VerticalLayout form;
    private ManufacturerEntity selectedAuthor;
    private Binder<ManufacturerEntity> binder;
    private TextField name;
    private Button deleteBtn = new Button("Delete", VaadinIcon.TRASH.create());

    @Autowired
    private ManufacturerService service;

    @PostConstruct
    public void init() {
        add(new MenuComponent());
        add(new Text("Manufacturers"));
        Grid<ManufacturerEntity> grid = new Grid<>();
        grid.setItems(service.getAll());
        grid.addColumn(ManufacturerEntity::getId).setHeader("Id");
        grid.addColumn(ManufacturerEntity::getName).setHeader("Name");
        grid.asSingleSelect().addValueChangeListener(event -> {
            selectedAuthor = event.getValue();
            binder.setBean(selectedAuthor);
            form.setVisible(selectedAuthor != null);
            deleteBtn.setEnabled(selectedAuthor != null);
        });
        addButtonBar(grid);
        add(grid);
        addForm(grid);
    }

    private void addForm(Grid<ManufacturerEntity> grid) {
        form = new VerticalLayout();
        binder = new Binder<>(ManufacturerEntity.class);
        HorizontalLayout nameField = new HorizontalLayout();
        name = new TextField();
        nameField.add(new Text("Name"), name);
        HorizontalLayout authorField = new HorizontalLayout();
        form.add(nameField, authorField, addSaveBtn(grid));
        add(form);
        form.setVisible(false);
        binder.bindInstanceFields(this);
    }

    private Button addSaveBtn(Grid<ManufacturerEntity> grid) {
        Button saveBtn = new Button("Save", VaadinIcon.SAFE.create());
        saveBtn.addClickListener(buttonClickEvent -> {
            //mentés
            if (selectedAuthor.getId() == null) {
                ManufacturerEntity authorEntity = new ManufacturerEntity();
                authorEntity.setName(selectedAuthor.getName());
                service.add(authorEntity);
                grid.setItems(service.getAll());
                selectedAuthor = null;
                Notification.show("Sikeres mentés");
            } else {
                service.update(selectedAuthor);
                grid.setItems(service.getAll());
                Notification.show("Sikeres módosítás");
            }
            form.setVisible(false);
        });
        return saveBtn;

    }

    private void addButtonBar(Grid<ManufacturerEntity> grid) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        deleteBtn.addClickListener(buttonClickEvent -> {
            service.remove(selectedAuthor);
            Notification.show("Sikeres törlés");
            selectedAuthor = null;
            grid.setItems(service.getAll());
            form.setVisible(false);

        });
        deleteBtn.setEnabled(false);

        Button addBtn = new Button("Add", VaadinIcon.PLUS.create());
        addBtn.addClickListener(buttonClickEvent -> {
            selectedAuthor = new ManufacturerEntity();
            binder.setBean(selectedAuthor);
            form.setVisible(true);

        });
        horizontalLayout.add(deleteBtn);
        horizontalLayout.add(addBtn);
        add(horizontalLayout);
    }
}
