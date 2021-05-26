package hu.pte.ttk.vaadin.vaadindemo.user.view;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import hu.pte.ttk.vaadin.vaadindemo.menu.MenuComponent;
import hu.pte.ttk.vaadin.vaadindemo.user.entity.RoleEntity;
import hu.pte.ttk.vaadin.vaadindemo.user.entity.UserEntity;
import hu.pte.ttk.vaadin.vaadindemo.user.service.RoleService;
import hu.pte.ttk.vaadin.vaadindemo.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;
import java.security.SecureRandom;
import java.util.Collections;

// http://localhost:8080/user
@Route
public class UserView extends VerticalLayout {
    private VerticalLayout form;
    private UserEntity selectedUser;
    private Binder<UserEntity> binder;
    private TextField username;
    private String password;
    private ComboBox<RoleEntity> comboBox;
    private Button deleteBtn = new Button("Delete", VaadinIcon.TRASH.create());

    @Autowired
    private UserService service;
    @Autowired
    private RoleService roleService;

    @PostConstruct
    public void init() {
        add(new MenuComponent());
        add(new Text("A felhasználók oldala"));
        Grid<UserEntity> grid = new Grid<>();
        grid.setItems(service.getAll());
        grid.addColumn(UserEntity::getId).setHeader("Id");
        grid.addColumn(UserEntity::getUsername).setHeader("Username");
        grid.addColumn(userEntity -> {
                    if (userEntity.getAuthorities() != null) {
                        StringBuilder builder = new StringBuilder();
                        userEntity.getAuthorities().forEach(roleEntity -> {
                            builder.append(roleEntity.getAuthority()).append(",");
                        });
                        return builder.toString();
                    }
                    return "";
                }
        ).setHeader("Author");
        grid.asSingleSelect().addValueChangeListener(event -> {
            selectedUser = event.getValue();
            binder.setBean(selectedUser);
            form.setVisible(selectedUser != null);
            deleteBtn.setEnabled(selectedUser != null);

        });
        addButtonBar(grid);
        add(grid);
        addForm(grid);
    }

    private void addForm(Grid<UserEntity> grid) {
        form = new VerticalLayout();
        binder = new Binder<>(UserEntity.class);

        HorizontalLayout nameField = new HorizontalLayout();
        username = new TextField();
        nameField.add(new Text("Username"), username);

        HorizontalLayout authorField = new HorizontalLayout();
        comboBox = new ComboBox<>();
        comboBox.setItems(roleService.getAll());
        comboBox.setItemLabelGenerator(authorEntity -> authorEntity.getAuthority());
        authorField.add(new Text("Authorities"), comboBox);


        form.add(nameField, authorField, addSaveBtn(grid));
        add(form);
        form.setVisible(false);
        binder.bindInstanceFields(this);
    }

    private Button addSaveBtn(Grid<UserEntity> grid) {
        Button saveBtn = new Button("Save", VaadinIcon.SAFE.create());
        saveBtn.addClickListener(buttonClickEvent -> {
            //mentés
            if (selectedUser.getId() == null) {
                UserEntity bookEntity = new UserEntity();
                bookEntity.setUsername(selectedUser.getUsername());
                bookEntity.setAuthorities(Collections.singletonList(comboBox.getValue()));
                password = generateRandomPassword(10);
                System.out.println(password);
                bookEntity.setPassword(new BCryptPasswordEncoder().encode(password));
                service.add(bookEntity);
                grid.setItems(service.getAll());
                selectedUser = null;
                Notification.show("Sikeres mentés");
                Dialog dialog = new Dialog();
                dialog.add(new Text("Your password: "+ password));
                dialog.setWidth("400px");
                dialog.setHeight("150px");
                dialog.open();
            } else {
                service.update(selectedUser);
                grid.setItems(service.getAll());
                Notification.show("Sikeres módosítás");
            }
            form.setVisible(false);
        });
        return saveBtn;

    }

    private void addButtonBar(Grid<UserEntity> grid) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        deleteBtn.addClickListener(buttonClickEvent -> {
            service.remove(selectedUser);
            Notification.show("Sikeres törlés");
            selectedUser = null;
            grid.setItems(service.getAll());
            form.setVisible(false);

        });
        deleteBtn.setEnabled(false);

        Button addBtn = new Button("Add", VaadinIcon.PLUS.create());
        addBtn.addClickListener(buttonClickEvent -> {
            selectedUser = new UserEntity();
            binder.setBean(selectedUser);
            form.setVisible(true);

        });
        horizontalLayout.add(deleteBtn);
        horizontalLayout.add(addBtn);
        add(horizontalLayout);
    }

    private static String generateRandomPassword(int len)
    {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < len; i++)
        {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }

        return sb.toString();
    }
}
