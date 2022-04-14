/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package listeners;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import view.View;
import controller.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.FileOperations;
/**
 *
 * @author OmarTarek
 */
public class CustomerNameTextFieldListener implements FocusListener, ActionListener
{
    private View view=null;
    
    public CustomerNameTextFieldListener(View view)
    {
        this.view=view;
    }

    @Override
    public void focusLost(FocusEvent e)
    {
        if(!((Controller.invoices.get(Controller.selectedRow).getInoviceCustomerName()).equals((view.getCustomerNameTextField().getText()))))
        {
            RightSideOperations.changeCustomerNameTextField(view,Controller.invoices);
            view.getInvoiceTable().setRowSelectionInterval(Controller.selectedRow, Controller.selectedRow);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(!((Controller.invoices.get(Controller.selectedRow).getInoviceCustomerName()).equals((view.getCustomerNameTextField().getText()))))
        {
            RightSideOperations.changeCustomerNameTextField(view,Controller.invoices);
        }
    }
    
    @Override
    public void focusGained(FocusEvent e){}

   
}
