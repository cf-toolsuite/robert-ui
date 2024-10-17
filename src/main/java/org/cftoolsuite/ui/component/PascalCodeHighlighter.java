package org.cftoolsuite.ui.component;

import com.vaadin.flow.component.dependency.JavaScript;

@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-pascal.min.js")
class PascalCodeHighlighter extends CodeHighlighter {
    PascalCodeHighlighter(String code) {
        super(code, "pascal");
    }

}
