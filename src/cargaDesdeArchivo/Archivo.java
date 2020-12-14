package cargaDesdeArchivo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public interface Archivo {

	public static List<String> obtenerLineas(String nombreArchivo) {
		List<String> lineas = new ArrayList<>();
		FileReader fr = null;
		BufferedReader br = null;
		try {
			File archivo = new File(nombreArchivo); 
			fr = new FileReader(archivo); 
			br = new BufferedReader(fr); 
			String linea = "";
			while ((linea = br.readLine()) != null) {
				lineas.add(linea);
			}
			fr.close();
			br.close();
			return lineas;
		} catch (Exception e) {
			return null;
		}
	}

}
