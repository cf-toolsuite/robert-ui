package org.cftoolsuite.ui.component;

import com.vaadin.flow.component.dependency.JavaScript;

@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-aspnet.min.js")
class AspNetCodeHighlighter extends CodeHighlighter {
    AspNetCodeHighlighter(String code) {
        super(code, "aspnet");
    }
}