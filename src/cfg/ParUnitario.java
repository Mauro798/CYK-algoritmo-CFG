package cfg;

public class ParUnitario {
		private String primerCaracter;
		private String segundoCaracter;
		
		public ParUnitario(String primer, String segundo) {
			this.primerCaracter 	= primer;
			this.segundoCaracter 	= segundo;
		}
		public String getPrimerCaracter() {
			return primerCaracter;
		}

		public void setPrimerCaracter(String primerCaracter) {
			this.primerCaracter = primerCaracter;
		}

		public String getSegundoCaracter() {
			return segundoCaracter;
		}

		public void setSegundoCaracter(String segundoCaracter) {
			this.segundoCaracter = segundoCaracter;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((primerCaracter == null) ? 0 : primerCaracter.hashCode());
			result = prime * result + ((segundoCaracter == null) ? 0 : segundoCaracter.hashCode());
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
			ParUnitario other = (ParUnitario) obj;
			if (primerCaracter == null) {
				if (other.primerCaracter != null)
					return false;
			} else if (!primerCaracter.equals(other.primerCaracter))
				return false;
			if (segundoCaracter == null) {
				if (other.segundoCaracter != null)
					return false;
			} else if (!segundoCaracter.equals(other.segundoCaracter))
				return false;
			return true;
		}
		
}
