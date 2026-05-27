/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author elena
 */
public class ConfiguracionCuotaModel {
    private int id;
    private float monto;
    
    public ConfiguracionCuotaModel(int id, float monto) {
        this.id = id;
        this.monto= monto;
    }
    
        // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public float getMonto() { return monto;}
    public void setMonto(float monto) { this.monto = monto;}
}
    

