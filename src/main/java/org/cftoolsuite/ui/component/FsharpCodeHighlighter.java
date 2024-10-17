package org.cftoolsuite.ui.component;

import com.vaadin.flow.component.dependency.JavaScript;

@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-fsharp.min.js")
class FsharpCodeHighlighter extends CodeHighlighter {
    FsharpCodeHighlighter(String code) {
        super(code, "fsharp");
    }
}