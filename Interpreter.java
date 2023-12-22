
import java.util.*;

public class Interpreter
{
    
    // Token getter Outputs a list of tokens
    private static List<String> getTokens(String expression)
    {
        List<String> result = new ArrayList<String>();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < expression.length(); i++){
            char c = expression.charAt(i);
            if (c == '+' || c == '-' || c == '*' || c == '(' || c == ')'){
                if (buffer.length() != 0) {
                    result.add(buffer.toString());
                    buffer = new StringBuffer();
                }
                result.add(c +"");
            } else {
                buffer.append(c);
            }
        }
        if (buffer.length() != 0) {
            result.add(buffer.toString());
        }
        return result;
    }

    
    // The interpret Function call
    private static int Interpret(String expression, Map<String, Integer> vars)
    {
        List<String> tokens = getTokens(expression);
        return InterpretExp(tokens, vars);
    }
    
    //Exp + Term | Exp - Term | Term
    private static int InterpretExp(List<String> tokens, Map<String, Integer> vars)
    {
        int result = InterpretTerm(tokens, vars);
        if (result == Integer.MIN_VALUE) {
            return result;
        }
        while (tokens.size() != 0) {
            String op = tokens.remove(0);
            if (!op.equals("+") && !op.equals("-")) {
                tokens.add(0, op);
                return result;
            }
            int value = InterpretTerm(tokens, vars);
            if (value == Integer.MIN_VALUE) {
                return value;
            }
            if (op.equals("+")) {
                result += value;
            } else {
                result -= value;
            }
        }
        return result;
    }
    //Term * Fact  | Fact
    private static int InterpretTerm(List<String> tokens, Map<String, Integer> vars)
    {
        int result = interpretFact(tokens, vars);
        if (result == Integer.MIN_VALUE) {
            return result;
        }
        while (tokens.size() != 0) {
            String op = tokens.remove(0);
            if (!op.equals("*")) {
                tokens.add(0, op);
                return result;
            }
            int value = interpretFact(tokens, vars);
            if (value == Integer.MIN_VALUE) {
                return value;
            }
            result *= value;
        }
        return result;
    }
    //( Exp ) | - Fact | + Fact | Literal | Identifier
    private static int interpretFact(List<String> tokens, Map<String, Integer> vars) {
        String next = tokens.remove(0);
        if (next.equals("(")) {
            int result = InterpretExp(tokens, vars);
            if (result == Integer.MIN_VALUE) {
                return result;
            }
            if (tokens.size() == 0 || !tokens.remove(0).equals(")")){
                return Integer.MIN_VALUE;
            }
            return result;
        } else if (next.equals("+") || next.equals("-")){
            int value = interpretFact(tokens, vars);
            if (value == Integer.MIN_VALUE){
                return value;
            }
            if (next.equals("+")){
                return value;
            } else{
                return -value;
            }
        } else {
            if (isIntegervalid(next)){
                return Integer.parseInt(next);
            } else if (vars.containsKey(next)){
                return vars.get(next);
            } else {
                return Integer.MIN_VALUE;
            }
        }
    }
    
    

    private static boolean isVarvalid(String var)
    {
        if (var.length() == 0){
            return false;
        }
        if (!Character.isLetter(var.charAt(0)) && var.charAt(0) != '_'){
            return false;
        }
        for (int i = 1; i < var.length(); i++){
            if (!Character.isLetterOrDigit(var.charAt(i)) && var.charAt(i) != '_'){
                return false;
            }
        }
        return true;
    }
    private static boolean isIntegervalid(String s)
    {
        if (s.charAt(0) == '0' && s.length() > 1){
            return false;
        }
        for (int i = 0; i < s.length(); i++){
            if (!Character.isDigit(s.charAt(i))){
                return false;
            }
        }
        return true;
    }
    public static void main(String[] args)
    {
        //Test case using String 
        String input = "x = 1;y = 2; z = (x+y)";
        String[] lines = input.split(";");
        Map<String, Integer> vars = new HashMap<String, Integer>();
        for (String line : lines) {
            String[] assignment = line.split("=");
            if (assignment.length != 2) {
                System.out.println("error");
                return;
            }
            String var = assignment[0].trim();
            String expression = assignment[1].trim();
            if (!isVarvalid(var)) {
                System.out.println("error");
                return;
            }
            int value = Interpret(expression, vars);
            if (value == Integer.MIN_VALUE) {
                System.out.println("error");
                return;
            }
            vars.put(var, value);
        }
        for (Map.Entry<String, Integer> entry : vars.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
    }
}