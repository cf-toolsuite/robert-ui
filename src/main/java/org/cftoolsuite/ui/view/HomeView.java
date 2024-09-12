package org.cftoolsuite.ui.view;

import org.cftoolsuite.client.ModeClient;
import org.cftoolsuite.ui.MainLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import io.micrometer.common.util.StringUtils;

@Route(value = "", layout = MainLayout.class)
@PageTitle("robert-ui » Home")
public class HomeView extends Div {

    private static Logger log = LoggerFactory.getLogger(HomeView.class);

    private final ModeClient modeClient;

    public HomeView(ModeClient modeClient) {
        this.modeClient = modeClient;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        log.trace("Navigated to ROOT... now redirecting!");
        String mode = modeClient.getMode();
        if (StringUtils.isNotBlank(mode) && mode.equalsIgnoreCase("advanced")) {
            UI.getCurrent().navigate(IngestView.class);
        } else {
            UI.getCurrent().navigate(RefactorView.class);
        }
    }
}