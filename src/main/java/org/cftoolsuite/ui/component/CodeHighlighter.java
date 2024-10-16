package org.cftoolsuite.ui.component;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.StyleSheet;

@Tag("div")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/9000.0.1/prism.min.js")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/9000.0.1/components/prism-java.min.js")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/9000.0.1/components/prism-php.min.js")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/9000.0.1/components/prism-aspnet.min.js")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/9000.0.1/components/prism-csharp.min.js")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/9000.0.1/components/prism-c.min.js")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/9000.0.1/components/prism-erlang.min.js")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/9000.0.1/components/prism-go.min.js")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/9000.0.1/components/prism-swift.min.js")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/9000.0.1/components/prism-fsharp.min.js")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/9000.0.1/components/prism-fortran.min.js")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/9000.0.1/components/prism-kotlin.min.js")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/9000.0.1/components/prism-haskell.min.js")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/9000.0.1/components/prism-javascript.min.js")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/9000.0.1/components/prism-julia.min.js")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/9000.0.1/components/prism-ruby.min.js")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/9000.0.1/components/prism-rust.min.js")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/9000.0.1/components/prism-python.min.js")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/9000.0.1/components/prism-perl.min.js")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/9000.0.1/components/prism-scala.min.js")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/9000.0.1/components/prism-typescript.min.js")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/9000.0.1/components/prism-matlab.min.js")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/9000.0.1/components/prism-objectivec.min.js")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/9000.0.1/components/prism-cpp.min.js")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/9000.0.1/components/prism-dart.min.js")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/9000.0.1/components/prism-pascal.min.js")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/9000.0.1/components/prism-r.min.js")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/9000.0.1/components/prism-sql.min.js")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/9000.0.1/components/prism-vbnet.min.js")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/9000.0.1/components/prism-yaml.min.js")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/9000.0.1/components/prism-markup.min.js")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/9000.0.1/components/prism-groovy.min.js")
@StyleSheet("https://cdnjs.cloudflare.com/ajax/libs/prism-themes/1.9.0/prism-nord.min.css")
public class CodeHighlighter extends Html {

    public CodeHighlighter(String code, String language) {
        super("<pre><code class=\"language-" + language + "\">" +
              escapeHtml(code) + "</code></pre>");
        getElement().executeJs("Prism.highlightElement($0)", getElement());
    }

    private static String escapeHtml(String html) {
        return html.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#039;");
    }
}
