package main;

import java.util.ArrayList;

import cfg.ContextFreeGrammar;
import cfg.ParUnitario;
import cfg.Produccion;
import cfg.Variable;

public class Main {

	public static void main(String[] args) {
//		ContextFreeGrammar CFG = new ContextFreeGrammar();
//		Variable a = new Variable(true, "a");
//		Variable b = new Variable(true, "b");
//		Variable c = new Variable(true, "c");
//		Variable d = new Variable(true, "d");
//		Variable A = new Variable(false, "A");
//		Variable B = new Variable(false, "B");
//		Variable C = new Variable(false, "C");
//		
//		Produccion p1 = new Produccion();
//		p1.addValor(a);
//		p1.addValor(B);
//		p1.addValor(A);
//		p1.addValor(c);
//		p1.addValor(d);
//		Produccion p2 = new Produccion();
//		p2.addValor(a);
//		Produccion p3 = new Produccion();
//		p3.addValor(C);
//		p3.addValor(a);
//		p3.addValor(b);
//		p3.addValor(A);
//		Produccion p4 = new Produccion();
//		p4.addValor(a);
//		p4.addValor(B);
//		p4.addValor(c);
//		p4.addValor(a);
//		
//		A.addProduccion(p1);
//		A.addProduccion(p2);
//		B.addProduccion(p3);
//		C.addProduccion(p4);
//		C.addProduccion(p2);
//		
//		CFG.addVariable(A);
//		CFG.addVariable(B);
//		CFG.addVariable(C);
		
//		for(Variable v: CFG.getVariables()) {
//			System.out.println(v.toString());
//		}
//		
//		for(ParUnitario p: CFG.descubrirProduccionesunitarias()) {
//			System.out.println("(" + p.getPrimerCaracter() + ", " + p.getSegundoCaracter() + " )");
//		}
		
		ContextFreeGrammar G = new ContextFreeGrammar();
		Variable I = new Variable(false, "I");
		Variable F = new Variable(false, "F");
		Variable T = new Variable(false, "T");
		Variable E = new Variable(false, "E");
		Variable B = new Variable(false, "B");
		Produccion B1= new Produccion();
		B1.addValor(B);
		B1.addValor(new Variable(true, "a"));
		B.addProduccion(B1);
		Produccion I1 = new Produccion();
		I1.addValor(new Variable(true, "a"));
		Produccion I2 = new Produccion();
		I2.addValor(new Variable(true, "b"));
		Produccion I3 = new Produccion();
		I3.addValor(I);
		I3.addValor(new Variable(true, "a"));
		Produccion I4 = new Produccion();
		I4.addValor(I);
		I4.addValor(new Variable(true, "b"));
		Produccion I5 = new Produccion();
		I5.addValor(I);
		I5.addValor(new Variable(true, "0"));
		Produccion I6 = new Produccion();
		I6.addValor(I);
		I6.addValor(new Variable(true, "1"));
		
		I.addProduccion(I1);
		I.addProduccion(I2);
		I.addProduccion(I3);
		I.addProduccion(I4);
		I.addProduccion(I5);
		I.addProduccion(I6);
		
		Produccion F1 = new Produccion();
		F1.addValor(I);
		Produccion F2 = new Produccion();
		F2.addValor(new Variable(true, "("));
		F2.addValor(E);
		F2.addValor(new Variable(true, ")"));
		
		F.addProduccion(F1);
		F.addProduccion(F2);
		
		Produccion T1 = new Produccion();
		T1.addValor(F);
		Produccion T2 = new Produccion();
		T2.addValor(T);
		T2.addValor(new Variable(true, "*"));
		T2.addValor(F);
		T2.addValor(F);
		T2.addValor(E);
		
		T.addProduccion(T1);
		T.addProduccion(T2);
		
		
		Produccion E1 = new Produccion();
		E1.addValor(T);
		Produccion E2 = new Produccion();
		E2.addValor(E);
		E2.addValor(new Variable(true, "+"));
		E2.addValor(T);
		
		E.addProduccion(E1);
		E.addProduccion(E2);
		
		G.addVariable(T);
		G.addVariable(F);
		G.addVariable(I);
		G.addVariable(E);
		G.addVariable(B);
		
//		ContextFreeGrammar G = new ContextFreeGrammar();
//		Variable A = new Variable(false, "A");
//		Variable B = new Variable(false, "B");
//		Variable C = new Variable(false, "C");
//		Variable D = new Variable(false, "D");
//		Produccion PA = new Produccion();
//		PA.addValor(A);
//		Produccion PB = new Produccion();
//		PB.addValor(B);
//		Produccion PC = new Produccion();
//		PC.addValor(C);
//		Produccion PD = new Produccion();
//		PD.addValor(D);
//		A.addProduccion(PB);
//		B.addProduccion(PC);
//		C.addProduccion(PD);
//		D.addProduccion(PA);
//		
//		G.addVariable(A);
//		G.addVariable(B);
//		G.addVariable(C);
//		G.addVariable(D);
		for(Variable v: G.getVariables()) {
			System.out.println(v.toString());
		}
		
		ArrayList<Variable> v3 = G.obtenerGeneradores();
		
		for(Variable v2 : v3) {
			System.out.println("Variable Generadora: " + v2.getNombre());
		} 
		
		for(ParUnitario p: G.descubrirProduccionesunitarias()) {
			System.out.println("(" + p.getPrimerCaracter() + ", " + p.getSegundoCaracter() + " )");
		}
		
		G.eliminarProduccionesUnitarias();
		
		for(Variable v: G.getVariables()) {
			System.out.println(v.toString());
		}
		
		System.out.println("Sin No generadores:");
		
		G.eliminarSimbolosNoGeneradores();
		
		for(Variable v: G.getVariables()) {
			System.out.println(v.toString());
		}
		
		System.out.println("Sin no alcanzables");
		
		G.eliminarSimbolosNoAlcanzables();
		
		for(Variable v: G.getVariables()) {
			System.out.println(v.toString());
		}
		
		System.out.println("Limpiando terminales");
		
		G.paso2();
		
		for(Variable v: G.getVariables()) {
			System.out.println(v.toString());
		}
		
		System.out.println("Cortando mayores de 2");
		
		G.paso3();
		
		for(Variable v: G.getVariables()) {
			System.out.println(v.toString());
		}
	
	}
}
