
public class Tokenizer {

    private static int currentPosition = 0;

    // Creates new token
    private static Token createToken(Tokentypes type, String value) {
        return new Token(type, value);
    }

    // checks for whitespace characters
    private static boolean isSpace(char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }

    // Function to skip whitespace characters
    private static void skipspace(String input) {
        while (currentPosition < input.length() && isSpace(input.charAt(currentPosition))) {
            currentPosition++;
        }
    }

    
    private static Token getNextToken(String input) {
        skipspace(input);

        if (currentPosition >= input.length()) {
            return createToken(Tokentypes.END_OF_INPUT, null);
        }

        if (Character.isDigit(input.charAt(currentPosition))) {
            int start = currentPosition;
            while (currentPosition < input.length() && Character.isDigit(input.charAt(currentPosition))) {
                currentPosition++;
            }
            return createToken(Tokentypes.INTEGER, input.substring(start, currentPosition));
        }

        if (input.charAt(currentPosition) == '+' || input.charAt(currentPosition) == '-' ||
            input.charAt(currentPosition) == '*' || input.charAt(currentPosition) == '/' ||
            input.charAt(currentPosition) == '=') {
            String value = Character.toString(input.charAt(currentPosition));
            currentPosition++;
            return createToken(Tokentypes.OPERATOR, value);
        }

        if (input.charAt(currentPosition) == '(') {
            String value = Character.toString(input.charAt(currentPosition));
            currentPosition++;
            return createToken(Tokentypes.LPAREN, value);
        }

        if (input.charAt(currentPosition) == ')') {
            String value = Character.toString(input.charAt(currentPosition));
            currentPosition++;
            return createToken(Tokentypes.RPAREN, value);
        }

        if (Character.isLetter(input.charAt(currentPosition))) {
            int start = currentPosition;
            while (currentPosition < input.length() && (Character.isLetterOrDigit(input.charAt(currentPosition)))) {
                currentPosition++;
            }
            return createToken(Tokentypes.IDENTIFIER, input.substring(start, currentPosition));
        }

        System.err.println("Error: Invalid character in input");
        System.exit(1);
        return null; 
    }

    public static void main(String[] args) {
        
        String input = "x = 001";

        Token token;
        do {
            token = getNextToken(input);
            switch (token.types) {
                case INTEGER:
                    System.out.println("Integer: " + token.value);
                    break;
                case OPERATOR:
                    System.out.println("Operator: " + token.value);
                    break;
                case IDENTIFIER:
                    System.out.println("Identifier: " + token.value);
                    break;
                case LPAREN:
                    System.out.println("Left Parenthesis");
                    break;
                case RPAREN:
                    System.out.println("Right Parenthesis");
                    break;
                case END_OF_INPUT:
                    System.out.println("End of Input");
                    break;
            }
        } while (token.types != Tokentypes.END_OF_INPUT);
    }
}