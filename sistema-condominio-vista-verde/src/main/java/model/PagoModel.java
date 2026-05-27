/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;



/**
 *
 * @author elena
 */
public class PagoModel {
    private int    id;
    private int    numeroCasa;
    private int    mes;
    private int    anio;
    private double monto;
    private String estado;

    public PagoModel() {}

    public PagoModel(int numeroCasa, int mes, int anio, double monto) {
        this.numeroCasa = numeroCasa;
        this.mes        = mes;
        this.anio       = anio;
        this.monto      = monto;
        this.estado     = "Pagado";
    }

    public int    getId()                { return id; }
    public void   setId(int id)          { this.id = id; }

    public int  getNumeroCasa()               { return numeroCasa; }
    public void setNumeroCasa(int numeroCasa) { this.numeroCasa = numeroCasa; }

    public int  getMes()       { return mes; }
    public void setMes(int mes) { this.mes = mes; }

    public int  getAnio()        { return anio; }
    public void setAnio(int anio) { this.anio = anio; }

    public double getMonto()          { return monto; }
    public void   setMonto(double m)  { this.monto = m; }

    public String getEstado()             { return estado; }
    public void   setEstado(String estado) { this.estado = estado; }
}
