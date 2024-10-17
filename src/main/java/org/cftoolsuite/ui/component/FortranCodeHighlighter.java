package org.cftoolsuite.ui.component;

import com.vaadin.flow.component.dependency.JavaScript;

@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-fortran.min.js")
class FortranCodeHighlighter extends CodeHighlighter {
    FortranCodeHighlighter(String code) {
        super(code, "fortran");
    }

}
