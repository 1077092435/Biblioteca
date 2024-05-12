import javax.swing.JOptionPane;

class Usuario {
    String nombre;
    int id;

    public Usuario(String nombre, int id) {
        this.nombre = nombre;
        this.id = id;
    }
}

class Libro {
    int id;
    String titulo;
    String autor;
    boolean prestado;
    Usuario usuarioPrestamo;

    public Libro(int id, String titulo, String autor) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.prestado = false;
        this.usuarioPrestamo = null;
    }

    public void prestarLibro(Usuario usuario) {
        this.prestado = true;
        this.usuarioPrestamo = usuario;
    }

    public void devolverLibro() {
        this.prestado = false;
        this.usuarioPrestamo = null;
    }
}

class NodoLibro {
    Libro libro;
    NodoLibro hijoIzquierdo;
    NodoLibro hijoDerecho;

    public NodoLibro(Libro libro) {
        this.libro = libro;
        this.hijoIzquierdo = null;
        this.hijoDerecho = null;
    }
}

public class Biblioteca {
    static ArbolLibros arbolLibros = new ArbolLibros();
    static int ultimoIdUsuario = 0;

    public static void main(String[] args) {
        short opcion = 0;

        do {
            try {
                opcion = Short.parseShort(JOptionPane.showInputDialog(null, "Escoga lo que desea hacer en la biblioteca:\n"
                        + "1. Agregar Libro.\n"
                        + "2. Mostrar Libros.\n"
                        + "3. Prestar Libro.\n"
                        + "4. Devolver Libro.\n"
                        + "5. Salir", "Biblioteca", JOptionPane.PLAIN_MESSAGE));

                switch (opcion) {
                    case 1:
                        int id = Integer.parseInt(JOptionPane.showInputDialog(null, "Ingrese el ID del libro:", "Agregando Libro", JOptionPane.PLAIN_MESSAGE));
                        String titulo = JOptionPane.showInputDialog(null, "Ingrese el título del libro:", "Agregando Libro", JOptionPane.PLAIN_MESSAGE);
                        String autor = JOptionPane.showInputDialog(null, "Ingrese el autor del libro:", "Agregando Libro", JOptionPane.PLAIN_MESSAGE);
                        arbolLibros.insertar(new Libro(id, titulo, autor));
                        break;

                    case 2:
                        if (!arbolLibros.estaVacio()) {
                            JOptionPane.showMessageDialog(null, "Los libros en la biblioteca son:\n" + arbolLibros.mostrarLibros(), "Biblioteca", JOptionPane.PLAIN_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "No hay libros en la biblioteca.", "Biblioteca", JOptionPane.INFORMATION_MESSAGE);
                        }
                        break;

                    case 3:
                        if (!arbolLibros.estaVacio()) {
                            int idLibroPrestamo = Integer.parseInt(JOptionPane.showInputDialog(null, "Ingrese el ID del libro que desea prestar:", "Prestar Libro", JOptionPane.PLAIN_MESSAGE));
                            Libro libroPrestamo = arbolLibros.buscarLibro(idLibroPrestamo);
                            if (libroPrestamo != null && !libroPrestamo.prestado) {
                                Usuario usuario = crearUsuario();
                                if (usuario != null) {
                                    libroPrestamo.prestarLibro(usuario);
                                    JOptionPane.showMessageDialog(null, "Libro prestado exitosamente a " + usuario.nombre, "Prestar Libro", JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    JOptionPane.showMessageDialog(null, "No se pudo crear el usuario.", "Prestar Libro", JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "El libro no está disponible para préstamo.", "Prestar Libro", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "No hay libros en la biblioteca.", "Prestar Libro", JOptionPane.INFORMATION_MESSAGE);
                        }
                        break;

                    case 4:
                        if (!arbolLibros.estaVacio()) {
                            int idLibroDevolucion = Integer.parseInt(JOptionPane.showInputDialog(null, "Ingrese el ID del libro que desea devolver:", "Devolver Libro", JOptionPane.PLAIN_MESSAGE));
                            Libro libroDevolucion = arbolLibros.buscarLibro(idLibroDevolucion);
                            if (libroDevolucion != null && libroDevolucion.prestado) {
                                libroDevolucion.devolverLibro();
                                JOptionPane.showMessageDialog(null, "Libro devuelto exitosamente.", "Devolver Libro", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "No se puede devolver el libro. Verifique el ID ingresado o si el libro no está prestado.", "Devolver Libro", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "No hay libros en la biblioteca.", "Devolver Libro", JOptionPane.INFORMATION_MESSAGE);
                        }
                        break;

                    case 5:
                        JOptionPane.showMessageDialog(null, "Gracias por usar la aplicación. ¡Hasta luego!", "Biblioteca", JOptionPane.PLAIN_MESSAGE);
                        break;

                    default:
                        JOptionPane.showMessageDialog(null, "La opción ingresada no es válida. Por favor, seleccione una opción válida.", "Biblioteca", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Error al ingresar el número. Por favor, ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } while (opcion != 5);
    }

    static Usuario crearUsuario() {
        String nombreUsuario = JOptionPane.showInputDialog(null, "Ingrese su nombre:", "Crear Usuario", JOptionPane.PLAIN_MESSAGE);
        if (nombreUsuario != null && !nombreUsuario.isEmpty()) {
            ultimoIdUsuario++;
            return new Usuario(nombreUsuario, ultimoIdUsuario);
        } else {
            JOptionPane.showMessageDialog(null, "Debe ingresar un nombre válido.", "Crear Usuario", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}

class ArbolLibros {
    NodoLibro raiz;

    public ArbolLibros() {
        raiz = null;
    }

    public boolean estaVacio() {
        return raiz == null;
    }

    public void insertar(Libro libro) {
        if (estaVacio()) {
            raiz = new NodoLibro(libro);
        } else {
            insertarRecursivo(raiz, libro);
        }
    }

    private void insertarRecursivo(NodoLibro nodo, Libro libro) {
        if (libro.id < nodo.libro.id) {
            if (nodo.hijoIzquierdo == null) {
                nodo.hijoIzquierdo = new NodoLibro(libro);
            } else {
                insertarRecursivo(nodo.hijoIzquierdo, libro);
            }
        } else if (libro.id > nodo.libro.id) {
            if (nodo.hijoDerecho == null) {
                nodo.hijoDerecho = new NodoLibro(libro);
            } else {
                insertarRecursivo(nodo.hijoDerecho, libro);
            }
        }
    }

    public Libro buscarLibro(int id) {
        return buscarLibroRecursivo(raiz, id);
    }

    private Libro buscarLibroRecursivo(NodoLibro nodo, int id) {
        if (nodo == null) {
            return null;
        }

        if (id == nodo.libro.id) {
            return nodo.libro;
        } else if (id < nodo.libro.id) {
            return buscarLibroRecursivo(nodo.hijoIzquierdo, id);
        } else {
            return buscarLibroRecursivo(nodo.hijoDerecho, id);
        }
    }

    public String mostrarLibros() {
        StringBuilder resultado = new StringBuilder();
        mostrarLibrosRecursivo(raiz, resultado);
        return resultado.toString();
    }

    private void mostrarLibrosRecursivo(NodoLibro nodo, StringBuilder resultado) {
        if (nodo != null) {
            mostrarLibrosRecursivo(nodo.hijoIzquierdo, resultado);
            resultado.append("ID: ").append(nodo.libro.id).append(", Título: ").append(nodo.libro.titulo).append(", Autor: ").append(nodo.libro.autor);
            if (nodo.libro.prestado) {
                resultado.append(" (Prestado a ").append(nodo.libro.usuarioPrestamo.nombre).append(")");
            }
            resultado.append("\n");
            mostrarLibrosRecursivo(nodo.hijoDerecho, resultado);
        }
    }
}
