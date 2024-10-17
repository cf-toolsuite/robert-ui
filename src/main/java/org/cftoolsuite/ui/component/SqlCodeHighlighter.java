package org.cftoolsuite.ui.component;

import com.vaadin.flow.component.dependency.JavaScript;

@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-sql.min.js")
class SqlCodeHighlighter extends CodeHighlighter {
    SqlCodeHighlighter(String code) {
        super(code, "sql");
    }

}
