package org.cftoolsuite.ui.component;

import com.vaadin.flow.component.dependency.JavaScript;

@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-python.min.js")
class PythonCodeHighlighter extends CodeHighlighter {
    PythonCodeHighlighter(String code) {
        super(code, "python");
    }
}