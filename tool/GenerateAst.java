package tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: GenerateAst <output directory>");
            System.exit(64);
        }
        String outputDir = args[0];
        defineAst(outputDir, Arrays.asList(
                "Binary   : Expr left, Token operator, Expr right",
                "Grouping : Expr expression",
                "Literal  : Object value",
                "Unary    : Token operator, Expr right"
        ));
    }

    private static void defineAst(String outputDir, List<String> types) throws IOException {
        String path = Paths.get(outputDir, "Expr.java").toString();
        try (PrintWriter writer = new PrintWriter(path, StandardCharsets.UTF_8)) {
            writer.println("package tool;");
            writer.println();
            writer.println("import java.util.List;");
            writer.println("import fox.Token;");
            writer.println();
            writer.println("abstract class Expr {");

            // Generate subclasses for the AST types.
            for (String type : types) {
                String className = type.split(":")[0].trim();
                String fields = type.split(":")[1].trim();
                defineType(writer, className, fields);
            }

            writer.println("}");
        }
    }

    private static void defineType(PrintWriter writer, String className, String fieldList) {
        writer.println("  static class " + className + " extends Expr {");

        // Constructor
        writer.println("    " + className + "(" + fieldList + ") {");
        String[] fields = fieldList.split(", ");
        for (String field : fields) {
            String name = field.split(" ")[1];
            writer.println("      this." + name + " = " + name + ";");
        }
        writer.println("    }");

        // Fields
        writer.println();
        for (String field : fields) {
            writer.println("    final " + field + ";");
        }

        writer.println("  }");
    }
}
