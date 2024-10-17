package org.cftoolsuite.ui.component;

import com.vaadin.flow.component.dependency.JavaScript;

@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-cpp.min.js")
class CppCodeHighlighter extends CodeHighlighter {
    CppCodeHighlighter(String code) {
        super(code, "cpp");
    }

}
