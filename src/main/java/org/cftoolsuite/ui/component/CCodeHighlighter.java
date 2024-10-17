package org.cftoolsuite.ui.component;

import com.vaadin.flow.component.dependency.JavaScript;

@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-c.min.js")
class CCodeHighlighter extends CodeHighlighter {
    CCodeHighlighter(String code) {
        super(code, "c");
    }
}