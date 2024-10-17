package org.cftoolsuite.ui.component;

import com.vaadin.flow.component.dependency.JavaScript;

@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-ruby.min.js")
class RubyCodeHighlighter extends CodeHighlighter {
    RubyCodeHighlighter(String code) {
        super(code, "ruby");
    }

}
