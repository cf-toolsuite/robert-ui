package org.cftoolsuite.ui.component;

import com.vaadin.flow.component.dependency.JavaScript;

@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-php.min.js")
class PhpCodeHighlighter extends CodeHighlighter {
    PhpCodeHighlighter(String code) {
        super(code, "php");
    }
}