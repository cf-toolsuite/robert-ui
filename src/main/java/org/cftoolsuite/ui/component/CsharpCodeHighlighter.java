package org.cftoolsuite.ui.component;

import com.vaadin.flow.component.dependency.JavaScript;

@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-csharp.min.js")
class CsharpCodeHighlighter extends CodeHighlighter {
    CsharpCodeHighlighter(String code) {
        super(code, "csharp");
    }
}