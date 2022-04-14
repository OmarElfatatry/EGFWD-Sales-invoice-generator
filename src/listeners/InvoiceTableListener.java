/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package listeners;

import controller.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import model.FileOperations;
import view.View;

/**
 *
 * @author OmarTarek
 */
public class InvoiceTableListener implements ListSelectionListener
{
    private FileOperations fileOperations;
    private View view=null;
    private InvoicesLineTableListener invoicesLineTableListener;
    public InvoiceTableListener(View view,FileOperations fileOperations,InvoicesLineTableListener invoicesLineTableListener) 
    {
        this.view=view;
        this.fileOperations=fileOperations;
        this.invoicesLineTableListener=invoicesLineTableListener;
    }
    @Override
    public void valueChanged(ListSelectionEvent e) 
    {
        if(Controller.invoices.size()>=1)
        {
            //Dected the latest change
            if(!e.getValueIsAdjusting())
            {
                view.getDeleteItemButton().setEnabled(false);
                Controller.selectedRow=view.getInvoiceTable().getSelectedRow();
                view.getInvoicesLineTable().getSelectionModel().removeListSelectionListener(invoicesLineTableListener);
                LoadTablesContents.loadInvoicesLineTable(view, Controller.invoices);
                RightSideOperations.rightSideTextUpdater(view, Controller.invoices, Controller.selectedRow);
                view.getInvoicesLineTable().getSelectionModel().addListSelectionListener(invoicesLineTableListener);
            }
        }
    }
}
