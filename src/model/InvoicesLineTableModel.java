/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import javax.swing.table.DefaultTableModel;
import view.View;

/**
 *
 * @author OmarTarek
 */
public class InvoicesLineTableModel 
{
    public static DefaultTableModel setInvoicesLineTableModel(View view)
    {
        DefaultTableModel newTable= new DefaultTableModel() {};
        newTable= (DefaultTableModel) view.getInvoicesLineTable().getModel();
        view.getInvoicesLineTable().setDefaultEditor(Object.class, null);
        return newTable;
    }
}
