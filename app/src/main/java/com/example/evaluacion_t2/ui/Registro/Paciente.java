package com.example.evaluacion_t2.ui.Registro;

public class Paciente {

    String id;
    String nombre;
    String apaterno;
    String amaterno;
    String edad;
    String sexo;
    Float altura;
    Float peso;
    String informacion;

    public Paciente(String id, String nombre, String apaterno, String amaterno, String edad, String sexo, Float peso, Float altura, String informacion) {
        this.id = id;
        this.nombre = nombre;
        this.apaterno = apaterno;
        this.amaterno = amaterno;
        this.edad = edad;
        this.sexo = sexo;
        this.peso = peso;
        this.altura = altura;
        this.informacion = informacion;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApaterno() {
        return apaterno;
    }
    public void setApaterno(String apaterno) {
        this.apaterno = apaterno;
    }
    public String getAmaterno() {
        return amaterno;
    }
    public void setAmaterno(String amaterno) {
        this.amaterno = amaterno;
    }
    public String getEdad() {
        return edad;
    }
    public void setEdad(String edad) {
        this.edad = edad;
    }
    public String getSexo() {
        return sexo;
    }
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
    public Float getAltura() {
        return altura;
    }
    public void setAltura(Float altura) {
        this.altura = altura;
    }
    public Float getPeso() {
        return peso;
    }
    public void setPeso(Float peso) {
        this.peso = peso;
    }
    public String getInformacion() {
        return informacion;
    }
    public void setInformacion(String informacion) {
        this.informacion = informacion;
    }
}