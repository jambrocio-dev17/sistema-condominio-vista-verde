/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author yeimy
 */
public class PropietarioModel {
    private int    id;
    private String nombreCompleto;
    private String dpi;
    private String telefono;
    private String correo;
    private int    numeroCasa;

    public PropietarioModel() {}

    public PropietarioModel(String nombreCompleto, String dpi,
                            String telefono, String correo, int numeroCasa) {
        this.nombreCompleto = nombreCompleto;
        this.dpi            = dpi;
        this.telefono       = telefono;
        this.correo         = correo;
        this.numeroCasa     = numeroCasa;
    }

    public int    getId()             { return id; }
    public void   setId(int id)       { this.id = id; }

    public String getNombreCompleto()                    { return nombreCompleto; }
    public void   setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getDpi()            { return dpi; }
    public void   setDpi(String dpi)  { this.dpi = dpi; }

    public String getTelefono()               { return telefono; }
    public void   setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo()             { return correo; }
    public void   setCorreo(String correo) { this.correo = correo; }

    public int  getNumeroCasa()             { return numeroCasa; }
    public void setNumeroCasa(int numeroCasa) { this.numeroCasa = numeroCasa; }
}
