/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package listeners;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import view.View;

/**
 *
 * @author OmarTarek
 */
public class InvoicesLineTableListener implements ListSelectionListener
{
    private View view=null;
    public InvoicesLineTableListener(View view) 
    {
        this.view=view;
    }

    
    @Override
    public void valueChanged(ListSelectionEvent e) 
    {
            //Dected the latest change
            if(!e.getValueIsAdjusting())
            {
                if(view.getInvoicesLineTable().getSelectedRow()>=0)
                {
                    view.getDeleteItemButton().setEnabled(true);
                }
                else
                {
                    view.getDeleteItemButton().setEnabled(false);
                }
            }
        }
}
