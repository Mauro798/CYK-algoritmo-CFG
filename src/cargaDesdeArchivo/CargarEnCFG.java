package cargaDesdeArchivo;

import cfg.ContextFreeGrammar;
import cfg.Produccion;
import cfg.Variable;

public class CargarEnCFG {

	public static ContextFreeGrammar obtenerGramatica (String nombreArchivo) {
		ContextFreeGrammar CFG = new ContextFreeGrammar();
		for(String s : Archivo.obtenerLineas(nombreArchivo)) {
			boolean pasoInicio = false;
			boolean terminoInicio = false;
        	boolean pasoProduccion = false;
        	Variable v = new Variable(false, "");
        	Produccion p = new Produccion();
	        for (int i = 0; i < s.length(); i++) {
	        	if(s.charAt(i) == '<' && !pasoInicio) {
	        		pasoInicio = true;
	        	}
	        	if(pasoInicio && !terminoInicio) {
	        		v.setNombre(Character.toString(s.charAt(i)));
	        	}
	        	if(s.charAt(i) == '>' && pasoInicio) {
	        		terminoInicio = true;
	        	}
	        	if(pasoInicio && terminoInicio && s.charAt(i) == '<') {
	        		pasoProduccion = true;
	        	}
	        	if(pasoInicio && terminoInicio && pasoProduccion && s.charAt(i) != '>') {
	        		Variable v1;
	        		if(Character.isUpperCase(s.charAt(i))) {
	        			v1 = new Variable(false, Character.toString(s.charAt(i)));
	        			p.addValor(v1);
	        			CFG.addVariable(v1);
	        		}
	        		else {
	        			v1 = new Variable(true, Character.toString(s.charAt(i)));
	        			p.addValor(v1);
	        		}
	        	}
	        	
	        }
	        v.addProduccion(p);
	        CFG.addVariable(v);
		}
		return CFG;
	}
}
