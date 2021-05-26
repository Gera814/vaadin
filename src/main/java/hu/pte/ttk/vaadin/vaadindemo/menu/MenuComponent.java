package hu.pte.ttk.vaadin.vaadindemo.menu;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import hu.pte.ttk.vaadin.vaadindemo.security.SecurityUtils;

public class MenuComponent extends HorizontalLayout {

    public MenuComponent() {
        Anchor main = new Anchor();
        main.setText("Home");
        main.setHref("/");
        add(main);

        Anchor link = new Anchor();
        link.setText("Car page");
        link.setHref("/car");
        add(link);

        Anchor author = new Anchor();
        author.setText("Manufacturer page");
        author.setHref("/manufacturer");
        add(author);

        if(SecurityUtils.isAdmin()){
            Anchor user = new Anchor();
            user.setText("Users page");
            user.setHref("/user");
            add(user);
        }
    }
}
