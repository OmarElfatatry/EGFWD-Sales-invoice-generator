/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;


import java.util.ArrayList;
import java.util.Date;
import model.InvoiceHeader;
import model.InvoicesHeaderTableModel;
import view.View;

/**
 *
 * @author OmarTarek
 */
public class LeftSideOperations 
{
    LoadTablesContents loadTablesContents=new LoadTablesContents();
    static void updateTableDate(View view, ArrayList<InvoiceHeader> invoices)
    {
        InvoicesHeaderTableModel.setInvoicesHeaderTableModel(view).setValueAt(invoices.get(view.getInvoiceTable().getSelectedRow()).getInoviceDate(), view.getInvoiceTable().getSelectedRow(), 1);
    }

    static void updateTableCustomerName(View view, ArrayList<InvoiceHeader> invoices) 
    {
        InvoicesHeaderTableModel.setInvoicesHeaderTableModel(view).setValueAt(invoices.get(view.getInvoiceTable().getSelectedRow()).getInoviceCustomerName(), view.getInvoiceTable().getSelectedRow(), 2);
    }
    static void updateTableTotal(View view, ArrayList<InvoiceHeader> invoices)
    {
        InvoicesHeaderTableModel.setInvoicesHeaderTableModel(view).setValueAt(invoices.get(view.getInvoiceTable().getSelectedRow()).getInoviceTotal(), view.getInvoiceTable().getSelectedRow(), 3);
    }
    static void showCreatNewInvoiceDialog(View view) 
    {
        Date invDate=new Date();//creat new current date
        view.setLocations(); //set location of creatNewInvoice Jdialog acccording to Invoice Header Table
        view.getNewInvoiceDateField().setText(view.getDate().format(invDate)); //set the new date before opening Jdialog
        View.getCreatNewInvoiceDialog().setVisible(true); //show the Jdialog
    }

    static void addNewInvoice(View view, ArrayList<InvoiceHeader> invoices) 
    {
        //check if no customer name entered 
        if(view.getNewCustomerName().getText().equalsIgnoreCase(""))
        {
            View.getCreatNewInvoiceDialog().setModal(false);
            view.setJOptionPaneMessagMessage(view.getLeftSidePanel(), "Please Enter A Name For The Customer", "Empty Name Entered", "ERROR_MESSAGE");
            View.getCreatNewInvoiceDialog().setModal(true);
            showCreatNewInvoiceDialog(view);
        }
        else
        {
            Controller.maxNumberOfExistedInvoices++;
            Controller.isThereIsNotSavedEdit=true;
            try{
                //creat new InvoiceHeader Row
                InvoiceHeader newRow=new InvoiceHeader((Controller.maxNumberOfExistedInvoices),(view.getDate().parse(view.getNewInvoiceDateField().getText())),(view.getNewCustomerName().getText()));
                //add InvoiceHeader row to the invoices arraylist
                invoices.add(newRow);
                //reload the invoice header table
                LoadTablesContents.loadInvoicesHeaderTable(view, invoices);
                //dissapear the Creat New Invoice Dialog
                View.getCreatNewInvoiceDialog().setVisible(false);
                //Select the new row automatically
                view.getInvoiceTable().setRowSelectionInterval((invoices.size()-1), (invoices.size()-1));
                //Clear the new customer name
                view.getNewCustomerName().setText("");
                //clear the date field
                view.getNewInvoiceDateField().setText("");
                //reload Right side text fileds
                RightSideOperations.rightSideTextUpdater(view, invoices, invoices.size()-1);
                //reload Right side table
                LoadTablesContents.loadInvoicesLineTable(view, invoices);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
     static void deleteSelectedInvoice(View view, ArrayList<InvoiceHeader> invoices)
    {
        int invoiceToBeDeleted= view.getInvoiceTable().getSelectedRow();
        //check if no row selected
        if((invoiceToBeDeleted==-1))
        {
            view.setJOptionPaneMessagMessage(view.getLeftSidePanel(), "Select Invoice Row First", "Error", "ERROR_MESSAGE");
        }
        else
        {
            Controller.isThereIsNotSavedEdit=true;
            //remove the selected row from the invoice array list
            invoices.remove(invoiceToBeDeleted);
            //reload the invoice header table
            LoadTablesContents.loadInvoicesHeaderTable(view, invoices);
            //Select the row before deleted row automatically
            if(!invoices.isEmpty())
            {
                view.getInvoiceTable().setRowSelectionInterval((invoices.size()-1), (invoices.size()-1));
                RightSideOperations.rightSideTextUpdater(view, invoices, invoices.size()-1);
                LoadTablesContents.loadInvoicesLineTable(view, invoices);
            }
            else
            {
                view.getDeleteInvoiceButton().setEnabled(false);
                RightSideOperations.rightSideDisable(view);
                CleanTablesContent.cleanInvoicesLineTable(view);
            }
        }
    }
    public static void calculateInvoiceTableTotal(ArrayList<InvoiceHeader> invoices) 
    {
        float total=0; //to store total of each invoice
        for(int i=0;i<invoices.size();i++)
        {
            total=0;
            for(int j=0;j<invoices.get(i).getInvoicerow().size();j++)
            {
               total = total + ((invoices.get(i).getInvoicerow().get(j).getItemPrice())*(invoices.get(i).getInvoicerow().get(j).getItemCount()));
            }
            invoices.get(i).setInoviceTotal(total);
        }
    }
}
