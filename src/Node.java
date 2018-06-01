import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

public class Node extends Arbol {

	private final Arbol nizq, nder;

	private String idOp;
	Arbol toUpdate;
	String result;
	String assembler;
	NodeHoja leaf;
	public static int nroaux = 0;
	public static int nroOut = 0;
	public static Integer label = new Integer(0);
	private static Stack<String> pila = new Stack<String>();
	
	public static String ret1 = "__ret1";
	public static String ret2 = "__ret2";
	private static Stack<String> caseVar = new Stack<String>();
	public static String data = "";
	
	static boolean primerCase=true;

	public Node(String id, Arbol hijoizq, Arbol hijoder) {
		super();
		this.idOp = id;
		this.nizq = hijoizq;
		this.nder = hijoder;
	}

	public Arbol getNodeizq() {
		return nizq;
	}

	public Arbol getNodeder() {
		return nder;
	}

	public boolean esHoja() {
		return false;
	}

	public Tree createTree() {
		List<Tree> aux = new Vector<Tree>();
		if (nizq != null)
			aux.add(nizq.createTree());
		if (nder != null)
			aux.add(nder.createTree());
		return new Tree("'" + idOp + "'", aux);
	}

	public NodeHoja getLeaf() {
		return leaf;
	}

	public static String getLabel() {
		label++;
		return "label" + label;
	}

	public static String getOutVariableName() {
		nroOut++;
		return "@out" + nroOut;
	}

	public static String getAuxVariableName() {
		nroaux++;
		return "@aux" + nroaux;
	}

	public String getAssembler() {
		String code = "";
		switch (idOp) {
		case "+":
			code += nizq.getAssembler() + nder.getAssembler();
			result = Node.getAuxVariableName();
			leaf = new NodeHoja(result, nizq.getLeaf().getType(), "auxiliar");

			if (nizq.getLeaf().getType().equals("uinteger")) {
				code += "MOV EAX, " + nizq.getLeaf().getId() + "\n";
				code += "ADD EAX, " + nder.getLeaf().getId() + "\n";
				code += "JO errorMult\n";
				code += "MOV " + result + ", EAX" + "\n";
			} else {
				code += "FLD " + nizq.getLeaf().getId() + "\n";
				code += "FADD " + nder.getLeaf().getId() + "\n";
				
				code += "FSTSW AX\n";
				code += "SAHF\n";
				code += "JO errorMult\n";
				
				code += "FST " + result + "\n";
			}
			break;

		case "-":
			code += nizq.getAssembler() + nder.getAssembler();
			result = Node.getAuxVariableName();
			leaf = new NodeHoja(result, nizq.getLeaf().getType(), "auxiliar");

			if (nizq.getLeaf().getType().equals("uinteger")) {
				code += "MOV EAX, " + nizq.getLeaf().getId() + "\n";
				code += "SUB EAX, " + nder.getLeaf().getId() + "\n";
				code += "JS errorResta\n";
				code += "MOV " + result + ", EAX" + "\n";
			} else {
				code += "FLD " + nizq.getLeaf().getId() + "\n";
				code += "FSUB " + nder.getLeaf().getId() + "\n";
				
				code += "FSTSW AX\n";
				code += "SAHF\n";
				code += "JO errorMult\n";
				
				
				code += "FST " + result + "\n";
				
			}
			break;

		case "*":
			code += nizq.getAssembler() + nder.getAssembler();
			result = Node.getAuxVariableName();
			leaf = new NodeHoja(result, nizq.getLeaf().getType(), "auxiliar");

			if (nizq.getLeaf().getType().equals("uinteger")) {
				code += "MOV EAX, " + nizq.getLeaf().getId() + "\n";
				code += "MUL " + nder.getLeaf().getId() + "\n";
				code += "JO errorMult\n";
				code += "MOV " + result + ", EAX" + "\n";
			} else {
				code += "FLD " + nizq.getLeaf().getId() + "\n";
				code += "FMUL " + nder.getLeaf().getId() + "\n";
				
				code += "FSTSW AX\n";
				code += "SAHF\n";
				code += "JO errorMult\n";
				
				code += "FST " + result + "\n";
			}
			break;

		case "/":
			code += nizq.getAssembler() + nder.getAssembler();
			result = Node.getAuxVariableName();
			leaf = new NodeHoja(result, nizq.getLeaf().getType(), "auxiliar");
			if (nizq.getLeaf().getType().equals("uinteger")) {
				code += "CMP " + nder.getLeaf().getId() + ", 0\n";
				code += "JE errorDivCero\n";
				
				code += "MOV EAX, " + nizq.getLeaf().getId() + "\n";
				code += "DIV " + nder.getLeaf().getId() + "\n";
				code += "MOV " + result + ", EAX" + "\n";
				
			} else {
				code += "FLD " + nder.getLeaf().getId() + "\n"; //lo resto para obtener el 0.
				code += "FSUB " + nder.getLeaf().getId() + "\n";
				code += "FCOMP " + nder.getLeaf().getId() + "\n";
				code += "FSTSW AX\n";
				code += "SAHF\n";
				code += "JE errorDivCero\n";
				
				code += "FLD " + nizq.getLeaf().getId() + "\n";
				code += "FDIV " + nder.getLeaf().getId() + "\n";
				code += "FST " + result + "\n";
			}
			break;

		case "=":
			code += nizq.getAssembler() + nder.getAssembler();
			if (nizq.getLeaf().getType().equals("uinteger")) {
				code += "MOV EAX, " + nder.getLeaf().getId() + "\n";
				code += "MOV " + nizq.getLeaf().getId() + ", EAX" + "\n";
			} else {
				code += "FLD " + nder.getLeaf().getId() + "\n";
				code += "FST " + nizq.getLeaf().getId() + "\n";
			}
			break;

		case ">":
			code += makeComparison("JLE","JBE");
			break;
		case "<":
			code += makeComparison("JGE","JAE");
			break;
		case ">=":
			code += makeComparison("JL","JB");
			break;
		case "<=":
			code += makeComparison("JG","JA");
			break;
		case "<>":
			code += makeComparison("JE","JE");
			break;
		case "==":
			code += makeComparison("JNE","JNE");
			break;

		case "INVOCACION":
			result = getAuxVariableName();
			leaf = new NodeHoja(result, nizq.getLeaf().getType(), "auxiliar");
			code += "call " + nizq.getLeaf().getId() + "\n";
			if (nizq.getLeaf().getType().equals("uinteger")) {
				code += "MOV EAX, " + ret1 + "\n";
				code += "MOV " + result + ", EAX";
			} else {
				code += "FLD " + ret2 + "\n";
				code += "FST " + result;
			}
			code += "\n";
			code += "FINIT \n";
			break;

		case "FUNCION":
			AssemblerGenerator.functionCode += nizq.getLeaf().getId() + ":\n";
			AssemblerGenerator.functionCode += nder.getAssembler();
			AssemblerGenerator.functionCode += "ret\n";
			break;
		case "CUERPO_FUNCION":
			code += nizq.getAssembler() + nder.getAssembler();
			if (nder.getLeaf().getType().equals("uinteger")) {
				code += "MOV EAX" + ", " + nder.getLeaf().getId() + "\n";
				code += "MOV " + ret1 + ", EAX\n";
			} else {
				code += "FLD  " + nder.getLeaf().getId() + "\n";
				code += "FST " + ret2 + "\n";
			}
			break;

		case "LS":
			code += nizq.getAssembler();
			code += nder.getAssembler();
			break;

		case "PROGRAM":
			code += nizq.getAssembler();
			break;

		case "IF":
			code += nizq.getAssembler();
			code += nder.getAssembler();
			break;

		case "CONDICION_IF":
			code += nizq.getAssembler();
			break;

		case "CUERPO_IF":
			code += nizq.getAssembler();
			if (nder != null) {
				String label = Node.getLabel();
				code += ("JMP " + label + "\n");
				code += (pila.pop() + ":" + "\n");
				pila.push(label);
				code += nder.getAssembler();
			}
			code += (pila.pop() + ":\n");
			break;

		case "BLOQUE_THEN":
			code += nizq.getAssembler();
			break;

		case "BLOQUE_ELSE":
			code += nizq.getAssembler();
			break;

		case "RETURN":
			code += nizq.getAssembler();
			leaf = new NodeHoja(nizq.getLeaf().getId(), nizq.getLeaf().getType(), "auxiliar");
			break;

		case "SWITCH":
			caseVar.push(nizq.getLeaf().getId());
			primerCase=true;
			code += nder.getAssembler();
			//primerCase=true;
			code += pila.pop() + ":\n";
			caseVar.pop();
			break;

		case "CASE":
			if (primerCase)  primerCase = false;
			else code += pila.pop() + ":\n";
			String label = getLabel();
			if (nizq.getLeaf().getType().equals("uinteger")) {
				code += "MOV EAX, " + caseVar.peek() + "\n"; 
				code += "CMP EAX, " + nizq.getLeaf().getId() + "\n";
			}
			else {
				code += "FLD " + caseVar.peek() + "\n";
				code += "FCOMP " + nizq.getLeaf().getId() + "\n";
				code += "FSTSW AX\n";
				code += "SAHF\n";
			}
			code += "JNE " + label + "\n";
			pila.push(label);
			code += nder.getAssembler();
			break;

		case "CUERPO_CASE":
			code += nizq.getAssembler();
			code += nder.getAssembler();
			break;
			
		case "BLOQUE":
			code += nizq.getAssembler();
			break;


		case "OUT":
			String outName = getOutVariableName();
			data += outName + " db \"" + nizq.getLeaf().getId() + "\", 0\n	";
			SymbolTable.addAuxiliarVariable(outName, "out", "string");
			code += "invoke MessageBox, NULL, addr " + outName + ", addr " + outName + ", MB_OK\n";
			break;

		}
		return code;
	}

	private String makeComparison(String compTypeInt, String compTypeDouble) {
		String aux = "";
		String label = Node.getLabel();
		result = Node.getAuxVariableName();
		aux += nizq.getAssembler();
		aux += nder.getAssembler();
		leaf = new NodeHoja(result, nizq.getLeaf().getType(), "auxiliar");

		if (nizq.getLeaf().getType().equals("uinteger")) {
			aux += "MOV EAX, " + nizq.getLeaf().getId() + "\n";
			aux += "CMP EAX, " + nder.getLeaf().getId() + "\n";
			aux += compTypeInt + " " + label + "\n";
		} else {
			aux += "FLD " + nizq.getLeaf().getId() + "\n";
			aux += "FCOMP " + nder.getLeaf().getId() + "\n";
			aux += "FSTSW AX\n";
			aux += "SAHF\n";
			aux += compTypeDouble + " " + label + "\n";
		}
		
		pila.push(label);
		return aux;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return idOp;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}