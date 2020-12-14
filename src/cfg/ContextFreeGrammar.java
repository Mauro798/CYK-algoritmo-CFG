package cfg;

import java.util.ArrayList;
import java.util.HashSet;

public class ContextFreeGrammar {
	/* Hay dos tipos de simbolos, terminales y no terminales. la gramatica solo aceptara no terminales */
	private ArrayList<Variable>		variables;
	public  Variable 				primera;
	
	public ContextFreeGrammar(){
		this.variables = new ArrayList<Variable>();
	}
	
	/* Si se agrega dos veces la misma variable pero con distintas producciones, se añada a esa variable las producciones de la nueva 
	 * ademas, la gramatica solo recibe variables no terminales. Las terminales se encuentran dentro de las variables como producciones.*/
	public void addVariable(Variable variable){
		
		if(!variable.isTerminal()) {
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
			if(primera == null) {
				primera = this.variables.get(0);
			}
		}
		
	}
	
	public ArrayList<Variable> getVariables(){
		return this.variables;
	}
	/* Para pasar una gramatica cualquiera a FNC hay que seguir los siguientes pasos:
	 * 1°: Limpiar la gramatica para que cada lado derecho de las producciones sea o bien un 
	 * terminal o bien tenga longitud al menos 2
	 * 2°: Para cada lado derecho que no sea una terminal, hacer que todo el lado derecho
	 * este compuesto por varoables.
	 * 3°: Descomponer los lados derechos de longitod mayor a 2 en una cadena de producciones
	 * con lados derechos de dos variables. */
	public void pasarAFNC() {
		limpiarGramatica();
		paso2();
		paso3();
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
	 *Repetir hasta que no se agreguen mas producciones. */
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
	private void borrarProduccionesUnitarias(HashSet<ParUnitario> paresUnitarios) {
		ArrayList<Variable> P1 = new ArrayList<Variable>();
		for(Variable v : this.variables) {
			for(Produccion p : v.getProducciones()) {
				if(!p.esUnitaria()) {
					for(ParUnitario par : paresUnitarios) {
						if(par.getSegundoCaracter().equals(v.getNombre())) {
							/* Una vez que se que tengo que agregar la produccion, reviso si la variable a la que pertenece ya existe. 
							 * Si ya lo hace lo agrego a esta, si no, la creo */
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
		//Esto asegura que el Primer valor siempre sea el de entrada
		ArrayList<Variable> P2 = new ArrayList<Variable>();
		P2.addAll(P1);
		for(Variable v : P2) {
			if(v.getNombre().equals(this.primera.getNombre())) {
				P1.remove(v);
				P1.add(0,v);
			}
		}
		this.variables = P1;
	}
	
	/* Descubriendo simbolos generadores:
	 * Caso Base: Agregar los simbolos Terminales al conjunto Res.
	 * Caso inductivo: Si existe una produccion A-> a en todas las producciones, donde
	 * a consiste  unicamente de simbolos de Res, Agregar A a Res. 
	 * Hacer esto hasta que Res no tenga simbolos nuevos.
	 * Una vez descubiertos los simbolos no generadores, hay que eliminarlos.
	 * Esto se haace eliminando todas las producciones donde aparescan simbolos
	 * que no estan en Res, ya sea a la derecha o a la izquierda.						*/
	public void eliminarSimbolosNoGeneradores() {
		ArrayList<Variable> Res = obtenerGeneradores();
		eliminarSimbolos(Res); 
	}
	
	public ArrayList<Variable> obtenerGeneradores(){
		ArrayList<Variable> Res = new ArrayList<Variable>();
		boolean hayCambios = true;
		/* Caso base: Agregar los simbolos terminales a Res */
		for(Variable v : this.variables) {
			for(Produccion p : v.getProducciones()) {
				for(Variable v2 : p.getValores()) {
					if(v2.isTerminal())	{
						/* Antes de añadirla reviso si no esta ya */
						boolean esta = false;
						for(Variable v3 : Res) {
							if(v3.getNombre().equals(v2.getNombre())) {
								esta = true;
								break;
							}
						}
						if(!esta) {
							Res.add(v2);
						}
					}
				}
			}
		}	
		/* Paso inductivo. */
		while(hayCambios){
			hayCambios = false;
			/* Uso un arreglo auxiliar para guardar
			 * los cambios y agregarlos al final del la iteracion */
			ArrayList<Variable> cambios = new ArrayList<Variable>();
			for(Variable v : this.variables) { // Ciclo de las variables de la gramatica
				for(Produccion p : v.getProducciones()) { // Ciclo de las producciones de cada variable
					boolean todosPertenecenARes = true;
					for(Variable v2 : p.getValores()) { // Ciclo las variable de cada produccion
						boolean estaEnRes = false;
						for(Variable v3 : Res) { // Ciclo de Res para ver si pertenecen
							if(v2.getNombre().equals(v3.getNombre())) {
								estaEnRes = true;
								break;
							}
						}
						if(!estaEnRes) {
							todosPertenecenARes = false;
							break;
						}
					}
					if(todosPertenecenARes) {
						cambios.add(v); //Con que una sola de las producciones sea correcta alcanza para mandar la variable a Res
						break;
					}
				}
			}
			/* Ahora agrego el contenido nuevo de cambios a Res y me fijo si existen cambios nuevos,
			 * si no es asi, termino el ciclo */
			for(Variable vc : cambios){
				boolean esta = false;
				for(Variable vr : Res) {
					if(vr.getNombre().equals(vc.getNombre())) {
						esta = true;
						break;
					}
				}
				if(!esta) {
					hayCambios = true;
					Res.add(vc);
				}
			}		
		}
		return Res;
	}
	/* Este metodo elimina de las variables de al gramatica, las variables y producciones que
	 * que no se encuentren en el arreglo que se le pasa. Esto se usa para eliminar los simbolos
	 * inutiles (no generadores y no alcanzables) */
	private void eliminarSimbolos(ArrayList <Variable> Res) {
		/* Elimino los simbolos de la parte izquierda primero */
		ArrayList<Variable> aux = new ArrayList<Variable>();
		ArrayList<Variable> iteradorAuxiliar = new ArrayList<Variable>();
		for(Variable v: this.variables) {
			iteradorAuxiliar.add(v);
		}
		for(Variable v: iteradorAuxiliar) {
			boolean generadora = false;
			for(Variable vr : Res) {
				if(v.getNombre().equals(vr.getNombre())) {
					generadora = true;
					break;
				}
			}
			if(!generadora) {
				this.variables.remove(v);
			}
		}
		/* Ahora elimino las partes derechas, esto es mas complejo porque si una variable
		 * tiene mas de una produccion hay que sacarle las producciones que tengan simbolos 
		 * no generadores */
		for(Variable v : this.variables) {
			Variable vAux = new Variable(v.isTerminal(),v.getNombre()); // Creo una nueva variable auxiliar.
			for(Produccion p : v.getProducciones()) { /* Reviso todas las producciones y las añado a la nueva variable si son validas */
				boolean esValida = true;
				for(Variable vp : p.getValores()) {
					boolean generador = false;
					for(Variable vr : Res) {
						if(vp.getNombre().equals(vr.getNombre())) {
							generador = true;
							break;
						}
					}
					if(!generador) {
						esValida = false;
						break;
					}
				}
				if(esValida) {
					vAux.addProduccion(p);
				}
			}
			aux.add(vAux);
		}
		//Esto asegura que el Primer valor siempre sea el de entrada
		ArrayList<Variable> P2 = new ArrayList<Variable>();
		P2.addAll(aux);
		for(Variable v : P2) {
			if(v.getNombre().equals(this.primera.getNombre())) {
				aux.remove(v);
				aux.add(0,v);
			}
		}
		this.variables = aux;
	}
	/* Caso Base: Podemos alcanzar S(el simbolo inicial)
	 * Caso inductivo: Si podemos alcanzar A, y existe una produccion A->a,
	 * entoces podemos alcanzar todos los simbolos de a */
	public void eliminarSimbolosNoAlcanzables() {
		ArrayList<Variable> Res = obtenerNoAlcanzables();
		eliminarSimbolos(Res);
	}
	
	public ArrayList<Variable> obtenerNoAlcanzables(){
		ArrayList<Variable> Res = new ArrayList<Variable>();
		boolean hayCambios = true;
		/* Primero añadimos al arreglo el caso base */
		Res.add(this.variables.get(0)); //Asumimos que por la forma en la que se cargan las gramaticas, el primer valor simpre va a ser el inicial.
		for(Produccion p : this.variables.get(0).getProducciones()) {
			for(Variable v2 : p.getValores()) {
				//Veo todas los simbolos que produce S, y lo agrego al arreglo si no esta
				boolean esta = false;
				for(Variable vr : Res) {
					if(vr.getNombre().equals(v2.getNombre())) {
						esta = true;
					}
				}
				if(!esta) {
					Res.add(v2);
				}
			}
		}
		/* Paso inductivo */
		
		while(hayCambios) {
			ArrayList<Variable> cambios = new ArrayList<Variable>();
			hayCambios = false;
			for(Variable v : this.getVariables()) { //lleno el arreglo cambios con todos los simbolos que tendrian que ir a la lista.
				boolean estaEnRes = false;
				for(Variable vr : Res) {
					if(v.getNombre().equals(vr.getNombre())) {
						estaEnRes = true;
					}
				}
				if(estaEnRes) {
					for(Produccion p : v.getProducciones()) {
						for(Variable v2 : p.getValores()) {
							//Veo todas los simbolos que produce S, y lo agrego al arreglo si no esta
							boolean esta = false;
							for(Variable vr : Res) {
								if(vr.getNombre().equals(v2.getNombre())) {
									esta = true;
								}
							}
							if(!esta) {
								cambios.add(v2);
							}
						}
					}
				}
			}
			/* Recorro el arreglo cambios y el Res. Si hay un simbolo que Res no tenga, lo agrego y cambio el bool hay cambios.
			 * Si no hay diferencias entre ambos, termina el ciclo while */
			for(Variable vc : cambios) {
				boolean esta = false;
				for(Variable vr : Res) {
					if(vc.getNombre().equals(vr.getNombre())) {
						esta = true;
					}
				}
				if(!esta) {
					Res.add(vc);
					hayCambios = true;
				}
			}
		}
		
		return Res;
	}
	/* En este paso se crean nuevas variables para que los lados derechos esten compuestos
	 * o unicamente de variables o unicamente de terminales.
	 * Esto requiere crear y darle nombre a nuevas variables 
	 * Las variables generadas en ese paso se llamaran: X1, X2, X3, ...*/
	public void paso2() {
		/* Se usan dos arreglos, que luego se unen, uno para las variables nuevas que se crean, y otro para las que ya existen pero se cambian.
		 * Esto se hace para asegurar que las variables se guarden en orden y siempre quede primera la variable de entrada */
		ArrayList<Variable> nuevasVariables = new ArrayList<Variable>();
		ArrayList<Variable> variablesCambiadas = new ArrayList<Variable>();
		int contadorVariable = 0;
		for(Variable v : this.variables) {
			Variable nuevaVariable = new Variable(v.isTerminal(), v.getNombre());
			for(Produccion p : v.getProducciones()) {
				Produccion nuevaProduccion = new Produccion();
				/* primero revisio si la produccion esta compuesta unicamente por terminales
				 * si es asi, la dejo como esta. */
				boolean todasTerminales = true;
				for(Variable v2 : p.getValores() ) {
					if(!v2.isTerminal()) {
						todasTerminales = false;
					}
				}
				if(todasTerminales) {
					nuevaProduccion = p;
					nuevaVariable.addProduccion(nuevaProduccion);
				}
				/* SI no esta compuesta solo por terminales, reviso variable por variable, si es
				 * terminal, me fijo si hay una variable que genere UNICAMENTE a esa terminal, primero en nuevas variables, luego en this.variables.
				 * Si es asi, la remplazo por la variable que la genera.
				 * Si no esta en ninguna de las dos, creo una nueva variable que produsca esta terminal usando el contador para ver
				 * si la nueva variable ya existe o no, y reemplazo esta por la terminal.*/
				else {
					for(Variable v2 : p.getValores()) {
						// En principio asumimos que no es terminal, y la dejamos como esta. Esto cambia mas adelante
						Variable nuevaGeneradora;
						/* Si el simbolo es terminal, entonces hay que buscar una variable que lo genere, si no existe, crear una */
						if(v2.isTerminal()) {
							boolean yaExiste = false;
							for(Variable v3 : nuevasVariables) {
								/* Reviso si esta variable genera una unica produccion compuesta por una unica terminal y veo si esta es la terminal que estamos revisando actualmente
								 * si es asi*/
								if(v3.getProducciones().size() == 1) {
									if(v3.getProducciones().get(0).getValores().size() == 1) {
										if(v3.getProducciones().get(0).getValores().get(0).isTerminal()) {
											if(v3.getProducciones().get(0).getValores().get(0).getNombre().equals(v2.getNombre())) {
												//Si cumple todas estas condiciones
												yaExiste = true;
												nuevaProduccion.addValor(v3);
												break;
											}
										}
									}
								}
							}
							/* Si no estaba en las nuevas variables generadas, puede que exista una variable que cumpla estas condiciones en this.variables */
							if(!yaExiste) {
								for(Variable v3 : this.variables) {
									/* Reviso si esta variable genera una unica produccion compuesta por una unica terminal y veo si esta es la terminal que estamos revisando actualmente
									 * si es asi*/
									if(v3.getProducciones().size() == 1) {
										if(v3.getProducciones().get(0).getValores().size() == 1) {
											if(v3.getProducciones().get(0).getValores().get(0).isTerminal()) {
												if(v3.getProducciones().get(0).getValores().get(0).getNombre().equals(v2.getNombre())) {
													//Si cumple todas estas condiciones
													yaExiste = true;
													nuevaProduccion.addValor(v3);
													break;
												}
											}
										}
									}
								}
							}
							
							//Si no esta en niguna de las dos quiere decir que hay que crear la variable, y añadirla tanto a la nueva produccion, como a nuevasVariables
							if(!yaExiste) {
								// Primero reviso si el nombre de la variable que quiero crear ya existe, si es asi, aumento el contador hasta que no exista
								boolean nombreYaExiste = true;
								while(nombreYaExiste) {
									nombreYaExiste = false;
									for(Variable v3 : nuevasVariables) {
										if(v3.getNombre().equals("X" + contadorVariable)) {
											nombreYaExiste = true;
											contadorVariable++;
										}
									}
								}
								//Ahora creo la nueva variable y le añado la variable actual, que es a la que tiene que apuntar.
								nuevaGeneradora = new Variable(false, "X" + contadorVariable);
								Produccion pAux = new Produccion();
								pAux.addValor(v2);
								nuevaGeneradora.addProduccion(pAux);
								
								//Ahora añado esta nueva variable a el arreglo nuevas Variables y a la produccion actual
								nuevaProduccion.addValor(nuevaGeneradora);
								nuevasVariables.add(nuevaGeneradora);
							}
						}
						//Si la variable no es terminal, entonces la añado como esta a la nueva produccion
						else {
							nuevaProduccion.addValor(v2);
						}
					}
					//Añado la produccion cambiada a una nueva variable;
					nuevaVariable.addProduccion(nuevaProduccion);
				}
			
			}
			variablesCambiadas.add(nuevaVariable);
		}
		variablesCambiadas.addAll(nuevasVariables);
		
		ArrayList<Variable> P2 = new ArrayList<Variable>();
		P2.addAll(variablesCambiadas);
		for(Variable v : P2) {
			if(v.getNombre().equals(this.primera.getNombre())) {
				variablesCambiadas.remove(v);
				variablesCambiadas.add(0,v);
			}
		}
		this.variables = variablesCambiadas;
	}
	
	/* En este paso se separan las producciones compuestas por 2 o mas variables
	 * Esto requiere crear y darle nombre a nuevas variables 
	 * Las variables generadas en ese paso se llamaran: W1, W2, W3, ...**/
	public void paso3() {
		int contadorVariable = 0;
		boolean hayCambios = true;
		
		while(hayCambios) {
			hayCambios = false;
			ArrayList<Variable> variablesNuevas = new ArrayList<Variable>();
			ArrayList<Variable> variablesCambiadas = new ArrayList<Variable>();
	
			for(Variable v : this.variables) {
				Variable nuevaVariableParaReemplazar = new Variable(v.isTerminal(), v.getNombre());
				for(Produccion p : v.getProducciones()) {
					Produccion nuevaProduccionParaReemplazar = new Produccion();
					/* primero revisio si la produccion mide menos de 2 o esta compuesta por terminales
					 * si es asi, la dejo como esta. */
					boolean tieneSTerminal = false;
					for(Variable v2 : p.getValores()) {
						if(v2.isTerminal()) {
							tieneSTerminal = true;
							break;
						}
					}
					if(p.getValores().size() <= 2 || tieneSTerminal) {
						nuevaVariableParaReemplazar.addProduccion(p);
					}
					/* si mide mas de 2, cambio desde la posicion 2 en adelante por una nueva variable y la guardo en nuevasVariables */
					else {
						hayCambios = true;
						//Creo una nueva produccion con esas caracteristicas.
						Produccion nuevaSubProduccion = new Produccion();
						for(int i = 0 ; i < p.getValores().size() ; i++) {
							if(i == 0) {
								nuevaProduccionParaReemplazar.addValor(p.getValores().get(0));
							}else {
								nuevaSubProduccion.addValor(p.getValores().get(i));
							}	
						}					
						//Reviso si ya hay una en variablesNuevas que genere esta produccion, si la hay, la uso
						boolean yaEsta = false;
						for(Variable nv : variablesNuevas) {
							boolean genera = true;
							if(nv.getProducciones().size() == 1) {
								if(nv.getProducciones().get(0).getValores().size() == nuevaSubProduccion.getValores().size()) {
									for(int i = 0 ; i <  nv.getProducciones().get(0).getValores().size() ; i++) {
										if(!nv.getProducciones().get(0).getValores().get(i).getNombre().equals(nuevaSubProduccion.getValores().get(i).getNombre())) {
											genera = false;
										}
									}
								}
								else {
									genera = false;
								}
							}
							else {
								genera = false;
							}
							if(genera) {
								nuevaProduccionParaReemplazar.addValor(nv);
								nuevaVariableParaReemplazar.addProduccion(nuevaProduccionParaReemplazar);
								yaEsta = true;
								break;
							}
						}
						
						//Reviso si ya hay una en this.variables que genere solo esta produccion, si la hay, la uso
						if(!yaEsta) {
							for(Variable nv : this.variables) {
								boolean genera = true;
								if(nv.getProducciones().size() == 1) {
									if(nv.getProducciones().get(0).getValores().size() == nuevaSubProduccion.getValores().size()) {
										for(int i = 0 ; i <  nv.getProducciones().get(0).getValores().size() ; i++) {
											if(!nv.getProducciones().get(0).getValores().get(i).getNombre().equals(nuevaSubProduccion.getValores().get(i).getNombre())) {
												genera = false;
											}
										}
									}
									else {
										genera = false;
									}
								}
								else {
									genera = false;
								}
								if(genera) {
									nuevaProduccionParaReemplazar.addValor(nv);
									nuevaVariableParaReemplazar.addProduccion(nuevaProduccionParaReemplazar);
									System.out.println(nuevaVariableParaReemplazar.toString());
									yaEsta = true;
									break;
								}
							}
						}	
						//SI no hay en ninguna de las dos, entonces la creo, la añado a la produccion y a nuevas variables
						if(!yaEsta) {
							boolean nombreYaExiste = true;
							while(nombreYaExiste) {
								nombreYaExiste = false;
								for(Variable v3 :this.variables) {
									if(v3.getNombre().equals("W" + contadorVariable)) {
										nombreYaExiste = true;
										contadorVariable++;
									}
								}
								for(Variable v3 : variablesNuevas) {
									if(v3.getNombre().equals("W" + contadorVariable)) {
										nombreYaExiste = true;
										contadorVariable++;
									}
								}
							}
							Variable nuevaVGeneradora = new Variable(false, "W" + contadorVariable);
							nuevaVGeneradora.addProduccion(nuevaSubProduccion);
							
							//Ahora añado esta nueva variable a el arreglo nuevas Variables y a la produccion actual
							variablesNuevas.add(nuevaVGeneradora);
							
							nuevaProduccionParaReemplazar.addValor(nuevaVGeneradora);
							nuevaVariableParaReemplazar.addProduccion(nuevaProduccionParaReemplazar);
						}			
					}
				}
				variablesCambiadas.add(nuevaVariableParaReemplazar);
			}
			variablesCambiadas.addAll(variablesNuevas);
			ArrayList<Variable> P2 = new ArrayList<Variable>();
			P2.addAll(variablesCambiadas);
			for(Variable v : P2) {
				if(v.getNombre().equals(this.primera.getNombre())) {
					variablesCambiadas.remove(v);
					variablesCambiadas.add(0,v);
				}
			}
			this.variables = variablesCambiadas;
		}
	}
	
	public boolean pertenece(String s) {
		ArrayList<Variable> variablesQueGeneranS = cykRecursivo(s);
		
		//revios si la primera variable esta en el arreglo, si es asi, la gramatica genera el String;
		
		for(Variable v : variablesQueGeneranS) {
			if(v.getNombre().equals(this.variables.get(0).getNombre())) {
				return true;
			}
		}
		return false;
	}
	//Dados dos arreglos de variables elijo las variables que generan un elemento de cada uno de los arreglos en el orden en el que se dan los arreglos.
	public ArrayList<Variable> cykRecursivo(String subString) {
		ArrayList<Variable> ret = new ArrayList<Variable>();
		//Caso base, el String mide 1
		if(subString.length() == 1) {
			for(Variable v : this.variables) {
				for(Produccion p : v.getProducciones()) {
					if(p.getValores().get(0).isTerminal()) {
						//si la variable genera 
						if(p.getValores().get(0).getNombre().equals(subString)) {
							ret.add(v);
						}
					}
				}
			}
			return ret;
		}
		//caso recursivo. El String mide mas de 1
		for(int i = 1; i < subString.length(); i++) {
			ArrayList<Variable> primeraParte = cykRecursivo(subString.substring(0, i));
			ArrayList<Variable> segundaParte = cykRecursivo(subString.substring(i+1));
		
			for(Variable v : this.variables) {
				for(Produccion p : v.getProducciones()) {
					if(!p.getValores().get(0).isTerminal()) {
						boolean generaPrimera = false;
						//Reviso si la produccion genera algun valor del primre arreglo
						for(Variable v2 : primeraParte) {
							if(p.getValores().get(0).getNombre().equals(v2.getNombre())) {
								generaPrimera = true;
							}
						}

						boolean generaSegunda = false;
						//Reviso si la produccion genera algun valor del primre arreglo
						for(Variable v2 : segundaParte) {
							if(p.getValores().get(1).getNombre().equals(v2.getNombre())) {
								generaSegunda = true;
							}
						}
						
						if(generaPrimera && generaSegunda) {
							ret.add(v);
							break;//con que una produccion genere, alcanza para añadir la variable. Esto impide que se añadan duplicados inesesarios si mas de una produccion genera.
						}
					}
				}
			}
		}
		
		return ret;
	}
}
