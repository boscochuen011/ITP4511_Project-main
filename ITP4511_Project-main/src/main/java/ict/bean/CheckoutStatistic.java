/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ict.bean;

/**
 *
 * @author boscochuen
 */

public class CheckoutStatistic {
    private String equipmentName;
    private int checkouts;

    public CheckoutStatistic(String equipmentName, int checkouts) {
        this.equipmentName = equipmentName;
        this.checkouts = checkouts;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public int getCheckouts() {
        return checkouts;
    }

    public void setCheckouts(int checkouts) {
        this.checkouts = checkouts;
    }
}