package cfg;

import java.util.ArrayList;

public class Variable {
	private String 					nombre;
	private boolean 				terminal;
	private ArrayList<Produccion>	producciones;
	
	
	/* Para saber si es un terminal o no nos fijamos en el bool terminal
	 * si es terminal, entonces su valor es su nombre */
	public Variable(boolean esTerminal, String nombre){
		this.nombre			= nombre;
		this.terminal 		= esTerminal;
		this.producciones 	= new ArrayList<Produccion>();
	}
	
	/* Para añadir una nueva produccion a la variable, me fijo si ya existia 
	 * no se le puede añadir producciones a una variable terminal*/
	public boolean addProduccion(Produccion p) {
		if(this.terminal) {
			return false;
		}
		if(this.producciones.contains(p)) {
			return false;
		}
		else {
			this.producciones.add(p);
			return true;
		}
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public boolean isTerminal() {
		return terminal;
	}

	public void setTerminal(boolean terminal) {
		this.terminal = terminal;
	}
	
	public ArrayList<Produccion> getProducciones() {
		return producciones;
	}

	public void setProducciones(ArrayList<Produccion> producciones) {
		this.producciones = producciones;
	}
	
	@Override
	public String toString() {
		if(terminal)
		{
			return this.nombre;
		}
		String aux = this.nombre + " -> ";
		for(int i = 0; i < producciones.size(); i++) {
			if(i < producciones.size() - 1) {
				aux =  aux + producciones.get(i).toString() + " | ";
			}
			else {
				aux =  aux + producciones.get(i).toString();
			}
		}
		return aux;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + ((producciones == null) ? 0 : producciones.hashCode());
		result = prime * result + (terminal ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Variable other = (Variable) obj;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (producciones == null) {
			if (other.producciones != null)
				return false;
		} else if (!producciones.equals(other.producciones))
			return false;
		if (terminal != other.terminal)
			return false;
		return true;
	}
}
