package com.yourorganization.maven_sample;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.typesystem.Type;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import java.io.IOException;

import static com.github.javaparser.JavaParser.parse;

/**
 * Some code that uses JavaSymbolSolver.
 */
public class MyAnalysis {
    public static void main(String[] args) throws IOException {
        // Set up a minimal type solver that only looks at the classes used to run this sample.
        CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
        combinedTypeSolver.add(new ReflectionTypeSolver());

        // Parse some code
        CompilationUnit cu = parse("class X { int x() { return 1 + 1.0; } }");

        // Find the expression 1+1.0
        ReturnStmt returnStatement = (ReturnStmt) cu
                .getClassByName("X").get()
                .getMethods().get(0)
                .getBody().get().getStatements().get(0);
        Expression onePlusOne = returnStatement.getExpression().get();

        // Find out what type the result of 1+1.0 has.
        Type typeOfOnePlusOne = JavaParserFacade.get(combinedTypeSolver).getType(onePlusOne);

        // Show that it's "double"
        System.out.println(typeOfOnePlusOne);
    }
}
