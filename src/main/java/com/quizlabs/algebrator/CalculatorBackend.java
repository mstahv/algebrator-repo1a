/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.quizlabs.algebrator;

/**
 *
 * @author quizz
 */
// Calculator Back-End (Model)
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.core.interfaces.IExpr;
import org.springframework.stereotype.Component;


@Component
public class CalculatorBackend {

    private EvalUtilities evaluator;

    public CalculatorBackend() {
        // Initialize evaluator for solving expressions
        evaluator = new EvalUtilities(false, true);
    }

    // Solve a problem containing one or more equations or arithmetic expressions
    public String solveProblem(String problem) {
        try {
            String scrubbedProblem = translateRoots(problem);
            
            if (!scrubbedProblem.contains("=")) {
                // Handle arithmetic expressions
                IExpr result = evaluator.evaluate(scrubbedProblem);
                return result.toString();
            }

            // Handle algebraic equations
            StringBuilder solveInput = new StringBuilder("Solve[{");

            solveInput.append(scrubbedProblem);
            solveInput.append("}, {}]");

            // Solve equations
            IExpr result = evaluator.evaluate(solveInput.toString());
            return "Solutions: " + result.toString();
        } catch (Exception e) {
            return "Error solving problem: " + e.getMessage();
        }
    }

    private String translateRoots(String problem) {
        // Regex to find root expressions (e.g., 27√3, n√m)
        String rootPattern = "(\\w+)√(\\w+)";
        Pattern pattern = Pattern.compile(rootPattern);
        Matcher matcher = pattern.matcher(problem);

        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            // Base (before √)
            String base = matcher.group(1);
            // Root (after √)
            String root = matcher.group(2);

            // Translate to Matheclipse-compatible syntax
            String replacement = base + "^(1/" + root + ")";
            matcher.appendReplacement(result, replacement);
        }

        matcher.appendTail(result);
        return result.toString();
    }

    public static void main(String[] args) {
        CalculatorBackend backend = new CalculatorBackend();

        // Test cases
        System.out.println(backend.solveProblem("1+4")); // Arithmetic
        System.out.println(backend.solveProblem("n==m+1,m*n==56,m>0")); // Algebraic
        System.out.println(backend.solveProblem("((3/7)*n)*n==84,n>0")); // Quadratic-like
        System.out.println(backend.solveProblem("27√3 + 81√2"));
    }
}
