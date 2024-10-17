package org.cftoolsuite.ui.component;

import com.vaadin.flow.component.dependency.JavaScript;

@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-perl.min.js")
class PerlCodeHighlighter extends CodeHighlighter {
    PerlCodeHighlighter(String code) {
        super(code, "perl");
    }

}
