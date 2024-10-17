package org.cftoolsuite.ui.component;

import com.vaadin.flow.component.dependency.JavaScript;

@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-go.min.js")
class GoCodeHighlighter extends CodeHighlighter {
    GoCodeHighlighter(String code) {
        super(code, "go");
    }
}