import java.io.*;
// hashmap abbina ID e "valore" all'interno della hashmap 
// es. iload x -> se hashmap ha dato val 0 a x -> iload 0 
public class Translator {
	private Lexer lex;
	private BufferedReader pbr;
	private Token look;
	
	SymbolTable st = new SymbolTable();
	CodeGenerator code = new CodeGenerator();
	int count = 0;
	
	public Translator(Lexer l, BufferedReader br) {
		lex = l;
		pbr = br;
		move();
	}
	
	void move() {
		look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
	}
	
	void error(String s) {
		throw new Error("near line " + lex.line + ": " + s);
	}
	
	void match(int t) {
		if (look.tag == t) {
    	    if (look.tag != Tag.EOF) 
			move();
    	} else 
			error("syntax error");
	}
	public void prog() {
		//GUIDA ( <prog> ::= <statlist> EOF ) = { assign, print, read, while, if, { } 
		if( look.tag == Tag.ASSIGN ||  look.tag ==  Tag.PRINT ||  look.tag ==  Tag.READ ||  
			look.tag == Tag.WHILE ||  look.tag == Tag.IF || look.tag == '{'){
				int lnext_prog = code.newLabel();
				statlist(lnext_prog);
				code.emitLabel(lnext_prog);
				match(Tag.EOF);
				try {
					code.toJasmin();
				}
				catch(java.io.IOException e) {
					System.out.println("IO error\n");
				}
			} else {
				error("syntax error - prog()");
		}
	}
	
	public void statlist(int label) { 
		//GUIDA ( <statlist> ::= <stat> <statlistp> ) = { assign, print, read, while, if, { }
		if( look.tag == Tag.ASSIGN ||  look.tag ==  Tag.PRINT ||  look.tag ==  Tag.READ ||  
			look.tag == Tag.WHILE ||  look.tag == Tag.IF || look.tag == '{'){
				stat();
				statlistp();
			} else {
				error("syntax error - statlist()");
		}
	}
	
	public void statlistp() {
		switch(look.tag){
			//GUIDA ( <statlistp> ::= ; <stat> <statlistp> ) = { ; }
			case ';':
				match(';');
				stat();
				statlistp();
				break;
			//GUIDA ( <statlistp> ::= ε ) = { EOF, } }
			case Tag.EOF:
				break;
			case '}':
				break;
			default:
				error("syntax error statlistp()");
				break;
		}
	}
	
	public void stat() {
		switch(look.tag) {
			//GUIDA ( <stat> ::= assign <expr> to <idlist> ) = { assign }
			case Tag.ASSIGN :
				match(Tag.ASSIGN);
				int v = expr();
				match(Tag.TO);
				idlist('a', v); // flag: idlist vuole parametro ma per assign il flag non serve
				break;
			//GUIDA ( <stat> ::= print ( <exprlist> ) ) = { print }
			case Tag.PRINT:
				match(Tag.PRINT);
				match('(');
				exprlist('p');
				match(')');
				break;
			//GUIDA ( <stat> ::= read ( <idlist> ) ) = { read }
			case Tag.READ:
				match(Tag.READ);
				match('(');
				idlist('r', -1); // flag dell'if 
				match(')');
				break;
			//GUIDA ( <stat> ::= while ( <bexpr> ) <stat> ) = { while }
			case Tag.WHILE:
			//3 label: true, false e loop
				int label_true = code.newLabel();
				int label_false = code.newLabel();
				int label_loop = code.newLabel(); // usata subito // label della condizione per il loop
				code.emitLabel(label_loop);
				match(Tag.WHILE);
				match('(');
				bexpr(label_true); // controlla che la sintassi della condizione sia corretta
				match(')');
				code.emit(OpCode.GOto, label_false); //condizione del while non rispettata, fine di tutto il while, ti fa saltare alla riga prima dl break
				code.emitLabel(label_true); //condizione rispettata
				stat(); // nel caso while true 
				code.emit(OpCode.GOto, label_loop); // torna alla label del while per rifare il controllo sulla condizione
				code.emitLabel(label_false); 
				break;
			//GUIDA ( <stat> ::= if ( <bexpr> ) <stat> A ) = { if }
			case Tag.IF:
			//2 label: if fatto se true e non fatto se false
				int l_true = code.newLabel();
				int l_false = code.newLabel();
				match(Tag.IF);
				match('(');
				bexpr(l_true);
				match(')');
				code.emit(OpCode.GOto, l_false); 
				code.emitLabel(l_true); //condizione rispettata
				stat();
				A(l_false);
				break;
			//GUIDA ( <stat> ::= { <statlist> } ) = { { }	
			case '{':
				match('{');
				statlist(-1);
				match('}');
				break;
			default:
				error("syntax error - stat()");
				break;
		}
	}
	
	public void A(int label_false) {
		switch(look.tag) {
			//GUIDA ( A ::= end ) = { end }
			case Tag.END:
				match(Tag.END);
				code.emitLabel(label_false);
				break;
			//GUIDA ( A ::= else <stat> end ) = { else }
			case Tag.ELSE:
				int label_noelse = code.newLabel(); // etichetta per non eseguire l'else (perché si è entrati nell'if )
				match(Tag.ELSE);
				code.emit(OpCode.GOto, label_noelse); // se if fatto 
				code.emitLabel(label_false); // emissione label_false, parametro passato da stat()
				stat();
				match(Tag.END);
				code.emitLabel(label_noelse);
				break;
			default:
				error("syntax error - A()");
				break;
		}
	}
	
	public int idlist(char flag, int value) {
		int id_addr = 0; 
		switch(look.tag) {
			//GUIDA ( <idlist> ::= ID <idlistp> ) = { ID }
			case Tag.ID:
				//dove inserisce il valore 
				id_addr = st.lookupAddress(((Word)look).lexeme); // indirizzo della hashmap abbinato all'ID 
				if (id_addr==-1) { // se ID mai creato 
					id_addr = count; 
					st.insert(((Word)look).lexeme,count++); // inserisco nella hashmap l'abbinamento ID, count 
				}
				// valore da inserire tramite read 
				if(flag == 'r'){
					// case READ
					code.emit(OpCode.invokestatic, 0);// 0 per prendere un valore da input
				}
				
				code.emit(OpCode.istore, id_addr); //stampa istore 
				match(Tag.ID);
				idlistp(flag, value);
				break;
			default:
				error("syntax error - idlist()");
				return 0;
		}
		return id_addr;
	}
	
	public void idlistp(char flag, int value) { // se ho più ID 
		switch(look.tag){
			//GUIDA ( <idlistp> ::= , ID <idlistp> ) = { , }
			case ',':
				match(',');
				int id_addr = st.lookupAddress(((Word)look).lexeme); // indirizzo della hashmap abbinato all'ID 
				if (id_addr==-1) { // se ID mai creato 
					id_addr = count; 
					st.insert(((Word)look).lexeme,count++); // inserisco nella hashmap l'abbinamento ID, count 
				}
				// valore da inserire tramite read 
				if(flag == 'r'){
					// case READ
					code.emit(OpCode.invokestatic, 0);// 0 per prendere un valore da input
				}
				
				/*questo else non so se vada bene*/
				/*forse bisogna aggiungere un argomento*/
				if(flag == 'a') {
					code.emit(OpCode.ldc, value);
				}
				
				
				code.emit(OpCode.istore, id_addr); //stampa istore 
				match(Tag.ID);
				idlistp(flag, value);
			//GUIDA ( <idlistp> ::= ε ) = { ), ;, EOF, }, end, else }
			case ')':
				break;
			case ';':
				break;
			case Tag.EOF:
				break;
			case '}':
				break;
			case Tag.END:
				break;
			case Tag.ELSE:
				break;
			default:
				error("syntax error - idlistp()");
				break;
				
		}
	}
	
	public void bexpr(int label_true) { //controlla la condizione e stampa i comandi 
		//GUIDA ( <bexpr> ::= RELOP <expr> <expr> ) = { RELOP }
		if (look.tag == Tag.RELOP){
			// per memorizzare la relazione che stiamo analizzando (es. ==, !=, <= ...)
			String tmp = ((Word)look).lexeme;
			match(Tag.RELOP);
			expr(); //prima o dopo gli if?
			expr();
			if (tmp.equals(Word.eq.lexeme)) // se relop è "==" stampo ifcmpeq
                    code.emit(OpCode.if_icmpeq, label_true);
            if (tmp.equals(Word.ge.lexeme)) 
                    code.emit(OpCode.if_icmpge, label_true);
            if (tmp.equals(Word.gt.lexeme))
                    code.emit(OpCode.if_icmpgt, label_true);
            if (tmp.equals(Word.le.lexeme)) 
                    code.emit(OpCode.if_icmple, label_true);
            if (tmp.equals(Word.lt.lexeme)) 
                    code.emit(OpCode.if_icmplt, label_true);
            if (tmp.equals(Word.ne.lexeme)) 
                    code.emit(OpCode.if_icmpne, label_true);
		} else 
			error("syntax error - bexpr()");
	}
	
	public int expr() {
		switch(look.tag) {
			//GUIDA ( <expr> ::= + ( <exprlist> ) ) = { + }
			case '+':
				match('+');
				match('(');
				exprlist('+');
				match(')');
				return -1;
				//break;
			//GUIDA ( <expr> ::= - <expr> <expr> ) = { - }
			case '-':
				match('-');
				expr();
				expr();
				code.emit(OpCode.isub);
				return -1;
				//break;
			//GUIDA ( <expr> ::= * ( <exprlist> ) ) = { * }
			case '*':
				match('*');
				match('(');
				exprlist('*');
				match(')');
				return -1;
				//break;
			//GUIDA ( <expr> ::= / <expr> <expr> ) = { / }
			case '/':
				match('/');
				expr();
				expr();
				code.emit(OpCode.idiv);
				return -1;
				//break;
			//GUIDA ( <expr> ::= NUM ) = { NUM }
			case Tag.NUM:
				int n = ((NumberTok)look).value;
				code.emit(OpCode.ldc, n); // secondo parametro è il valore di NUM 
				match(Tag.NUM);
				return n;
				//break;
			//GUIDA ( <expr> ::= ID ) = { ID }
			case Tag.ID:
				int id_addr = st.lookupAddress(((Word)look).lexeme);
				code.emit(OpCode.iload, id_addr);
				match(Tag.ID);
				return -1;
				//break;
			default: 
				error("syntax error - expr()");
				return -1;
		}
	}
	
	public void exprlist(char flag) {
		//GUIDA ( <exprlist> ::= <expr> <exprlistp> ) = { +, -, *, /, NUM, ID }
		switch(look.tag) {
			case '+':
			case '-':
			case '*':
			case '/': 
			case Tag.NUM:
			case Tag.ID:
				expr();
				if (flag == 'p'){
					code.emit(OpCode.invokestatic, 1); // 1 è per il print 
				}
				exprlistp(flag);
				break;
			default: 
				error("syntax error - exprlist()");
		}
	}
	
	public void exprlistp(char flag) {
		switch (look.tag){
			//GUIDA ( <exprlistp> ::= , <expr> <exprlistp> ) = { , }
			case ',':
				match(',');
				expr();
				if (flag == 'p'){
					code.emit(OpCode.invokestatic, 1); // 1 è per il print 
				}
				if (flag == '+'){
					code.emit(OpCode.iadd);
				}
				if (flag == '*'){
					code.emit(OpCode.imul);
				}
				exprlistp(flag);
				break;
			//GUIDA ( <exprlistp> ::= ε ) = { ) }
			case ')':
				break;
			default: 
				error("syntax error - exprlistp()");
		}
	}
	
	public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "test.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator translator = new Translator(lex, br);
            translator.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}