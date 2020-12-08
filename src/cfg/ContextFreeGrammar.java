package cfg;

import java.util.ArrayList;
import java.util.HashSet;

public class ContextFreeGrammar {
	private ArrayList<Variable>		variables;
	
	public ContextFreeGrammar(){
		this.variables = new ArrayList<Variable>();
	}
	
	//Si se agrega dos veces la misma variable pero con distintas producciones, se añada a esa variable las producciones de la nueva
	public void addVariable(Variable variable){
		boolean esta = false;
		for(Variable v : this.variables) {
			if (variable.getNombre().equals(v.getNombre())) {
				for(Produccion p : variable.getProducciones()) {
					v.addProduccion(p);
				}
				esta = true;
			}
		}
		if(!esta) {
			this.variables.add(variable);
		}
	}
	
	public ArrayList<Variable> getVariables(){
		return this.variables;
	}
	
	public void limpiarGramatica(){
		eliminarEProducciones();
		eliminarProduccionesUnitarias();
		eliminarSimbolosNoGeneradores();
		eliminarSimbolosNoAlcanzables();
	}
	
	/* la version del punto 1 se asume sin producciones epsilon */
	public void eliminarEProducciones() {
	}
	
	/*Una produccion unitaria es una produccion tal que si lado derecho consiste en
	 *exactamente una variable. Estas producciones pueden ser eliminadoas.*/
	public void eliminarProduccionesUnitarias() {
		//Se usa un hashSet para garantizar que no haya repetidos en el conjunto de pares unitarios.
		HashSet<ParUnitario> paresUnitarios = descubrirProduccionesunitarias(); 
		borrarProduccionesUnitarias(paresUnitarios);
	}
	
	/*Encontrar todos los pares (A,B) tales qeu A =>* B  por una secuencia
	 *de producciones unitarias. Llamamos a estos (A,B) pares unitarios
	 *Caso Base: Agregar (A,A) para cada bariable A.
	 *Caso Inductivo: Si encontramos (A,B), y B -> C es una produccion unitaria,
	 *entonces agregar(A,C). 
	 *Repetir hasta que no se agreguen mas producciones.*/
	public HashSet<ParUnitario> descubrirProduccionesunitarias() {
		HashSet<ParUnitario> aux 	= new HashSet<ParUnitario>();
		boolean hayCambios 			= true;
		for(Variable v : this.variables) {
			//Caso base
			aux.add(new ParUnitario(v.getNombre(), v.getNombre()));
			//reviso todas las producciones de cada variable y si es unitaria la agrego al arreglo haciendo par con la variable actual.
			for (Produccion p : v.getProducciones()) {
				if(p.esUnitaria()) {
					aux.add(new ParUnitario(v.getNombre(), p.getValores().get(0).getNombre()));
				}
			}
		}
		//una vez agregado el caso base y las producciones unitarias basicas, hago el paso inductivo.
		while(hayCambios) {
			hayCambios = false; // Si no hay mas cambios, esto se queda en false y se sale del ciclo
			//En base a los pares unitarios actuales, agrego mas siguiendo el algoritmo.
			ArrayList<ParUnitario> arrayAux = new ArrayList<ParUnitario>();
			for(ParUnitario par1 : aux) {
				for(ParUnitario par2 : aux) {
					
					//Si el primer par es (A,B) y el segundo (B,C), entonces agrego (A,C) al conjunto.
					if(par1.getSegundoCaracter().equals(par2.getPrimerCaracter())) {
						arrayAux.add(new ParUnitario(par1.getPrimerCaracter(), par2.getSegundoCaracter()));//lo añado a un arreglo auxiliar para luego poder agregarlos al Hashset
					}
				}
			}
			for(ParUnitario pu : arrayAux) {
				if(aux.add(pu)) {
					hayCambios = true; // Si se realiza un cambio, entonces se sigue con el ciclo while.
				}
			}
		}
		return aux;
	}
	
	/*Una vez tenemos todos los pares unitarios seguimos con el algoritmo
	 *Para cada par unitario (A,B), agregar a P1 todas las producciones A->a, donde B->a
	 *es una produccion no unitaria en P.*/
	public void borrarProduccionesUnitarias(HashSet<ParUnitario> paresUnitarios) {
		ArrayList<Variable> P1 = new ArrayList<Variable>();
		for(Variable v : this.variables) {
			for(Produccion p : v.getProducciones()) {
				if(!p.esUnitaria()) {
					for(ParUnitario par : paresUnitarios) {
						if(par.getSegundoCaracter().equals(v.getNombre())) {
							boolean esta = false;
							for(Variable v2 : P1) {
								if(v2.getNombre().equals(par.getPrimerCaracter())) {
									v2.addProduccion(p);
									esta = true;
								}
							}
							if(!esta) {
								Variable VAux = new Variable(false, par.getPrimerCaracter());
								VAux.addProduccion(p);
								P1.add(VAux);
							}
						}
					}
				}
			}
		}
		this.variables = P1;
	}
	
	public void eliminarSimbolosNoGeneradores() {
		
	}
	
	public void eliminarSimbolosNoAlcanzables() {
		
	}
}
