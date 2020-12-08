package cfg;

import java.util.ArrayList;

public class Produccion {
	private ArrayList<Variable>		valores;
	
	public Produccion() {
		valores = new ArrayList<Variable>();
	}
	
	public void addValor(Variable valor) {
		this.valores.add(valor);
	}
	
	public ArrayList<Variable> getValores() {
		return valores;
	}

	public void setValores(ArrayList<Variable> valores) {
		this.valores = valores;
	}
	
	public boolean esUnitaria() {
		if(this.valores.size() == 0) {
			return false;
		}
		return this.valores.size() == 1 && !valores.get(0).isTerminal();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Produccion other = (Produccion) obj;
		if (valores == null) {
			if (other.valores != null)
				return false;
		} else if (!valores.equals(other.valores)) {
			if(valores.size() != other.valores.size()) {
				return false;
			}
			// Si recorro todo el arreglo de valores y no sale, eso quiere decir que ambas prodcucciones son iguales.
			for(int i = 0; i < valores.size(); i++)
			{
				if(!valores.get(i).getNombre().equals(other.valores.get(i).getNombre())){
					return false;
				}
			}
		}
		return true;
	}
	
	@Override
	public String toString() {
		String aux = "";
		for(Variable v : this.valores){
			aux = aux + v.getNombre();
		}
		return aux;
	}
	
}
