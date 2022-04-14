/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package listeners;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import view.View;

/**
 *
 * @author OmarTarek
 */
public class AddItemDialogWindowListener implements WindowListener
{
    private View view=null;
    public AddItemDialogWindowListener(View view)
    {
        this.view=view;
    }
    @Override
    public void windowClosed(WindowEvent e)
    {
        //clear all text field of add new item dialog and reset spinner
        view.getNewItemName().setText("");
        view.getNewItemPrice().setText("");
        view.getNewItemPriceSpinner().setValue((Object)1);
    }
    @Override
    public void windowOpened(WindowEvent e){}

    @Override
    public void windowClosing(WindowEvent e){}
    
    @Override
    public void windowIconified(WindowEvent e){}

    @Override
    public void windowDeiconified(WindowEvent e){}

    @Override
    public void windowActivated(WindowEvent e){}

    @Override
    public void windowDeactivated(WindowEvent e){}
    
}
