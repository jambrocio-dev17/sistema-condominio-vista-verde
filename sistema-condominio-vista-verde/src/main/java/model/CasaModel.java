/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author mc296
 */
public class CasaModel {
    private int numeroCasa;

    public CasaModel() {}
    public CasaModel(int numeroCasa) { this.numeroCasa = numeroCasa; }

    public int  getNumeroCasa()               { return numeroCasa; }
    public void setNumeroCasa(int numeroCasa) { this.numeroCasa = numeroCasa; }

    @Override
    public String toString() { return "Casa " + numeroCasa; }  
}
