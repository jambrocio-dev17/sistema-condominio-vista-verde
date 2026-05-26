/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author yeimy
 */
public class CasaMorosaModel {
    private int    numeroCasa;
    private String propietario;
    private String telefono;
    private int    mesesDeuda;

    public CasaMorosaModel() {}

    public CasaMorosaModel(int numeroCasa, String propietario, String telefono, int mesesDeuda) {
        this.numeroCasa  = numeroCasa;
        this.propietario = propietario;
        this.telefono    = telefono;
        this.mesesDeuda  = mesesDeuda;
    }

    public int    getNumeroCasa()                  { return numeroCasa; }
    public void   setNumeroCasa(int numeroCasa)    { this.numeroCasa = numeroCasa; }

    public String getPropietario()                 { return propietario; }
    public void   setPropietario(String p)         { this.propietario = p; }

    public String getTelefono()                    { return telefono; }
    public void   setTelefono(String t)            { this.telefono = t; }

    public int    getMesesDeuda()                  { return mesesDeuda; }
    public void   setMesesDeuda(int m)             { this.mesesDeuda = m; }
}
