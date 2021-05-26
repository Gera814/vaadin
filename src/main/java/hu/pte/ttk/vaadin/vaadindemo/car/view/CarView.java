package hu.pte.ttk.vaadin.vaadindemo.car.view;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import hu.pte.ttk.vaadin.vaadindemo.manufacturer.entity.ManufacturerEntity;
import hu.pte.ttk.vaadin.vaadindemo.manufacturer.service.ManufacturerService;
import hu.pte.ttk.vaadin.vaadindemo.car.entity.CarEntity;
import hu.pte.ttk.vaadin.vaadindemo.car.service.CarService;
import hu.pte.ttk.vaadin.vaadindemo.menu.MenuComponent;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

// http://localhost:8080/car
@Route
public class CarView extends VerticalLayout {
    private VerticalLayout form;
    private CarEntity selectedCar;
    private Binder<CarEntity> binder;
    private TextField type;
    private TextField doors;
    private TextField year;
    private ComboBox<ManufacturerEntity> manufacturer;
    private Button deleteBtn = new Button("Delete", VaadinIcon.TRASH.create());
    private TextField filterText = new TextField();


    @Autowired
    private CarService service;
    @Autowired
    private ManufacturerService manufacturerService;
    Grid<CarEntity> grid = new Grid<>();

    @PostConstruct
    public void init() {
        add(new MenuComponent());
        add(new Text("Cars"));

        filterText.setPlaceholder("search (type & year)");
        filterText.setClearButtonVisible(true);

        filterText.setValueChangeMode(ValueChangeMode.EAGER);
        filterText.addValueChangeListener(e -> updateList());

        grid.setItems(service.getAll());
        grid.addColumn(CarEntity::getId).setHeader("Id");
        grid.addColumn(CarEntity::getType).setHeader("Type");
        grid.addColumn(carEntity -> {
                    if (carEntity.getManufacturer() != null) {
                        return carEntity.getManufacturer().getName();
                    }
                    return "";
                }
        ).setHeader("Manufacturer");
        grid.addColumn(CarEntity::getDoors).setHeader("Doors");
        grid.addColumn(CarEntity::getYear).setHeader("Year");
        grid.asSingleSelect().addValueChangeListener(event -> {
            selectedCar = event.getValue();
            binder.setBean(selectedCar);
            form.setVisible(selectedCar != null);
            deleteBtn.setEnabled(selectedCar != null);

        });

        addButtonBar(grid);
        add(filterText, grid);
        addForm(grid);
    }

    private void updateList() {
        grid.setItems(service.findAll(filterText.getValue()));
    }

    private void addForm(Grid<CarEntity> grid) {
        form = new VerticalLayout();
        binder = new Binder<>(CarEntity.class);

        // TYPE
        HorizontalLayout typeField = new HorizontalLayout();
        type = new TextField();
        typeField.add(new Text("Type"), type);

        // MANUFACTURER
        HorizontalLayout manufacturerField = new HorizontalLayout();
        manufacturer = new ComboBox<>();
        manufacturer.setItems(manufacturerService.getAll());
        manufacturer.setItemLabelGenerator(manufacturerEntity -> {
            return manufacturerEntity.getName();
        });
        manufacturerField.add(new Text("Manufacturer"), manufacturer);

        // DOORS
        HorizontalLayout doorsField = new HorizontalLayout();
        doors = new TextField();
        doorsField.add(new Text("Doors"), doors);

        // YEAR
        HorizontalLayout yearField = new HorizontalLayout();
        year = new TextField();
        yearField.add(new Text("Year"), year);

        form.add(typeField, manufacturerField, doorsField, yearField, addSaveBtn(grid));

        add(form);
        form.setVisible(false);
        binder.bindInstanceFields(this);
    }

    private Button addSaveBtn(Grid<CarEntity> grid) {
        Button saveBtn = new Button("Save", VaadinIcon.SAFE.create());
        saveBtn.addClickListener(buttonClickEvent -> {
            //mentés
            if (selectedCar.getId() == null) {
                CarEntity carEntity = new CarEntity();
                carEntity.setType(selectedCar.getType());
                carEntity.setDoors(selectedCar.getDoors());
                carEntity.setManufacturer(selectedCar.getManufacturer());
                carEntity.setYear(selectedCar.getYear());
                service.add(carEntity);
                grid.setItems(service.getAll());
                selectedCar = null;
                Notification.show("Sikeres mentés");
            } else {
                service.update(selectedCar);
                grid.setItems(service.getAll());
                Notification.show("Sikeres módosítás");
            }
            form.setVisible(false);
        });
        return saveBtn;

    }

    private void addButtonBar(Grid<CarEntity> grid) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        deleteBtn.addClickListener(buttonClickEvent -> {
            service.remove(selectedCar);
            Notification.show("Sikeres törlés");
            selectedCar = null;
            grid.setItems(service.getAll());
            form.setVisible(false);

        });
        deleteBtn.setEnabled(false);

        Button addBtn = new Button("Add", VaadinIcon.PLUS.create());
        addBtn.addClickListener(buttonClickEvent -> {
            selectedCar = new CarEntity();
            binder.setBean(selectedCar);
            form.setVisible(true);

        });
        horizontalLayout.add(deleteBtn);
        horizontalLayout.add(addBtn);
        add(horizontalLayout);
    }
}
