package org.cftoolsuite.ui.component;

import com.vaadin.flow.component.dependency.JavaScript;

@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-swift.min.js")
class SwiftCodeHighlighter extends CodeHighlighter {
    SwiftCodeHighlighter(String code) {
        super(code, "swift");
    }
}