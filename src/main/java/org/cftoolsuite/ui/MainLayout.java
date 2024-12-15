package org.cftoolsuite.ui;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.StreamResource;
import org.cftoolsuite.client.ModeClient;
import org.cftoolsuite.ui.view.ChatView;
import org.cftoolsuite.ui.view.HomeView;
import org.cftoolsuite.ui.view.IngestView;
import org.cftoolsuite.ui.view.RefactorView;
import org.cftoolsuite.ui.view.SearchView;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;


public class MainLayout extends AppLayout {

    private static final long serialVersionUID = 1L;

    public MainLayout(ModeClient modeClient) {
    	Tab homeTab = createTab(VaadinIcon.HOME.create(), "Home", HomeView.class);

    	Tabs actionTabs = createTabs();
		if (modeClient.isAdvancedModeConfigured()) {
    		Tab ingestTab = createTab(VaadinIcon.PLUG.create(),"Ingest", IngestView.class);
			actionTabs.add(ingestTab);
			Tab chatTab = createTab(VaadinIcon.CHAT.create(),"Chat", ChatView.class);
			actionTabs.add(chatTab);
			Tab searchTab = createTab(VaadinIcon.SEARCH.create(),"Search", SearchView.class);
			actionTabs.add(searchTab);
		}
    	Tab refactorTab = createTab(VaadinIcon.PLAY.create(), "Refactor", RefactorView.class);
    	actionTabs.add(refactorTab);

    	addToNavbar(true, homeTab, new DrawerToggle());
    	addToDrawer(getLogoImage(), actionTabs);
    }

    private Tabs createTabs() {
    	Tabs menu = new Tabs();
    	menu.setWidthFull();
    	menu.setOrientation(Tabs.Orientation.VERTICAL);
    	menu.setFlexGrowForEnclosedTabs(1);
    	return menu;
    }

    private Tab createTab(Icon icon, String label, Class<? extends Component> layout) {
		RouterLink link = new RouterLink();
    	link.setRoute(layout);
		Div container = new Div();
		container.getStyle().set("display", "flex");
		container.getStyle().set("justify-content", "flex-start");
		container.getStyle().set("align-items", "center");
		container.getStyle().set("width", "100%");
		icon.getStyle().set("margin-right", "8px");
		container.add(icon, new Span(label));
		link.add(container);
		Tab tab = new Tab();
		tab.add(link);
		return tab;
    }

	private Image getLogoImage() {
		StreamResource imageResource = new StreamResource("robert.png",
				() -> getClass().getResourceAsStream("/static/robert.png"));
		Image logo = new Image(imageResource, "Logo");
		logo.setWidth("240px");
		return logo;
	}

}
