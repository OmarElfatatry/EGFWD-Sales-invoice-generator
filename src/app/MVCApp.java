package app;

import controller.Controller;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import model.InvoiceHeader;
import model.InvoiceLine;
import view.View;

/**
 *
 * @author OmarTarek
 */
public class MVCApp 
{
    public static void main(String[] args) 
    {
        ArrayList <InvoiceHeader> invoices= new ArrayList<>();
        View          view= new View();
        view.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
        view.setVisible(true);
        view.setLocations();
        Controller c= new Controller(invoices, view);
    }
}
