package org.cftoolsuite.ui.component;

import com.vaadin.flow.component.dependency.JavaScript;

@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-kotlin.min.js")
class KotlinCodeHighlighter extends CodeHighlighter {
    KotlinCodeHighlighter(String code) {
        super(code, "kotlin");
    }

}
