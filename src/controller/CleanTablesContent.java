/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.InvoicesHeaderTableModel;
import model.InvoicesLineTableModel;
import view.View;

/**
 *
 * @author OmarTarek
 */
public class CleanTablesContent 
{
    public static void cleanInvoicesHeaderTable(View view) 
    {
       while(InvoicesHeaderTableModel.setInvoicesHeaderTableModel(view).getRowCount()>0)
       {
           InvoicesHeaderTableModel.setInvoicesHeaderTableModel(view).removeRow(0);
       }
    }
    public static void cleanInvoicesLineTable(View view) 
    {
       while(InvoicesLineTableModel.setInvoicesLineTableModel(view).getRowCount()>0)
       {
           InvoicesLineTableModel.setInvoicesLineTableModel(view).removeRow(0);
       }
    }
}
