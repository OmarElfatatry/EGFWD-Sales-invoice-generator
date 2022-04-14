/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import model.InvoiceHeader;
import model.InvoicesHeaderTableModel;
import model.InvoicesLineTableModel;
import view.View;

/**
 *
 * @author OmarTarek
 */
public class LoadTablesContents 
{
    public static void loadInvoicesHeaderTable(View view, ArrayList<InvoiceHeader> invoices) 
    {
       CleanTablesContent.cleanInvoicesHeaderTable(view);
       Object data[]=new Object[4];
       for(int i=0;i<invoices.size();i++)
       {
        data[0]=invoices.get(i).getInoviceNumber();
        data[1]=invoices.get(i).getInoviceDate();
        data[2]=invoices.get(i).getInoviceCustomerName();
        data[3]=invoices.get(i).getInoviceTotal();
        InvoicesHeaderTableModel.setInvoicesHeaderTableModel(view).addRow(data);
       }
    }
    public static void loadInvoicesLineTable(View view, ArrayList<InvoiceHeader> invoices)
    {
        float total=0;
        Object data[]=new Object[5];
        int selectedRow= view.getInvoiceTable().getSelectedRow();
        //check if no row is selected after it was selected before
        if(selectedRow==-1)
        {
            //clear all fields and disable all buttons of right side in GUI
            RightSideOperations.rightSideDisable(view);
            CleanTablesContent.cleanInvoicesLineTable(view);
            //disable delete invoice button on left side if there's no row selected
            view.getDeleteInvoiceButton().setEnabled(false);
        }
        else
        {
            //enable all buttons of the right side in GUI
            RightSideOperations.rightSideEnable(view);
            //enable delete invoice button on left side if there's row selected
            view.getDeleteInvoiceButton().setEnabled(true);
            CleanTablesContent.cleanInvoicesLineTable(view);
            InvoicesLineTableModel.setInvoicesLineTableModel(view).setRowCount(0);
            for(int j=0;j<invoices.get(selectedRow).getInvoicerow().size();j++)
            {
                //Calculate and Set invoice total
                total=((invoices.get(selectedRow).getInvoicerow().get(j).getItemPrice())*(invoices.get(selectedRow).getInvoicerow().get(j).getItemCount()));
                invoices.get(selectedRow).getInvoicerow().get(j).setItemTotal(total);
                data[0]=invoices.get(selectedRow).getInvoicerow().get(j).getMainInvoice().getInoviceNumber();
                data[1]=invoices.get(selectedRow).getInvoicerow().get(j).getItemName();
                data[2]=invoices.get(selectedRow).getInvoicerow().get(j).getItemPrice();
                data[3]=invoices.get(selectedRow).getInvoicerow().get(j).getItemCount();
                data[4]=invoices.get(selectedRow).getInvoicerow().get(j).getItemTotal();
                InvoicesLineTableModel.setInvoicesLineTableModel(view).addRow(data);
                RightSideOperations.rightSideTextUpdater(view,invoices,selectedRow);
            }
        }
    }
}
