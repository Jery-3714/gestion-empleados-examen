package edu.umg.programacion2.gestionempleados.modelo;

import java.time.LocalDate;

public class Empleado {

    private int id;
    private String nombreCompleto;
    private String departamento;
    private double salario;
    private double bonoCompetencia;
    private LocalDate fechaContratacion;
    private boolean activo;
    
    public Empleado() {
    }

    public Empleado(int id, String nombreCompleto, String departamento,
            double salario, double bonoCompetencia,
            LocalDate fechaContratacion, boolean activo) {

        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.departamento = departamento;
        this.salario = salario;
        this.bonoCompetencia = bonoCompetencia;
        this.fechaContratacion = fechaContratacion;
        this.activo = activo;
    }

    public Empleado(String nombreCompleto, String departamento,

            double salario, double bonoCompetencia,
            LocalDate fechaContratacion, boolean activo) {

        this.nombreCompleto = nombreCompleto;
        this.departamento = departamento;
        this.salario = salario;
        this.bonoCompetencia = bonoCompetencia;
        this.fechaContratacion = fechaContratacion;
        this.activo = activo;
    }
    
    public Empleado(String nombreCompleto, String departamento,

            double salario, LocalDate fechaContratacion, boolean activo) {

        this.nombreCompleto = nombreCompleto;
        this.departamento = departamento;
        this.salario = salario;
        this.bonoCompetencia = 0.00;
        this.fechaContratacion = fechaContratacion;
        this.activo = activo;

    }
   
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }
    
    public double getBonoCompetencia() {

        return bonoCompetencia;

    }

    public void setBonoCompetencia(double bonoCompetencia) {

        this.bonoCompetencia = bonoCompetencia;

    }

    public LocalDate getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(LocalDate fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}