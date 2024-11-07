package com.example.khronos;

import java.io.Serializable;

public class AttendanceRecord implements Serializable {

    private String empleado;
    private String documento;
    private String horaEntrada;
    private String horaSalida;
    private String fecha;
    private int horasTrabajadas;
    private int horasExtras;
    private String ausencia;

    // Constructor de la clase
    public AttendanceRecord(String empleado, String documento, String horaEntrada, String horaSalida,
                            String fecha, int horasTrabajadas, int horasExtras, String ausencia) {
        this.empleado = empleado;
        this.documento = documento;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.fecha = fecha;
        this.horasTrabajadas = horasTrabajadas;
        this.horasExtras = horasExtras;
        this.ausencia = ausencia;
    }

    // Métodos getter
    public String getEmpleado() {
        return empleado;
    }

    public String getDocumento() {
        return documento;
    }

    public String getHoraEntrada() {
        return horaEntrada;
    }

    public String getHoraSalida() {
        return horaSalida;
    }

    public String getFecha() {
        return fecha;
    }

    public int getHorasTrabajadas() {
        return horasTrabajadas;
    }

    public int getHorasExtras() {
        return horasExtras;
    }

    public String getAusencia() {
        return ausencia;
    }

    // Métodos setter (si es necesario, por ejemplo, para actualizar un registro)
    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public void setHoraEntrada(String horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public void setHoraSalida(String horaSalida) {
        this.horaSalida = horaSalida;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setHorasTrabajadas(int horasTrabajadas) {
        this.horasTrabajadas = horasTrabajadas;
    }

    public void setHorasExtras(int horasExtras) {
        this.horasExtras = horasExtras;
    }

    public void setAusencia(String ausencia) {
        this.ausencia = ausencia;
    }
}
