Grammar of language to parse (Simplified C ENBF)

Simplified version derived from https://cs.wmich.edu/~gupta/teaching/cs4850/sumII06/The%20syntax%20of%20C%20in%20Backus-Naur%20form.htm




<program> -> VOID IDENTIFIER ‘(‘ ‘)’ <block>
<block> -> ‘{‘ { <statement> ; } ‘}’

<statement> -> <switch> | <for> | <while> | <dowhile> | <block> | <if> | <assignment> 
                         | <return>

<switch> -> switch ( <expression> ) <statement>

<if> -> if (<conditional_expression>) <block> [else <block>]*

<for> -> ( <assignment_expression> ; <conditional_expression> ; <postfix_expression>
               ) <block>

<while> -> while ( <conditional_expression> ) <block>

<dowhile> -> do <block> while ( condtional_expression )

<return> -> return [primary_expression] ;

<assignment> -> <assignment_expression>

<expression> -> <primary_expression> | <conditional_expression> 
                         | <postfix_expression> | <assignment_expression>


<primary_expression> -> ; | IDENTIFIER | INT | FLOAT | (<primary_expression> <unary_operator>)*


<conditional_expression> -> ; | <primary_expression> <conditional_operator> <primary_expression>


<assignment_expression> -> IDENTIFIER <assignment_operator> <primary_expression> ;

<postfix_expression> -> IDENTIFIER ++ | INDENTIFIER - -


<conditional_operator> -> == | <= | >= | != | < | >




<assignment-operator> -> =
                        | *=
                        | /=
                        | %=
                        | +=
                        | -=
                    

<unary-operator> -> &
                   | *
                   | +
                   | -



INT => [0-9] [INT]
FLOAT = [INT] . [INT]   
IDENTIFIER = !KEYWORD && ([a-z]|[A-Z])+ (0-9)*


Variables are created without a type in the structure:
IDENTIFIER = FLOAT | INT

Variables are operated upon using either an <assignment_expression> |  <postfix_expression>
They can also be compared using a <conditional_expression>
