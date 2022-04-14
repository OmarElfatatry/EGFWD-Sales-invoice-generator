/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package listeners;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.FileOperations;
import view.View;
import controller.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
/**
 *
 * @author OmarTarek
 */
public class FileMenuItemsListener implements ActionListener ,MenuListener
{
    private FileOperations fileOperations;
    private InvoiceTableListener invoiceTableListener;
    private View view=null;
    
    public FileMenuItemsListener(View view,FileOperations fileOperations,InvoiceTableListener invoiceTableListener) 
    {
        this.view=view;
        this.fileOperations=fileOperations;
        this.invoiceTableListener=invoiceTableListener;
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch(e.getActionCommand())
        {
            case "Load Fisle Sequence":
            {
                /*Clear Both Table*/
                Controller.isThereIsNotSavedEdit=false;
                CleanTablesContent.cleanInvoicesHeaderTable(view);
                CleanTablesContent.cleanInvoicesLineTable(view);
                //clean total
                view.getInvoiceTotalLabel().setText("");
                fileOperations.getFilesPaths();
                if((FileOperations.selectedInvoiceHeader!=null)&&(FileOperations.selectedInvoiceLine!=null))
                {
                    Controller.invoices=fileOperations.readFile();
                    fileOperations.testMain(Controller.invoices);
                    LeftSideOperations.calculateInvoiceTableTotal(Controller.invoices);
                    LoadTablesContents.loadInvoicesHeaderTable(view,Controller.invoices);
                    fileOperations.getMaxNumberOfExistedInvoices(Controller.maxNumberOfExistedInvoices,Controller.invoices);
                }
                break;
            }
            case "Save File":
            {
                //Write the invoices arraylist
                fileOperations.writeFile(Controller.invoices);
                //Reload CSV files into tables
                if((FileOperations.selectedInvoiceHeader!=null)&&(FileOperations.selectedInvoiceLine!=null))
                {
                    view.getInvoiceTable().getSelectionModel().removeListSelectionListener(invoiceTableListener);
                    Controller.invoices=fileOperations.readFile();
                    LeftSideOperations.calculateInvoiceTableTotal(Controller.invoices);
                    LoadTablesContents.loadInvoicesHeaderTable(view,Controller.invoices);
                    Controller.isThereIsNotSavedEdit=false;
                    view.getInvoiceTable().getSelectionModel().addListSelectionListener(invoiceTableListener);
                    if(Controller.invoices.size()>=1)
                    view.getInvoiceTable().setRowSelectionInterval(0, 0);
                }
                if(Controller.isThereIsNotSavedEdit)
                {
                    view.getCancelButton().setEnabled(Controller.isThereIsNotSavedEdit);
                }
                else
                {
                    view.getCancelButton().setEnabled(Controller.isThereIsNotSavedEdit);
                }
                break;
            }
        }
    }

    @Override //Enable or disable Save File Button in File Menu
    public void menuSelected(MenuEvent e) 
    {
        if((Controller.isThereIsNotSavedEdit))
        {
            view.getSaveFile().setEnabled(true);
        }
        else
        {
            view.getSaveFile().setEnabled(false);
        }
    }

    @Override
    public void menuDeselected(MenuEvent e){}

    @Override
    public void menuCanceled(MenuEvent e){}
    
}
