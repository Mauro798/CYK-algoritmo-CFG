package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cfg.*;

class TestLimpiarGramatica {
	
	@BeforeEach
	static void init() {
		
	}

	@Test
	void testParesUnitarios1() {
		
		/* Gramatica de la forma:			
		 * I -> a | b | Ia | Ib | I0 | I1
		 * F -> I | (E)
		 * T -> F | T*F
		 * E -> T | E+T
		 * */
		ContextFreeGrammar G = new ContextFreeGrammar();
		Variable I = new Variable(false, "I");
		Variable F = new Variable(false, "F");
		Variable T = new Variable(false, "T");
		Variable E = new Variable(false, "E");
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
		G.addVariable(I);
		G.addVariable(F);
		G.addVariable(T);
		G.addVariable(E);

		HashSet<ParUnitario> paresUnitarios = G.descubrirProduccionesunitarias();
		
		assertTrue(paresUnitarios.contains(new ParUnitario("I","I")));
		assertTrue(paresUnitarios.contains(new ParUnitario("F","F")));
		assertTrue(paresUnitarios.contains(new ParUnitario("T","T")));
		assertTrue(paresUnitarios.contains(new ParUnitario("E","E")));
		assertTrue(paresUnitarios.contains(new ParUnitario("F","I")));
		assertTrue(paresUnitarios.contains(new ParUnitario("T","F")));
		assertTrue(paresUnitarios.contains(new ParUnitario("E","T")));
		assertTrue(paresUnitarios.contains(new ParUnitario("T","I")));
		assertTrue(paresUnitarios.contains(new ParUnitario("E","F")));
		assertTrue(paresUnitarios.contains(new ParUnitario("E","I")));
	}
	@Test
	void testParesUnitarios2() {
		ContextFreeGrammar G = new ContextFreeGrammar();
		Variable A = new Variable(false, "A");
		Variable B = new Variable(false, "B");
		Variable C = new Variable(false, "C");
		Variable D = new Variable(false, "D");
		Produccion PA = new Produccion();
		PA.addValor(A);
		Produccion PB = new Produccion();
		PB.addValor(B);
		Produccion PC = new Produccion();
		PC.addValor(C);
		Produccion PD = new Produccion();
		PD.addValor(D);
		A.addProduccion(PB);
		B.addProduccion(PC);
		C.addProduccion(PD);
		D.addProduccion(PA);
		
		G.addVariable(A);
		G.addVariable(B);
		G.addVariable(C);
		G.addVariable(D);
		
		HashSet<ParUnitario> paresUnitarios = G.descubrirProduccionesunitarias();
		
		assertTrue(paresUnitarios.contains(new ParUnitario("A", "A")));
		assertTrue(paresUnitarios.contains(new ParUnitario("A", "B")));
		assertTrue(paresUnitarios.contains(new ParUnitario("A", "C")));
		assertTrue(paresUnitarios.contains(new ParUnitario("A", "D")));
		assertTrue(paresUnitarios.contains(new ParUnitario("B", "A")));
		assertTrue(paresUnitarios.contains(new ParUnitario("B", "B")));
		assertTrue(paresUnitarios.contains(new ParUnitario("B", "C")));
		assertTrue(paresUnitarios.contains(new ParUnitario("B", "D")));
		assertTrue(paresUnitarios.contains(new ParUnitario("C", "A")));
		assertTrue(paresUnitarios.contains(new ParUnitario("C", "B")));
		assertTrue(paresUnitarios.contains(new ParUnitario("C", "C")));
		assertTrue(paresUnitarios.contains(new ParUnitario("C", "D")));
		assertTrue(paresUnitarios.contains(new ParUnitario("D", "A")));
		assertTrue(paresUnitarios.contains(new ParUnitario("D", "B")));
		assertTrue(paresUnitarios.contains(new ParUnitario("D", "C")));
		assertTrue(paresUnitarios.contains(new ParUnitario("D", "D")));
	}

}
