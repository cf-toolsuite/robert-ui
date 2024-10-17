package org.cftoolsuite.ui.component;

import com.vaadin.flow.component.dependency.JavaScript;

@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-vbnet.min.js")
class VbNetCodeHighlighter extends CodeHighlighter {
    VbNetCodeHighlighter(String code) {
        super(code, "vbnet");
    }

}
