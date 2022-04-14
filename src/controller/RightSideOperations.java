/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.FileOperations;
import model.InvoiceHeader;
import model.InvoiceLine;
import view.View;

/**
 *
 * @author OmarTarek
 */
public class RightSideOperations 
{
    static void rightSideDisable(View view)
    {
        view.getInvoiceDateTextField().setText("");
        view.getCustomerNameTextField().setText("");
        view.getInvoiceTotalLabel().setText("");
        view.getInvoiceNumberLabel().setText("");
        view.getAddItemButton().setEnabled(false);
        view.getDeleteItemButton().setEnabled(false);
        view.getInvoiceDateTextField().setEditable(false);
        view.getCustomerNameTextField().setEditable(false);
    }
    static void rightSideEnable(View view)
    {
        view.getAddItemButton().setEnabled(true);
        view.getInvoiceDateTextField().setEditable(true);
        view.getCustomerNameTextField().setEditable(true);
    }
    public static void rightSideTextUpdater(View view, ArrayList<InvoiceHeader> invoices, int selectedRow)
    {
        if(selectedRow!=-1)
        {
            view.getInvoiceNumberLabel().setText(Integer.toString(invoices.get(selectedRow).getInoviceNumber()));
            view.getInvoiceDateTextField().setText(view.getDate().format(invoices.get(selectedRow).getInoviceDate()));
            view.getCustomerNameTextField().setText(invoices.get(selectedRow).getInoviceCustomerName());
            view.getInvoiceTotalLabel().setText(Float.toString(invoices.get(selectedRow).getInoviceTotal()));
        }
    }
    public static void dateFieldValidator(View view,ArrayList<InvoiceHeader> invoices)
    {
        //Confirm that user want to change date
        int choice=view.showYesNoCancelDialog(view.getRightSidePanel(), "Do you want To save date changes?", "Date change confirmation");
        if(choice==0)//yes
        {
            try 
            {
                //Check if new date have the pattern "dd-MM-yyyy" exactly using regex expression
                if(!(view.getInvoiceDateTextField().getText().matches("^\\d{2}\\-\\d{2}\\-\\d{4}")))
                {
                    throw new Exception();
                }
                //Check if Date is valid date
                FileOperations.date.setLenient(false);
                FileOperations.date.parse(view.getInvoiceDateTextField().getText());
                invoices.get(view.getInvoiceTable().getSelectedRow()).setInoviceDate(view.getDate().parse(view.getInvoiceDateTextField().getText()));
                LeftSideOperations.updateTableDate(view,invoices);
                view.getInvoiceDateTextField().requestFocus();
                Controller.isThereIsNotSavedEdit=true;
            } catch (ParseException ex) 
            {
                view.setJOptionPaneMessagMessage(view.getRightSidePanel(),"Please Enter A Valid Date","Wrong Date","ERROR_MESSAGE");
                view.getInvoiceDateTextField().setText(view.getDate().format(invoices.get(view.getInvoiceTable().getSelectedRow()).getInoviceDate()));
                view.getInvoiceDateTextField().requestFocus();
            }
            catch (Exception ex)
            {
                view.setJOptionPaneMessagMessage(view.getRightSidePanel(),"Please Enter A Valid Date Format (e.g 05-09-1993)", "Wrong Date Format","ERROR_MESSAGE");
                view.getInvoiceDateTextField().setText(view.getDate().format(invoices.get(view.getInvoiceTable().getSelectedRow()).getInoviceDate()));
                view.getInvoiceDateTextField().requestFocus();
            }
        }
        else if(choice==1)//no
        {
            view.getInvoiceDateTextField().requestFocus();
        }
        else //cancel
        {
            view.getInvoiceDateTextField().setText(view.getDate().format(invoices.get(Controller.selectedRow).getInoviceDate()));
            view.getInvoiceDateTextField().requestFocus();
        }
    }

    public static void changeCustomerNameTextField(View view, ArrayList<InvoiceHeader> invoices) 
    {
        //Confirm that user want to customer name
        int choice=view.showYesNoCancelDialog(view.getRightSidePanel(), "Do you want To save customer name changes?", "Customer name change confirmation");
        if(choice==0) //yes
        {
            invoices.get(view.getInvoiceTable().getSelectedRow()).setInoviceCustomerName(view.getCustomerNameTextField().getText());
            LeftSideOperations.updateTableCustomerName(view,invoices);
            view.getCustomerNameTextField().requestFocus();
            Controller.isThereIsNotSavedEdit=true;
        }
        else if(choice==1) //no
        {
            view.getCustomerNameTextField().requestFocus();
        }
        else //cancel
        {
            view.getCustomerNameTextField().setText(invoices.get(Controller.selectedRow).getInoviceCustomerName());
            view.getCustomerNameTextField().requestFocus();
        }
    }
    
    static void AddNewItem(View view, ArrayList<InvoiceHeader> invoices)
    {
        String itemName;
        float price=0;
        int count=0;
        boolean errorFlag=false;
        itemName=view.getNewItemName().getText();
        if(itemName.equalsIgnoreCase(""))
        {
            View.getAddItemDialog().setVisible(false);
            view.setJOptionPaneMessagMessage(view.getRightSidePanel(), "Please Enter A Name For The Item", "Empty Item Name Entered", "ERROR_MESSAGE");
            showCreatNewItemDialog(view);
        }
        else if(view.getNewItemPrice().getText().equalsIgnoreCase(""))
        {
            View.getAddItemDialog().setVisible(false);
            view.setJOptionPaneMessagMessage(view.getRightSidePanel(), "Please Enter A Price For The Item", "Empty Price Entered", "ERROR_MESSAGE");
            showCreatNewItemDialog(view);
        }
        else
        {
            try {
                    price=Float.parseFloat(view.getNewItemPrice().getText());
                } catch ( Exception e ) 
                {
                    errorFlag=true;
                }
            try {
                    view.getNewItemPriceSpinner().commitEdit();
                    count = (Integer)view.getNewItemPriceSpinner().getValue();
                } catch ( Exception e ) 
                {
                    errorFlag=true;
                }
            if(!errorFlag)
            {   
                InvoiceHeader temp = invoices.get(view.getInvoiceTable().getSelectedRow());
                InvoiceLine newItem= new InvoiceLine(itemName, price, count, temp);
                temp.getInvoicerow().add(newItem);
                Controller.isThereIsNotSavedEdit=true;
            }
            view.getNewItemName().setText("");
            view.getNewItemPrice().setText("");
            view.getNewItemPriceSpinner().setValue((Object)1);
        }
    }

    static void showCreatNewItemDialog(View view)
    {
        view.setLocations();
        View.getAddItemDialog().setTitle("Add New Item To Invoice Number "+ view.getInvoiceNumberLabel().getText());
        View.getAddItemDialog().setVisible(true);
    }

    static void DeleteItem(View view, ArrayList<InvoiceHeader> invoices) 
    {
        if(view.getInvoicesLineTable().getSelectedRow()>=0)
        {
            int rowToBeDeleted;
            rowToBeDeleted=view.getInvoicesLineTable().getSelectedRow();
            invoices.get(view.getInvoiceTable().getSelectedRow()).getInvoicerow().remove(rowToBeDeleted);
            Controller.isThereIsNotSavedEdit=true;
        }
    }
}
