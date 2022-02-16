grammar THATLANG;

compilation_unit : program+;

program: R NAME indented_statement+ R;

indented_statement: '\n    ' statement;

statement: usual_stmt | multi_stmt;

usual_stmt: NAME member_call;
multi_stmt: NAME multi_stmt_frag+;

multi_stmt_frag: Ss '.' NAME loose_arguments+;

member_call: member_access arguments?;
member_access: '.' NAME;

arguments: loose_arguments | tight_arguments;

loose_arguments: '[' NAME (Ss NAME)* ']';
tight_arguments: '(' NAME (LIST_SEP NAME)* ')';

LIST_SEP: Ss? ',' Ss?;

NAME : [a-zA-Z0-9_$]+;

R: (N|S)+;
Ns: N+;
N: [\n\r];
Ss: S+;
S: [ \t];
//WS : [ \t\r\n]+ -> skip ;
