package testsmell;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.MethodDeclaration;

public class Util {

    public static boolean isValidTestMethod(MethodDeclaration n) {
        boolean valid = false;

        if (!n.getAnnotationByName("Ignore").isPresent()) {
            if (n.getAnnotationByName("Test").isPresent() || n.getNameAsString().toLowerCase().startsWith("test")) {
            	
                if (!n.getModifiers().contains(Modifier.PRIVATE)) {
                    valid = true;
                }
            }
        }

        return valid;
    }

}
