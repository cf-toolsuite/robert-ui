package org.cftoolsuite.ui.component;

import com.vaadin.flow.component.dependency.JavaScript;

@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-yaml.min.js")
class YamlCodeHighlighter extends CodeHighlighter {
    YamlCodeHighlighter(String code) {
        super(code, "yaml");
    }

}
