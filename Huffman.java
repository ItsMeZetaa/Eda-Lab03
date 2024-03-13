import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

class NodoHuffman {
    int frecuencia;
    char dato;
    NodoHuffman izquierdo, derecho;

    public NodoHuffman(char dato, int frecuencia) {
        this.dato = dato;
        this.frecuencia = frecuencia;
        izquierdo = null;
        derecho = null;
    }
}

class ColaPrioridadPersonalizada {
    private List<NodoHuffman> nodos;

    public ColaPrioridadPersonalizada() {
        nodos = new ArrayList<>();
    }

    public int tamano() {
        return nodos.size();
    }

    public void agregar(NodoHuffman nodo) {
        nodos.add(nodo);
        int i = nodos.size() - 1;
        while (i > 0 && nodos.get(padre(i)).frecuencia > nodos.get(i).frecuencia) {
            intercambiar(i, padre(i));
            i = padre(i);
        }
    }

    public NodoHuffman extraer() {
        if (nodos.isEmpty()) {
            return null;
        }

        NodoHuffman raiz = nodos.get(0);
        nodos.set(0, nodos.get(nodos.size() - 1));
        nodos.remove(nodos.size() - 1);

        monticuloMinimo(0);

        return raiz;
    }

    private int padre(int i) {
        return (i - 1) / 2;
    }

    private int hijoIzquierdo(int i) {
        return 2 * i + 1;
    }

    private int hijoDerecho(int i) {
        return 2 * i + 2;
    }

    private void intercambiar(int i, int j) {
        NodoHuffman temp = nodos.get(i);
        nodos.set(i, nodos.get(j));
        nodos.set(j, temp);
    }

    private void monticuloMinimo(int i) {
        int izquierdo = hijoIzquierdo(i);
        int derecho = hijoDerecho(i);
        int menor = i;

        if (izquierdo < nodos.size() && nodos.get(izquierdo).frecuencia < nodos.get(i).frecuencia) {
            menor = izquierdo;
        }

        if (derecho < nodos.size() && nodos.get(derecho).frecuencia < nodos.get(menor).frecuencia) {
            menor = derecho;
        }

        if (menor != i) {
            intercambiar(i, menor);
            monticuloMinimo(menor);
        }
    }

    public boolean Vacia() {
        return nodos.isEmpty();
    }
}

public class Huffman {
    private static HashMap<Character, String> mapaCodificado = new HashMap<>();
    private static HashMap<String, Character> mapaDecodificado = new HashMap<>();

    public static void construirArbolHuffman(String texto) {
        HashMap<Character, Integer> mapaFrecuencia = new HashMap<>();
    
        for (char c : texto.toCharArray()) {
            mapaFrecuencia.put(c, mapaFrecuencia.getOrDefault(c, 0) + 1);
        }
    
        ColaPrioridadPersonalizada colaPrioridad = new ColaPrioridadPersonalizada();
        for (Character key : mapaFrecuencia.keySet()) {
            colaPrioridad.agregar(new NodoHuffman(key, mapaFrecuencia.get(key)));
        }
    
        while (colaPrioridad.tamano() > 1) {
            NodoHuffman izquierdo = colaPrioridad.extraer();
            NodoHuffman derecho = colaPrioridad.extraer();
    
            NodoHuffman fusionado = new NodoHuffman('\0', izquierdo.frecuencia + derecho.frecuencia);
            fusionado.izquierdo = izquierdo;
            fusionado.derecho = derecho;
            colaPrioridad.agregar(fusionado);
        }
    
        NodoHuffman raiz = colaPrioridad.extraer();
        generarCodigos(raiz, "");
    }
    

    public static void generarCodigos(NodoHuffman nodo, String codigo) {
        if (nodo == null) {
            return;
        }

        if (nodo.izquierdo == null && nodo.derecho == null) {
            mapaCodificado.put(nodo.dato, codigo);
            mapaDecodificado.put(codigo, nodo.dato);
        }

        generarCodigos(nodo.izquierdo, codigo + "0");
        generarCodigos(nodo.derecho, codigo + "1");
    }

    public static String codificar(String texto) {
        StringBuilder textoCodificado = new StringBuilder();

        for (char c : texto.toCharArray()) {
            textoCodificado.append(mapaCodificado.get(c));
        }

        return textoCodificado.toString();
    }

    public static String decodificar(String textoCodificado) {
        StringBuilder textoDecodificado = new StringBuilder();
        StringBuilder códigoActual = new StringBuilder();

        for (char bit : textoCodificado.toCharArray()) {
            códigoActual.append(bit);
            if (mapaDecodificado.containsKey(códigoActual.toString())) {
                textoDecodificado.append(mapaDecodificado.get(códigoActual.toString()));
                códigoActual = new StringBuilder();
            }
        }

        return textoDecodificado.toString();
    }

    public static String leerTextoDesdeArchivo(String rutaArchivo) {
        StringBuilder texto = new StringBuilder();
        try {
            BufferedReader lector = new BufferedReader(new FileReader(rutaArchivo));
            String linea = lector.readLine();
            while (linea != null) {
                texto.append(linea);
                linea = lector.readLine();
                if (linea != null) {
                    texto.append("\n");
                }
            }
            lector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return texto.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> nombresArchivos = new ArrayList<>();

        nombresArchivos.add("C:\\Users\\Xthen\\OneDrive\\Escritorio\\U\\estructura\\Lab3\\alice_in_wonderland.txt"); // Ruta del primer archivo
        nombresArchivos.add("C:\\Users\\Xthen\\OneDrive\\Escritorio\\U\\estructura\\Lab3\\biblia.txt"); // Ruta del segundo archivo
        nombresArchivos.add("C:\\Users\\Xthen\\OneDrive\\Escritorio\\U\\estructura\\Lab3\\mobydick.txt"); // Ruta del tercer archivo

        System.out.println("Elige el archivo con el que quieres trabajar:");
        System.out.println("1. alice_in_wonderland.txt");
        System.out.println("2. biblia.txt");
        System.out.println("3. mobydick.txt");

        int elección = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea después de leer el número

        if (elección >= 1 && elección <= nombresArchivos.size()) {
            String rutaArchivo = nombresArchivos.get(elección - 1); // Obtener la ruta del archivo seleccionado

            String texto = leerTextoDesdeArchivo(rutaArchivo);

            long tiempoInicio = System.currentTimeMillis(); // Tiempo inicial

            construirArbolHuffman(texto);
            String textoCodificado = codificar(texto);

            long tiempoFin = System.currentTimeMillis(); // Tiempo final
            long tiempoTranscurrido = tiempoFin - tiempoInicio; // Tiempo total de ejecución

            double tamañoOriginal = texto.length() * 8; // Tamaño del texto original en bits
            double tamañoCodificado = textoCodificado.length(); // Tamaño del texto codificado en bits

            double espacioAhorrado = ((tamañoOriginal - tamañoCodificado) / tamañoOriginal) * 100; // Espacio ahorrado en porcentaje

            System.out.println("Cadena codificada para " + rutaArchivo + ": " + textoCodificado);
            System.out.println("Cadena decodificada para " + rutaArchivo + ": " + decodificar(textoCodificado));
            System.out.println("Espacio ahorrado para " + rutaArchivo + ": " + espacioAhorrado + "%");
            System.out.println("Tiempo de ejecución para " + rutaArchivo + ": " + tiempoTranscurrido + " milisegundos");
        } else {
            System.out.println("Opción inválida. Elige un número entre 1 y " + nombresArchivos.size() + ".");
        }

        scanner.close();
    }
}
