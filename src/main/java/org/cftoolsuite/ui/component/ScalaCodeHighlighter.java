package org.cftoolsuite.ui.component;

import com.vaadin.flow.component.dependency.JavaScript;

@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-scala.min.js")
class ScalaCodeHighlighter extends CodeHighlighter {
    ScalaCodeHighlighter(String code) {
        super(code, "scala");
    }

}
