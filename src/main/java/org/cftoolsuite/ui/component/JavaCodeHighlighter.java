package org.cftoolsuite.ui.component;

import com.vaadin.flow.component.dependency.JavaScript;

@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-java.min.js")
class JavaCodeHighlighter extends CodeHighlighter {
    JavaCodeHighlighter(String code) {
        super(code, "java");
    }
}