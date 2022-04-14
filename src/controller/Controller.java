package controller;

import listeners.InvoicesLineTableListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import listeners.*;
import model.FileOperations;
import model.InvoiceHeader;
import model.InvoiceLine;
import view.View;

/**
 *
 * @author OmarTarek
 */
public class Controller implements ActionListener, KeyListener
{
    private  InvoiceHeader invoiceHeader;
    private  InvoiceLine   invoiceLine;
    private  View          view;
    private FileOperations fileOperations;
    private LoadTablesContents loadTablesContents;
    private CleanTablesContent cleanTablesContent;
    public volatile static ArrayList <InvoiceHeader> invoices= new ArrayList<>();
    public volatile static int selectedRow=0;
    public volatile static boolean isThereIsNotSavedEdit = false;
    public volatile static int maxNumberOfExistedInvoices=0;
    /*Listeners Objects*/
    private InvoiceTableListener invoiceTableListener;
    private InvoicesLineTableListener invoicesLineTableListener;
    private FileMenuItemsListener fileMenuItemsListener;
    private MainFrameWindowListener mainFrameWindowListener;
    private MainFrameMouseMotionListener mainFrameMouseMotionListener;
    private AddItemDialogWindowListener addItemDialogWindowListener;
    private InvoiceDateTextFieldListener invoiceDateTextFieldListener;
    private CustomerNameTextFieldListener customerNameTextFieldListener;
    public Controller(InvoiceHeader invoiceHeader,InvoiceLine invoiceLine, View view) 
    {
        this.invoiceHeader = invoiceHeader;
        this.invoiceLine = invoiceLine;
        this.view = view;
        fileOperations= new FileOperations(this.view);
        /*Listeners Initiation*/
        invoicesLineTableListener=new InvoicesLineTableListener(view);
        invoiceTableListener=new InvoiceTableListener(view,fileOperations,invoicesLineTableListener);
        fileMenuItemsListener=new FileMenuItemsListener(view,fileOperations,invoiceTableListener);
        mainFrameWindowListener=new MainFrameWindowListener(view,fileOperations,invoiceTableListener);
        addItemDialogWindowListener=new AddItemDialogWindowListener(view);
        mainFrameMouseMotionListener=new MainFrameMouseMotionListener(view);
        invoiceDateTextFieldListener=new InvoiceDateTextFieldListener(view);
        customerNameTextFieldListener=new CustomerNameTextFieldListener(view);
        turnOnAllActionListerners(view);
        loadTablesContents= new LoadTablesContents();
        cleanTablesContent= new CleanTablesContent();
    }
    private void turnOnAllActionListerners(View view) 
    {
        view.getLoadFile().addActionListener(fileMenuItemsListener);
        view.getLoadFile().setActionCommand("Load Fisle Sequence");
        view.getSaveFile().addActionListener(fileMenuItemsListener);
        view.getSaveFile().setActionCommand("Save File");
        view.getFileMenu().addMenuListener(fileMenuItemsListener);
        view.getInvoiceTable().getSelectionModel().addListSelectionListener(invoiceTableListener);
        view.getInvoicesLineTable().getSelectionModel().addListSelectionListener(invoicesLineTableListener);
        view.addWindowListener(mainFrameWindowListener);
        view.getAddItemDialog().addWindowListener(addItemDialogWindowListener);
        view.getInvoiceDateTextField().addActionListener(invoiceDateTextFieldListener);
        view.getInvoiceDateTextField().addFocusListener(invoiceDateTextFieldListener);
        view.getCustomerNameTextField().addActionListener(customerNameTextFieldListener);
        view.getCustomerNameTextField().addFocusListener(customerNameTextFieldListener);
        view.getRootPane().addMouseMotionListener(mainFrameMouseMotionListener);        
        view.getCreatNewInvoiceButton().addActionListener(this);
        view.getCreatNewInvoiceButton().setActionCommand("Creat New Invoice");
        view.getCreatNewInvoiceOK().addActionListener(this);
        view.getCreatNewInvoiceOK().setActionCommand("Creat New Invoice OK");
        view.getCreatNewInvoiceCancel().addActionListener(this);
        view.getCreatNewInvoiceCancel().setActionCommand("Creat New Invoice Cancel");
        view.getDeleteInvoiceButton().addActionListener(this);
        view.getDeleteInvoiceButton().setActionCommand("Delete Invoice");
        view.getAddItemButton().addActionListener(this);
        view.getAddItemButton().setActionCommand("Add Item");
        view.getNewItemPrice().addKeyListener(this);
        view.getAddItemDialogCancel().addActionListener(this);
        view.getAddItemDialogCancel().setActionCommand("Add Item Dialog Cancel");
        view.getAddItemDialogOK().addActionListener(this);
        view.getAddItemDialogOK().setActionCommand("Add Item Dialog OK");
        view.getDeleteItemButton().addActionListener(this);
        view.getDeleteItemButton().setActionCommand("Delete Item");
        view.getCancelButton().addActionListener(this);
        view.getCancelButton().setActionCommand("Cancel Any Changes");
    }
    /**
    *
    * Implement All Listeners
    * 
    */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch(e.getActionCommand())
        {
            case "Creat New Invoice":
            {
                if(view.getFocusOwner()!=null)
                LeftSideOperations.showCreatNewInvoiceDialog(view);
                break;
            }
            case "Creat New Invoice OK":
            {
                //disable list selection listener for a moment
                view.getInvoiceTable().getSelectionModel().removeListSelectionListener(invoiceTableListener);
                //add new invoice
                LeftSideOperations.addNewInvoice(view,invoices);
                //enable list selection listener
                view.getInvoiceTable().getSelectionModel().addListSelectionListener(invoiceTableListener);
                if(isThereIsNotSavedEdit)
                {
                    view.getCancelButton().setEnabled(isThereIsNotSavedEdit);
                }
                else
                {
                    view.getCancelButton().setEnabled(isThereIsNotSavedEdit);
                }
                break;
            }
            case "Creat New Invoice Cancel":
            {
                view.getNewCustomerName().setText("");
                View.getCreatNewInvoiceDialog().setVisible(false);
                break;
            }
            case "Delete Invoice":
            {
                if(view.getFocusOwner()!=null)
                {
                    //disable list selection listener for a moment
                    view.getInvoiceTable().getSelectionModel().removeListSelectionListener(invoiceTableListener);
                    //delete the selected invoice
                    LeftSideOperations.deleteSelectedInvoice(view,invoices);
                    //enable list selection listener
                    view.getInvoiceTable().getSelectionModel().addListSelectionListener(invoiceTableListener);
                    if(isThereIsNotSavedEdit)
                    {
                        view.getCancelButton().setEnabled(isThereIsNotSavedEdit);
                    }
                    else
                    {
                        view.getCancelButton().setEnabled(isThereIsNotSavedEdit);
                    }
                }
                break;
            }
            case "Add Item":
            {
                if(view.getFocusOwner()!=null)
                RightSideOperations.showCreatNewItemDialog(view);
                break;
            }
            case "Add Item Dialog OK":
            {
                RightSideOperations.AddNewItem(view,invoices);
                LeftSideOperations.calculateInvoiceTableTotal(invoices);
                LeftSideOperations.updateTableTotal(view, invoices);
                RightSideOperations.rightSideTextUpdater(view, invoices, selectedRow);
                LoadTablesContents.loadInvoicesLineTable(view, invoices);
                int sizeOfinvoicesLinesForTheSelectedInvoice=invoices.get(view.getInvoiceTable().getSelectedRow()).getInvoicerow().size();
                view.getInvoicesLineTable().setRowSelectionInterval((sizeOfinvoicesLinesForTheSelectedInvoice-1), (sizeOfinvoicesLinesForTheSelectedInvoice-1));
                View.getAddItemDialog().setVisible(false);
                if(isThereIsNotSavedEdit)
                {
                    view.getCancelButton().setEnabled(isThereIsNotSavedEdit);
                }
                else
                {
                    view.getCancelButton().setEnabled(isThereIsNotSavedEdit);
                }
                break;
            }
            case "Add Item Dialog Cancel":
            {
                View.getAddItemDialog().setVisible(false);
                view.getNewItemName().setText("");
                view.getNewItemPrice().setText("");
                view.getNewItemPriceSpinner().setValue((Object)1);
                break;
            }
            case "Delete Item":
            {
                if(view.getFocusOwner()!=null)
                {
                    RightSideOperations.DeleteItem(view,invoices);
                    LeftSideOperations.calculateInvoiceTableTotal(invoices);
                    LeftSideOperations.updateTableTotal(view, invoices);
                    RightSideOperations.rightSideTextUpdater(view, invoices, selectedRow);
                    LoadTablesContents.loadInvoicesLineTable(view, invoices);
                    int sizeOfinvoicesLinesForTheSelectedInvoice=invoices.get(view.getInvoiceTable().getSelectedRow()).getInvoicerow().size();
                    if(sizeOfinvoicesLinesForTheSelectedInvoice>0)
                    view.getInvoicesLineTable().setRowSelectionInterval((sizeOfinvoicesLinesForTheSelectedInvoice-1), (sizeOfinvoicesLinesForTheSelectedInvoice-1));
                    if(isThereIsNotSavedEdit)
                    {
                        view.getCancelButton().setEnabled(isThereIsNotSavedEdit);
                    }
                    else
                    {
                        view.getCancelButton().setEnabled(isThereIsNotSavedEdit);
                    }
                }
                break;
            }
            case "Cancel Any Changes":
            {
                //Reload CSV files into tables
                if((FileOperations.selectedInvoiceHeader!=null)&&(FileOperations.selectedInvoiceLine!=null))
                {
                    view.getInvoiceTable().getSelectionModel().removeListSelectionListener(invoiceTableListener);
                    invoices=fileOperations.readFile();
                    LeftSideOperations.calculateInvoiceTableTotal(invoices);
                    loadTablesContents.loadInvoicesHeaderTable(view,invoices);
                    maxNumberOfExistedInvoices=0;
                    fileOperations.getMaxNumberOfExistedInvoices(maxNumberOfExistedInvoices,invoices);
                    isThereIsNotSavedEdit=false;
                    view.getInvoiceTable().getSelectionModel().addListSelectionListener(invoiceTableListener);
                    if(invoices.size()>=1)
                    view.getInvoiceTable().setRowSelectionInterval(0, 0);
                }
                if(isThereIsNotSavedEdit)
                {
                    view.getCancelButton().setEnabled(isThereIsNotSavedEdit);
                }
                else
                {
                    view.getCancelButton().setEnabled(isThereIsNotSavedEdit);
                }
                break;
            }
        }
        
    }
    //validate new item price
    @Override
    public void keyTyped(KeyEvent evnt)
    {
        char price=evnt.getKeyChar();
        if(Character.isLetter(price)&&!evnt.isAltDown()&&evnt.isShiftDown()&&evnt.isControlDown())
        {
            evnt.consume();
        }
        if((price=='f')||(price=='d'))
                evnt.consume();
        try {
            Float.parseFloat(view.getNewItemPrice().getText()+price);
        } catch (Exception e) {
            evnt.consume();
        }
    }

    @Override
    public void keyPressed(KeyEvent e){}

    @Override
    public void keyReleased(KeyEvent e){}
}
