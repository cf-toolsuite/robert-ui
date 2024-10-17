package org.cftoolsuite.ui.component;

import com.vaadin.flow.component.dependency.JavaScript;

@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-julia.min.js")
class JuliaCodeHighlighter extends CodeHighlighter {
    JuliaCodeHighlighter(String code) {
        super(code, "julia");
    }

}
