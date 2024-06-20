/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jueguito;

import java.util.ArrayList;
import java.util.List;

public class Pregunta {
    private String enunciado;
    private List<String> opciones;
    private int respuestaCorrecta;

    public Pregunta(String enunciado, List<String> opciones, int respuestaCorrecta) {
        this.enunciado = enunciado;
        this.opciones = opciones;
        this.respuestaCorrecta = respuestaCorrecta;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public List<String> getOpciones() {
        return opciones;
    }

    public int getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    public static List<Pregunta> generarPreguntas() {
        List<Pregunta> preguntas = new ArrayList<>();
        preguntas.add(new Pregunta("¿Cuál es la capital de Francia?",
                                    List.of("París", "Madrid", "Londres", "Roma"),
                                    0));
        // Puedes agregar más preguntas aquí
        return preguntas;
    }
}