package org.cftoolsuite.ui.component;

import com.vaadin.flow.component.dependency.JavaScript;

@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-matlab.min.js")
class MatlabCodeHighlighter extends CodeHighlighter {
    MatlabCodeHighlighter(String code) {
        super(code, "matlab");
    }

}
