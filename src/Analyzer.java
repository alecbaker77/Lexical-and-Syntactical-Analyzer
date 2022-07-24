import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class Analyzer {



    public static final Map<String, String> keyWords = new HashMap<String, String>() {{
        put("switch", "SWITCH");
        put("foreach", "FOREACH");
        put("for", "FOR");
        put("while", "WHILE");
        put("do", "DO");
        put("if", "IF");
        put("else", "ELSE");
        put("return", "RETURN");
        put("const", "CONST");
        put("int", "INT");
        put("double", "DOUBLE");
        put("float", "FLOAT");
        put("public", "PUBLIC");
        put("void", "VOID");
        put("default", "DEFAULT");
        put("case", "CASE");

    }};

    public static final Map<String, String> specialSymbols = new HashMap<String, String>() {{
        put("+", "ADD");
        put("-", "SUB");
        put("*", "MUL");
        put("/", "DIV");
        put("%", "MOD");
        put("=", "EQUAL");
        put("+=", "ADD_EQUAL");
        put("-=", "SUB_EQUAL");
        put("*=", "MUL_EQUAL");
        put("/=", "DIV_EQUAL");
        put("%=", "MOD_EQUAL");
        put("==", "EQUAL_TO");
        put(">", "GREATER");
        put("<", "LESS");
        put("!=", "NOT_EQUAL");
        put(">=", "GREATER_EQUAL");
        put("<=", "LESS_EQUAL");
        put("&&", "AND");
        put("||", "OR");
        put("!", "NOT");
        put("(", "LEFT_PAREN");
        put(")", "RIGHT_PAREN");
        put("{", "LEFT_BRACE");
        put("}", "RIGHT_BRACE");
        put("[", "LEFT_BRACKET");
        put("]", "RIGHT_BRACKET");
        put(";", "SEMI_COLON");
        put("&", "UNARY_AND");
        put("++", "INC");
        put("--", "DEC");

    }};






    public static int lineNum = 1;


    public static void main(String args[]) {
        File program = new File("program.txt");
        parse(program);
    }

    public static Boolean parse(File program){
        try {
            Scanner reader = new Scanner(program);

            while(reader.hasNextLine()){
                String line = reader.nextLine();
                lex(line);
                lineNum++;
            }

            System.out.println("LEXICAL ANALYSIS COMPLETE WITHOUT ERROR");
            tokenItr  = tokenStrings.listIterator();
            boolean success = syntax();

            if (success){
                System.out.println("SYNTACTICAL ANALYSIS COMPLETE WITHOUT ERROR");
            } else {
                System.out.println("SYNTACTICAL ANALYSIS FAILED");
            }

        } catch (FileNotFoundException e){
            System.out.println("An Error Occured");
            return false;
        }
        return true;

    }

    public static ArrayList<String> tokenStrings = new ArrayList<String>();
    public static ListIterator<String> tokenItr;
    public static String TOKEN = "";

    public static void getNextToken(){
        if (tokenItr.hasNext()){
            TOKEN = tokenItr.next();
        } else {
            TOKEN = "EOF";
        }
    }

    public static void getPrevToken(){
        if (tokenItr.hasPrevious()){
            TOKEN = tokenItr.previous();
        }
    }

    public static Boolean syntax(){
        getNextToken();
        if(TOKEN == "VOID"){
            return program();
        } else {
            return false;
        }

    }

    public static Boolean program(){
        getNextToken();
        if(TOKEN == "IDENTIFIER"){
            getNextToken();
            if(TOKEN == "LEFT_PAREN"){
                getNextToken();
                if(TOKEN == "RIGHT_PAREN"){
                    return block();
                } else {
                    //error
                    return false;
                }
            } else {
                //error
                return false;
            }
        } else {
            //error
            return false;
        }
    }

    public static Boolean block(){
        getNextToken();
        if(TOKEN == "LEFT_BRACE"){
            while(TOKEN == "LEFT_BRACE"){
                getNextToken();
            }
            if(TOKEN != "RIGHT_BRACE"){
                statementList();
                if(TOKEN == "EOF"){
                    return true;
                }
                if(TOKEN == "RIGHT_BRACE"){
                    //("check for closing brace   TOKEN = "  + TOKEN);
                    while(TOKEN == "RIGHT_BRACE"){
                        getNextToken();
                    }
                    return true;
                } else {
                    return false;
                }
            } else {
                getNextToken();
                if(TOKEN == "RIGHT_BRACE"){
                    while(TOKEN == "RIGHT_BRACE"){
                        getNextToken();
                    }
                    return true;
                } else {
                    return false;
                }
            }

        } else {
            return false;
        }

    }


    public static Boolean statementList(){
        while(true){
            if(TOKEN == "EOF"){
                return true;
            }

            boolean success = statement();
            if(!success){

                break;
            }


            /*if(TOKEN != "SEMI_COLON"){
                return false;
            }*/

            if(TOKEN == "WHILE"){
                continue;
            }
            getNextToken();
            if (TOKEN == "RIGHT_BRACE"){

                return true;
            }

        }

        if(TOKEN != "SEMI_COLON"){
            return false;
        } else {
            return true;
        }

    }


    public static Boolean statement(){

        if (TOKEN == "IDENTIFIER"){
            return assignmentStatement();
        } else if (TOKEN == "FOR"){

            return forStatement();
        } else if (TOKEN == "WHILE"){
            return whileStatement();
        } else if (TOKEN == "DO") {
            return doWhileStatement();
        } else if (TOKEN == "IF"){
            return ifStatement();
        } else if (TOKEN == "BLOCK") {
            return block();
        }else if (TOKEN == "SWITCH"){
            return switchStatement();
        } else if (TOKEN == "RETURN"){
            return returnStatement();
        }

        return false;
    }

    public static Boolean doWhileStatement(){
        getNextToken();
        if(TOKEN == "LEFT_BRACK"){
            if(block()){
                if (TOKEN == "RIGHT_BRACK"){
                    getNextToken();
                    if (TOKEN == "LEFT_PAREN"){
                        if (conditionalExpression()){
                            getNextToken();
                            if(TOKEN == "RIGHT_PAREN"){
                                getNextToken();
                                if(TOKEN == "SEMI_COLON"){
                                    return true;
                                }else {
                                    return false;
                                }
                            } else {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static Boolean assignmentStatement(){
        getNextToken();
        if(assignmentOperator()){
            getNextToken();
            while(primaryExpression2()){

                getNextToken();
                if (TOKEN == "SEMI_COLON"){

                    return true;
                }

                if(!unaryOperator()){
                    return false;
                }

            }
        }
        return true;
    }


    public static Boolean returnStatement(){
        getNextToken();
        if(TOKEN == "IDENTIFIER"){
            getNextToken();
            if(TOKEN == "SEMI_COLON"){
                return true;
            } else {
                return false;
            }
        } else if (TOKEN == "SEMI_COLON"){
            return true;
        } else if (primaryExpression2()) {
            getNextToken();
            if(TOKEN == "SEMI_COLON"){
                return true;
            } else {
                return false;
            }
        }
        else{
            return false;
        }
    }

    public static Boolean forStatement(){
        getNextToken();
        if (TOKEN == "LEFT_PAREN"){
            getNextToken();
            if (!assignmentStatement()){
                return false;
            }
            if(TOKEN == "SEMI_COLON"){

                getNextToken();
                if (!conditionalExpression()){
                    return false;
                }

                if(TOKEN == "SEMI_COLON"){
                    getNextToken();
                    if (!postFixExpression()){
                        return false;
                    }
                    getNextToken();
                    if(TOKEN == "RIGHT_PAREN"){
                        //getNextToken();
                        return block();
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    public static boolean whileStatement(){

        getNextToken();
        if(TOKEN == "LEFT_PAREN"){
            getNextToken();
            if(conditionalExpression()){
                //getNextToken();
                if(TOKEN == "RIGHT_PAREN"){

                    getNextToken();
                    return block();
                } else {
                    return false;
                }
            } else {

                return false;
            }
        }  else {
            return false;
        }
    }

    public static Boolean ifStatement(){

        getNextToken();
        if(TOKEN == "LEFT_PAREN"){
            getNextToken();
            if(conditionalExpression()){
                //getNextToken();
                if(TOKEN == "RIGHT_PAREN"){

                    getNextToken();
                    return block();
                } else {
                    return false;
                }
            } else {

                return false;
            }
        }  else {
            return false;
        }
    }

    public static Boolean switchStatement(){

        getNextToken();
        if(TOKEN == "LEFT_PAREN"){
            getNextToken();
            if(conditionalExpression()){
                //getNextToken();
                if(TOKEN == "RIGHT_PAREN"){

                    getNextToken();
                    return block();
                } else {
                    return false;
                }
            } else {

                return false;
            }
        }  else {
            return false;
        }
    }


    public static Boolean conditionalExpression(){
        if(TOKEN == "SEMI_COLON"){
            return true;
        }
        if (TOKEN == "IDENTIFIER" || TOKEN == "INT" || TOKEN == "FLOAT"){
            while(primaryExpression2()){

                getNextToken();
                if (conditionalOperator()){

                    break;
                }

                if(!unaryOperator()){
                    return false;
                }

            }
            getNextToken();
            while(primaryExpression2()){
                getNextToken();
                if (TOKEN == "SEMI_COLON"){

                    break;
                }

                if(!unaryOperator()){
                    break;
                }

            }
        } else if (TOKEN == "LEFT_PAREN") {
            getNextToken();
            conditionalExpression();
            //getNextToken();
            if (TOKEN == "RIGHT_PAREN") {

                return true;
            } else {

                return false;
            }
        } else if (conditionalOperator()) {
            getNextToken();
            return true;

        } else {

            return false;
        }
        return true;
    }

    public static Boolean postFixExpression(){
        if(primaryExpression2()){
            getNextToken();
            if(TOKEN == "INC" || TOKEN == "DEC"){
                return true;
            } else {
                getNextToken();
                while(primaryExpression2()){

                    getNextToken();
                    if (TOKEN == "SEMI_COLON"){

                        return true;
                    }

                    if(!unaryOperator()){
                        return false;
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }

    public static Boolean primaryExpression2(){
        if(TOKEN == "SEMI_COLON"){
            return true;
        }
        if (TOKEN == "IDENTIFIER"){
            return true;
        } else if (TOKEN == "INT" || TOKEN == "FLOAT"){
            return true;
        } else if (TOKEN == "LEFT_PAREN") {
            getNextToken();
            primaryExpression2();
            getNextToken();
            if (TOKEN == "RIGHT_PAREN") {
                return true;
            } else {
                return false;
            }
        } else if (unaryOperator()) {
            getNextToken();
            return primaryExpression2();

        } else {
            return false;
        }
    }


    public static Boolean assignmentOperator(){
        if (TOKEN == "EQUAL" || TOKEN == "MUL_EQUAL" || TOKEN == "DIV_EQUAL" ||
                TOKEN == "MOD_EQUAL" || TOKEN == "ADD_EQUAL" || TOKEN == "SUB_EQUAL"){

            return true;
        } else {
            return false;
        }
    }

    public static Boolean unaryOperator(){
        if (TOKEN == "UNARY_AND" || TOKEN == "MUL" || TOKEN == "ADD" ||
                TOKEN == "SUB"){
            return true;
        } else {
            return false;
        }
    }

    public static Boolean conditionalOperator(){
        if (TOKEN == "EQUAL_TO" || TOKEN == "LESS_EQUAL" || TOKEN == "GREATER_EQUAL" ||
                TOKEN == "NOT_EQUAL" || TOKEN == "LESS" || TOKEN == "GREATER"){
            return true;
        } else {
            return false;
        }
    }




    public static Boolean lex (String line){
        //Map <String, String> charTypes = new LinkedHashMap<>();

        LinkedList<String[]> charTypes = new LinkedList<String[]>();
        String lexeme = "";

        //Split line into lexemes and assign character type
        for(int i=0; i < line.length(); i++){
            char currChar = line.charAt(i);
           // ("CURR CHAR =   " + currChar);
            if (currChar == ' ')
                continue;
            if (Character.isAlphabetic(currChar)){
                lexeme += currChar;
                for(int j = i+1; j < line.length(); j++){
                    char consecChar = line.charAt(j);
                    if (Character.isAlphabetic(consecChar)){
                        lexeme += consecChar;
                    } else {
                        //Word Found
                        //(lexeme + " -- WORD");
                        String[] group = new String[]{lexeme, "WORD"};
                        charTypes.addLast(group);
                        lexeme = "";
                        i = j-1;
                        break;
                    }
                }
            } else if (Character.isDigit(currChar)){
                lexeme += currChar;
                for(int j = i+1; j < line.length(); j++){
                    char consecChar = line.charAt(j);
                    if (Character.isDigit(consecChar)){
                        lexeme += consecChar;
                    } else {
                        //(lexeme + " -- NUMBER");
                        String[] group = new String[]{lexeme, "NUMBER"};
                        charTypes.addLast(group);
                        lexeme = "";
                        i = j-1;
                        break;
                    }
                }
            } else if (!Character.isAlphabetic(currChar) && !Character.isDigit(currChar)){
                //("CURR CHAR 2 =   " + currChar);
                if (currChar != ' ' && currChar != '\t' && currChar != '\r' && currChar != '\n') {
                    String symbolChain = ""+currChar;
                    for(int j = i+1; j < line.length(); j++){
                        char consecSymbol = line.charAt(j);
                        if (consecSymbol == ' ')
                            continue;
                        if (specialSymbols.containsKey(symbolChain + consecSymbol)){
                            symbolChain += consecSymbol;
                        } else {
                            lexeme = "";
                            i = j-1;
                            break;
                        }
                    }
                    String[] group = new String[]{symbolChain, "SYMBOL"};
                    charTypes.addLast(group);
                }
            }
        }

        //assign token to each lexeme and check for lexical errors

        LinkedList<String> tokenEntries = new LinkedList<String>();
        for(String[] entry: charTypes){
            Object key = entry[0];
            String value = entry[1];

            String token = "";

            if(value == "WORD"){
                token = parseWord(key.toString());


            } else if (value == "NUMBER"){
                token = parseNumber(key.toString());

            } else {
                token = parseSymbol(key.toString());

            }
            if (value == "INVALID"){
                System.out.println("Lexical Error on line " + lineNum + "  '"+key.toString()+"'");
                return false;
            }
            tokenStrings.add(token);
        }
        return true;
    }

    public static String parseWord(String word){
        //check if word is a keyword
        if (keyWords.containsKey(word)){
            return keyWords.get(word);
        } else {
            return "IDENTIFIER";
        }
    }

    public static String parseNumber(String number){
        //check if number is integer
        if (number.matches("-?\\d+")){
            return "INT";
        } else if (number.matches("/^[0-9]+(\\\\.[0-9]+)?$")) {
            return "FLOAT";
        } else {
            return "INVALID";
        }
    }

    public static
    String parseSymbol(String symbol){
        //check if word is a keyword
        if (specialSymbols.containsKey(symbol)){
            return specialSymbols.get(symbol);
        } else {
            return "INVALID";
        }
    }

}




