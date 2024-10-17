package org.cftoolsuite.ui.component;

import com.vaadin.flow.component.dependency.JavaScript;

@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-erlang.min.js")
class ErlangCodeHighlighter extends CodeHighlighter {
    ErlangCodeHighlighter(String code) {
        super(code, "erlang");
    }
}