package org.cftoolsuite.ui.component;

import com.vaadin.flow.component.dependency.JavaScript;

@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-rust.min.js")
class RustCodeHighlighter extends CodeHighlighter {
    RustCodeHighlighter(String code) {
        super(code, "rust");
    }

}
