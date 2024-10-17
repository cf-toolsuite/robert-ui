package org.cftoolsuite.ui.component;

import com.vaadin.flow.component.dependency.JavaScript;

@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-r.min.js")
class RCodeHighlighter extends CodeHighlighter {
    RCodeHighlighter(String code) {
        super(code, "r");
    }

}
