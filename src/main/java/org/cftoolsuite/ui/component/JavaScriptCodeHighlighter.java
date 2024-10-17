package org.cftoolsuite.ui.component;

import com.vaadin.flow.component.dependency.JavaScript;

@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-javascipt.min.js")
class JavaScriptCodeHighlighter extends CodeHighlighter {
    JavaScriptCodeHighlighter(String code) {
        super(code, "javascript");
    }
}