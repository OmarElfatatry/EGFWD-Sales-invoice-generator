package app;

import controller.Controller;
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
        InvoiceHeader invoiceHeader=new InvoiceHeader();
        InvoiceLine   invoiceLine=new InvoiceLine();
        View          view= new View();
        view.setVisible(true);
        view.setLocations();
        Controller c= new Controller(invoiceHeader, invoiceLine, view);
    }
}
