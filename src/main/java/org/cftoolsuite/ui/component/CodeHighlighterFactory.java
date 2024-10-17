package org.cftoolsuite.ui.component;

public class CodeHighlighterFactory {

    public static CodeHighlighter createHighlighter(String code, String language) {
        switch (language.toLowerCase()) {
            case "aspnet":
                return new AspNetCodeHighlighter(code);
            case "c":
                return new CCodeHighlighter(code);
            case "cpp":
                return new CppCodeHighlighter(code);
            case "csharp":
                return new CsharpCodeHighlighter(code);
            case "dart":
                return new DartCodeHighlighter(code);
            case "erlang":
                return new ErlangCodeHighlighter(code);
            case "fsharp":
                return new FsharpCodeHighlighter(code);
            case "fortran":
                return new FortranCodeHighlighter(code);
            case "go":
                return new GoCodeHighlighter(code);
            case "groovy":
                return new GroovyCodeHighlighter(code);
            case "haskell":
                return new HaskellCodeHighlighter(code);
            case "java":
                return new JavaCodeHighlighter(code);
            case "javascript":
                return new JavaScriptCodeHighlighter(code);
            case "julia":
                return new JuliaCodeHighlighter(code);
            case "kotlin":
                return new KotlinCodeHighlighter(code);
            case "markdown":
                return new MarkdownCodeHighlighter(code);
            case "matlab":
                return new MatlabCodeHighlighter(code);
            case "objective-c":
                return new ObjectiveCCodeHighlighter(code);
            case "pascal":
                return new PascalCodeHighlighter(code);
            case "perl":
                return new PerlCodeHighlighter(code);
            case "php":
                return new PhpCodeHighlighter(code);
            case "python":
                return new PythonCodeHighlighter(code);
            case "r":
                return new RCodeHighlighter(code);
            case "ruby":
                return new RubyCodeHighlighter(code);
            case "rust":
                return new RustCodeHighlighter(code);
            case "scala":
                return new ScalaCodeHighlighter(code);
            case "sql":
                return new SqlCodeHighlighter(code);
            case "swift":
                return new SwiftCodeHighlighter(code);
            case "typescript":
                return new TypeScriptCodeHighlighter(code);
            case "vbnet":
                return new VbNetCodeHighlighter(code);
            case "yaml":
                return new YamlCodeHighlighter(code);
            default:
                throw new IllegalArgumentException("Unsupported language: " + language);
        }
    }
}