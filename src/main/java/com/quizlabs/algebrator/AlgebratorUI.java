/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.quizlabs.algebrator;

/**
 *
 * @author quizz
 */
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.server.PWA;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Route("")
//@PWA(
//    name = "The Algebrator",
//    shortName = "Algebrator",
//    description = "A handy algebraic calculator PWA",
//    enableInstallPrompt = true
//)
public class AlgebratorUI extends VerticalLayout {

    private final TextArea display;
    private final CalculatorBackend backend;
    private boolean solved = true;
    private final String[] operators = {"+", "-", "*", "/", "^", "√", "<", "<=", ">=", ">", "!=", "=="};
    private final List<String> history = new ArrayList<>();
    private final String HISTORY_SEPARATOR = ";";

    @Autowired
    public AlgebratorUI(CalculatorBackend backend) {
        this.backend = backend;
        // Set the layout's width and alignment
        setWidth("25%");
        setHeight("100vh"); // Full viewport height
        setDefaultHorizontalComponentAlignment(Alignment.START);

        // Add CSS class for styling the calculator
        addClassName("calculator-container");

        // Display Area
        display = new TextArea("The Algebrator");
        display.setWidthFull();
        display.setHeight("150px");
        display.setValue("0");
        display.setReadOnly(true);
        add(display);

        // Button Sections
        createAlgebraButtons();
        createMathButtons();
    }

    private void createMathButtons() {

        String[][] mathButtons = {{"(", "7", "8", "9", "*"}, {")", "4", "5", "6", "-"}, {"", "1", "2", "3", "+"}, {"", "+/-", "0", ".", "Solve"}};
        createButtonBlock(mathButtons);
    }

    private void createAlgebraButtons() {

        String[] topButtons = {"", "", "H", "←", "C"};
        add(createButtonRow(topButtons));

        String[][] variableButtons = {{"a", "b", "c", "d", "e"}, {"f", "g", "h", "i", "j"},
        {"k", "l", "m", "n", "o"}, {"p", "q", "r", "s", "t"},
        {"u", "v", "w", "x", "y"}};
        createButtonBlock(variableButtons, true);

        String[][] equationButtons = {{"z", "^2", "2√", "^3", "3√"}, {"<", ">", "!=", "^", "√"}, {"<=", ">=", "==", ",", "/"}};
        createButtonBlock(equationButtons, true);

    }

    private void createButtonBlock(String[][] buttons) {
        createButtonBlock(buttons, false);
    }

    private void createButtonBlock(String[][] buttons, boolean narrowButtons) {
        for (String[] row : buttons) {
            HorizontalLayout eachRow = createButtonRow(row, narrowButtons);
            add(eachRow);
        }
    }

    private HorizontalLayout createButtonRow(String[] labels) {
        return createButtonRow(labels, false);
    }

    private HorizontalLayout createButtonRow(String[] labels, boolean narrowButtons) {
        HorizontalLayout row = new HorizontalLayout();
        for (String label : labels) {
            Button newButton = createButton(label);
            if (!narrowButtons) {
                //Optional To do:  Find a way to narrow buttons which actually works
            }
            row.add(newButton);
        }
        return row;
    }

    private Button createButton(String buttonText) {
        Button button = new Button(buttonText);
        if (!buttonText.isEmpty()) {
            button.addClickListener(e -> handleButtonPress(buttonText));
        } else {
            button.setEnabled(false);
        }
        addButtonToolTip(button);
        return button;
    }

    private void addButtonToolTip(Button button) {
        // Set tool tip text based on the label
        String buttonText = button.getText();
        switch (buttonText) {
            case "H" ->
                button.getElement().setAttribute("title", "History");
            case "Solve" ->
                button.getElement().setAttribute("title", "Solve or Calculate");
            case "," ->
                button.getElement().setAttribute("title", "Separate (In-)Equations");
            case "←" ->
                button.getElement().setAttribute("title", "Backspace");
            case "C" ->
                button.getElement().setAttribute("title", "Clear");
            case "+/-" ->
                button.getElement().setAttribute("title", "Negation");
            case "0" ->
                button.getElement().setAttribute("title", "Zero");
            case "." ->
                button.getElement().setAttribute("title", "Decimal Point");
            case "==" ->
                button.getElement().setAttribute("title", "Equals");
            case "!=" ->
                button.getElement().setAttribute("title", "Not Equals");
            case "<" ->
                button.getElement().setAttribute("title", "Less Than");
            case "<=" ->
                button.getElement().setAttribute("title", "Less Than or Equals");
            case ">" ->
                button.getElement().setAttribute("title", "Greater Than");
            case ">=" ->
                button.getElement().setAttribute("title", "Greater Than or Equals");
            case "+" ->
                button.getElement().setAttribute("title", "Plus");
            case "-" ->
                button.getElement().setAttribute("title", "Minus");
            case "*" ->
                button.getElement().setAttribute("title", "Times");
            case "/" ->
                button.getElement().setAttribute("title", "Divided by");
            case "2√" ->
                button.getElement().setAttribute("title", "Square Root");
            case "3√" ->
                button.getElement().setAttribute("title", "Cube Root");
            case "√" ->
                button.getElement().setAttribute("title", "Root");
            case "^2" ->
                button.getElement().setAttribute("title", "Square");
            case "^3" ->
                button.getElement().setAttribute("title", "Cube");
            case "^" ->
                button.getElement().setAttribute("title", "Power");
            default ->
                button.getElement().setAttribute("title", buttonText); // Default tooltip shows the button's label
        }

    }

    
    private void addToHistory(String problem, String result) {
        history.add(problem + HISTORY_SEPARATOR + result);
    }

private void showHistory() {
    Dialog historyDialog = new Dialog();
    historyDialog.setWidth("400px");
    historyDialog.setHeight("300px");

    VerticalLayout historyLayout = new VerticalLayout();
    historyLayout.setPadding(true);
    historyLayout.setSpacing(false); // Minimize default spacing between items
    historyLayout.setWidthFull();
    historyLayout.addClassName("history-container"); // Add a class for custom CSS

    // Populate history
    for (String entry : history) {
        // Split problem and solution
        String[] parts = entry.split(HISTORY_SEPARATOR, 2);
        String problem = parts[0];
        String solution = parts.length > 1 ? parts[1] : "No solution";

        // Group problem and solution in a container
        Div group = new Div();

        Div problemDiv = new Div();
        problemDiv.setText("Entered: " + problem);

        Div solutionDiv = new Div();
        if(!solution.contains("Solutions")) {
            solution = "Result:  " + solution;
        }
        
        solutionDiv.setText(solution);

        Div spacerDiv = new Div();
        spacerDiv.setText("---------------------------------");
        
        group.add(problemDiv, solutionDiv, spacerDiv);
        historyLayout.add(group);
    }

    historyDialog.add(historyLayout);
    historyDialog.open();
}

    private void handleButtonPress(String label) {
        String displayText = display.getValue();

        switch (label) {
            case "C" -> {
                display.setValue("0");
                solved = true;
            }
            case "H" -> {
                showHistory();
            }
            case "+/-" -> {
                display.setValue(negateLastValue(displayText));
            }
            case "Solve" -> {
                if (!solved && displayHasOps()) {
                    String result = backend.solveProblem(displayText);
                    result = extractBoolean(result);
                    if (result.isEmpty()) {
                        result = "No results";
                    }
                    display.setValue(result);
                    addToHistory(displayText, result);
                    solved = true;
                } else {
                    // Optionally, notify the user that solve doesn't apply yet
                    Notification.show("Enter at least one expression before clicking Solve.");
                    solved = false;
                }
            }
            case "←" -> {
                if (!displayText.isEmpty()) {
                    display.setValue(
                            displayText.length() > 1
                            ? displayText.substring(0, displayText.length() - 1)
                            : "0"
                    );
                }
            }
            case "," -> {
                if (canAppendComma(displayText)) {
                    updateDisplay(label);
                } else {
                    // Optionally, notify the user that a comma can't be appended
                    Notification.show("Complete the current equation or inequation before adding a comma.");
                }

            }
            case "2√", "3√" ->
                updateDisplay(reverseText(label));
            default ->
                updateDisplay(label);
        }
    }

    private String reverseText(String textToReverse) {
        return new StringBuilder(textToReverse).reverse().toString();
    }

    private void updateDisplay(String buttonText) {
        String displayText = display.getValue();
        boolean isVariable = buttonText.length() == 1 && Character.isLowerCase(buttonText.charAt(0)) && Character.isLetter(buttonText.charAt(0));

        if (solved) {
            if (buttonText.matches("[0-9]") || buttonText.equals(".") || buttonText.equals("(") || isVariable) {
                display.setValue(buttonText);
                solved = false;
            } else {
                if (!solutionText(displayText)) {
                    preventEndlessOps(buttonText);
                    if (displayText.endsWith(buttonText)) {
                        solved = false;
                    }
                }
            }
        } else {
            if (solutionText(displayText)) {
                display.setValue(buttonText);
            } else {
                preventEndlessOps(buttonText);
            }
        }
    }

    private String negateLastValue(String displayText) {
        // Regex to find the last number or variable
        String regex = "(.*?)([\\d.]+|[a-zA-Z]+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(displayText);

        if (matcher.find()) {
            String prefix = matcher.group(1); // Everything before the last value
            String lastValue = matcher.group(2); // The last number or variable

            if (prefix.endsWith("-")) {
                // Remove negation
                prefix = prefix.substring(0, prefix.length() - 1);
            } else {
                // Add negation
                prefix = prefix + "-";
            }

            return prefix + lastValue; // Reconstruct the display
        }

        // If no match, return the original text
        return displayText;
    }

    private void preventEndlessOps(String label) {
        String displayText = display.getValue();

        if (!buttonIsOperator(label) || !displayEndsWithOperator()) {
            display.setValue(displayText + label);
        }
    }

    private boolean canAppendComma(String currentText) {
        // Regex to match valid equations or inequations, also works for logarithm and trig functions.
        String regex = ".*?(\\w+|\\d+|\\([^()]+\\))([=<>!]=?|<=|>=)([\\w\\d√*+/^()-]+|\\w+\\([^()]+\\))$";
        return currentText.matches(regex);
    }

    private boolean buttonIsOperator(String label) {
        for (String operator : operators) {
            if (label.equals(operator)) {
                return true;
            }
        }
        return false;
    }

    private boolean displayHasOps() {
        String displayText = display.getValue();
        for (String operator : operators) {
            if (displayText.contains(operator)) {
                return true;
            }
        }
        return false;
    }

    private boolean displayEndsWithOperator() {
        String displayText = display.getValue();
        for (String operator : operators) {
            if (displayText.endsWith(operator)) {
                return true;
            }
        }
        return false;
    }

    private boolean solutionText(String displayText) {
        return displayText.contains("Solutions");
    }

    private String extractBoolean(String backendResult) {

        if (backendResult.contains("Solve")) {
            // Regex to extract content between the first pair of curly braces
            String regex = "\\{([^}]*)\\}";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(backendResult);

            if (matcher.find()) {
                return matcher.group(1); // Extract the content inside the first braces
            }
        }

        // If no match, return the original result
        return backendResult;
    }
}
