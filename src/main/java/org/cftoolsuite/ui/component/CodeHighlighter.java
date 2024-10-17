package org.cftoolsuite.ui.component;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.StyleSheet;

@Tag("div")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/prism.min.js")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/plugins/line-numbers/prism-line-numbers.min.js")
@StyleSheet("https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/plugins/line-numbers/prism-line-numbers.min.css")
@StyleSheet("https://cdnjs.cloudflare.com/ajax/libs/prism-themes/1.9.0/prism-nord.min.css")
public abstract class CodeHighlighter extends Html {

    protected CodeHighlighter(String code, String language) {
        super("<pre class=\"line-numbers\"><code class=\"language-" + language + "\">" +
              escapeHtml(code) + "</code></pre>");
        getElement().executeJs("Prism.highlightAll();", getElement());
    }

    private static String escapeHtml(String html) {
        return html.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#039;");
    }
}