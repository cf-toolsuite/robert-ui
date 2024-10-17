package org.cftoolsuite.ui.component;

import com.vaadin.flow.component.dependency.JavaScript;

@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-markdown.min.js")
class MarkdownCodeHighlighter extends CodeHighlighter {
    MarkdownCodeHighlighter(String code) {
        super(code, "markdown");
    }

}
