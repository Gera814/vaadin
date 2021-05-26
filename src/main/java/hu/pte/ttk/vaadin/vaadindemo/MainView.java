package hu.pte.ttk.vaadin.vaadindemo;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import hu.pte.ttk.vaadin.vaadindemo.menu.MenuComponent;

@Route
public class MainView extends VerticalLayout {

    public MainView() {
        add(new MenuComponent());
        Text text = new Text("Hi! welcome to my car app!");
        add(text);
    }
}
