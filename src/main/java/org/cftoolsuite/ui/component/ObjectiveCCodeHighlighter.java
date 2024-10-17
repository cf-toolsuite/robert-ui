package org.cftoolsuite.ui.component;

import com.vaadin.flow.component.dependency.JavaScript;

@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-objectivec.min.js")
class ObjectiveCCodeHighlighter extends CodeHighlighter {
    ObjectiveCCodeHighlighter(String code) {
        super(code, "objectivec");
    }

}
