package org.cftoolsuite.ui.component;

import com.vaadin.flow.component.dependency.JavaScript;

@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-haskell.min.js")
class HaskellCodeHighlighter extends CodeHighlighter {
    HaskellCodeHighlighter(String code) {
        super(code, "haskell");
    }

}
