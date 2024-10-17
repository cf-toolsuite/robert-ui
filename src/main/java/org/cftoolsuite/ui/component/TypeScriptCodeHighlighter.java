package org.cftoolsuite.ui.component;

import com.vaadin.flow.component.dependency.JavaScript;

@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-typescript.min.js")
class TypeScriptCodeHighlighter extends CodeHighlighter {
    TypeScriptCodeHighlighter(String code) {
        super(code, "typescript");
    }

}
