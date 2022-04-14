package model;

import controller.Controller;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import view.View;

/**
 *
 * @author OmarTarek
 */
public class FileOperations 
{
    /*Used To Store Files Paths*/
    public static File selectedInvoiceHeader=null;
    public static File selectedInvoiceLine=null;
    //Date format
    public static SimpleDateFormat date=new SimpleDateFormat("dd-MM-yyyy");
    private  View view=null;
    public FileOperations(View view) 
    {
        this.view=view;
    }
    public void getFilesPaths()
    {
        /*Reset Files Paths*/
        selectedInvoiceHeader=null;
        selectedInvoiceLine=null;
        //Flag to check extension of selected file
        boolean extensionFlag = false;
        //Flag to check if "Cancel" button clicked
        boolean cancelButton = false;
        //To chekc which button user clicked on Jfile chooser
        int clicked;
        //String to store selected file name to check it's extension 
        String fileName=null;
        //String to strre file name extension
        String fileExtension=null;
        //Configure file filter to restrict selection of csv file only
        FileNameExtensionFilter filter=new FileNameExtensionFilter("CSV Files","csv");
        //Set the defult path when JFileChooser opens
        view.getOpenFileJFileChooser().setCurrentDirectory(new File(System.getProperty("user.dir")));

        /*************************************************Load Invoice Header File*************************************************/
        
        //Tell user to select "Invoice Header" file first
        view.setJOptionPaneMessagMessage(view,"Kindly select Invoice Header File","Invoice Header Selection","WARNING_MESSAGE");
        do{
            //Reset file name
            view.getOpenFileJFileChooser().setSelectedFile(new File(""));
            //Reset & set file filter
            view.getOpenFileJFileChooser().resetChoosableFileFilters();
            view.getOpenFileJFileChooser().setFileFilter(filter);
            //Set JFileChooser title
            view.getOpenFileJFileChooser().setDialogTitle("Open Invoice Header File");
            clicked= view.getOpenFileJFileChooser().showOpenDialog(view);
            if(clicked == view.getOpenFileJFileChooser().APPROVE_OPTION) 
            {
                //invoices.clear();
                cancelButton=false;
                //Fetch file name
                fileName= view.getOpenFileJFileChooser().getSelectedFile().getName();
                //Fetch file extension
                fileExtension = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
                //Validate file extension
                if(fileExtension.equalsIgnoreCase("csv"))
                {
                    extensionFlag=false; //correct file extension
                    selectedInvoiceHeader=view.getOpenFileJFileChooser().getSelectedFile();
                }
                else
                {
                    extensionFlag=true; //wrong file extension
                    view.setJOptionPaneMessagMessage(view,"Error: Load File With Extension .CSV Only","Wrong File Selected","ERROR_MESSAGE");
                }
            }
            else
            {
                cancelButton=true;
            }
        }
        while((extensionFlag == true)&& (cancelButton==false));
        
        /*************************************************Load Invoice Line File*************************************************/
        //Reset extension flag
        extensionFlag=true;
        if(selectedInvoiceHeader!=null)
        {
            //Tell user to select "Invoice Line" file second
            view.setJOptionPaneMessagMessage(view,"Kindly select Invoice Line File","Invoice Line Selection","WARNING_MESSAGE");
        }
        while ((extensionFlag == true)&& (cancelButton==false))
        {
            //Reset file name
            view.getOpenFileJFileChooser().setSelectedFile(new File(""));
            //Reset and set file filter
            view.getOpenFileJFileChooser().resetChoosableFileFilters();
            view.getOpenFileJFileChooser().setFileFilter(filter);
            //Set JFileChooser title
            view.getOpenFileJFileChooser().setDialogTitle("Open Invoice Line File");
            //Open JFileChooser to select file
            clicked= view.getOpenFileJFileChooser().showOpenDialog(view);
            if(clicked == view.getOpenFileJFileChooser().APPROVE_OPTION) 
            {
                //invoices.clear();
                cancelButton=false;
                //Fetch file name
                fileName= view.getOpenFileJFileChooser().getSelectedFile().getName();
                //Fetch file extension
                fileExtension = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
                //Validate file extension
                if(fileExtension.equalsIgnoreCase("csv"))
                {
                    extensionFlag=false; //correct file extension
                    selectedInvoiceLine=view.getOpenFileJFileChooser().getSelectedFile();
                }
                else
                {
                    extensionFlag=true; //wrong file extension
                    view.setJOptionPaneMessagMessage(view,"Error: Load File With Extension .CSV Only","Wrong File Selected","ERROR_MESSAGE");
                }
            }
            else
            {
                cancelButton=true;
            }
        }
        
    }
    public ArrayList<InvoiceHeader> readFile()
    {
        //Line string to store each line of csv file to proccess it
        String Line=null;
        //String array to store line parts of "Invoice Header" file using split function
        String []invoicesHeader=null;
        //String to temporarily store invoice number from csv file
        String invoiceNumberStr=null;
        //String to temporarily store invoice date from csv file
        String invoiceDateStr= null;
        //String to temporarily store customer name from csv file
        String invoiceCustomerName= null;
        //Integer to temporarily store invoice number
        int invoiceNumber=0;
        //Date Object to temporarily store invoice number
        Date invoiceDate=null;
        //Invoice Header Object to hold each header before add it to the main array list
        InvoiceHeader header=null;
        //Invoice Line Object to hold each line before add it to the main array list
        InvoiceLine line=null;
        //String array to store line parts of "Invoice Line" file using split function
        String []invoicesLine=null;
        //String to temporarily store each item name
        String invoiceItemName=null;
        //String to temporarily store each item price as a string
        String itemPriceStr=null;
        //String to temporarily store each item count as a string
        String itemCountStr=null;
        //Float variable to temporarily store each item price as a float
        float itemPrice=0;
        //Integer variable to temporarily store each item count as a integer
        int itemCount=0;
        //InvoiceHeader temporary Object to store parent header of each invoice line
        InvoiceHeader temporary=null;
        //Array list to store invoice header lines
        ArrayList <InvoiceHeader> invoices= null;
       /*Start Reading Invoice Header File*/
        try
        {
        if((selectedInvoiceHeader!=null)&&(selectedInvoiceLine!=null))
        {
            invoices= new ArrayList<InvoiceHeader>();
            FileReader file= new FileReader(selectedInvoiceHeader);
                BufferedReader B = new BufferedReader(file);
                while((Line = B.readLine())!= null)
                {
                    //Split each line by "," and store it in temporary string array
                    invoicesHeader = Line.split(",");
                    if(invoicesHeader.length!=3)
                    throw new Exception("Wrong File Internal Format");
                    //Assign each element of the temporary string array to it's corssponding variable
                    invoiceNumberStr = invoicesHeader[0];
                    if(!(invoiceNumberStr.matches("^\\d+$")))
                    throw new Exception("Wrong File Internal Format");
                    invoiceDateStr = invoicesHeader[1];
                    if(!(invoiceDateStr.matches("^\\d{2}\\-\\d{2}\\-\\d{4}$")))
                    throw new Exception("Wrong File Internal Format");
                    invoiceCustomerName = invoicesHeader[2];
                    view.getDate().setLenient(false);
                    invoiceDate = view.getDate().parse(invoiceDateStr);
                    invoiceNumber = Integer.parseInt(invoiceNumberStr); 
                    header=new InvoiceHeader(invoiceNumber, invoiceDate, invoiceCustomerName);
                    invoices.add(header); 
                }
                B.close();
                file.close();
        }
        }
        catch(Exception e)
        {
            selectedInvoiceHeader=null;
            selectedInvoiceLine=null;
            invoices.clear();
            view.getCreatNewInvoiceButton().setEnabled(false);
            if(e.toString().contains("Wrong File Internal Format"))
            {
                view.setJOptionPaneMessagMessage(view, "Wrong File Internal Format", "Error", "ERROR_MESSAGE");
            }
            if(e.toString().contains("Unparseable date"))
            {
                view.setJOptionPaneMessagMessage(view, "Wrong Date", "Error", "ERROR_MESSAGE");
            }
        }
        /*Start Reading Invoice Line File*/
        try
        {
        if((selectedInvoiceHeader!=null)&&(selectedInvoiceLine!=null))
        {
            FileReader file= new FileReader(selectedInvoiceLine);
                BufferedReader B = new BufferedReader(file);
                while((Line = B.readLine())!= null)
                {
                    //Split each line by "," and store it in temporary string array
                    invoicesLine = Line.split(",");
                    if(invoicesLine.length!=4)
                    throw new Exception("Wrong File Internal Format");
                    //Assign each element of the temporary string array to it's corssponding variable
                    invoiceNumberStr = invoicesLine[0];
                    if(!(invoiceNumberStr.matches("^\\d+$")))
                    throw new Exception("Wrong File Internal Format");
                    invoiceItemName = invoicesLine[1];
                    itemPriceStr = invoicesLine[2];
                    itemCountStr = invoicesLine[3];
                    if((!(itemCountStr.matches("^\\d+$")))||(Integer.parseInt(itemCountStr)<1))
                    throw new Exception("Wrong File Internal Format");
                    invoiceNumber = Integer.parseInt(invoiceNumberStr);
                    itemPrice=Float.parseFloat(itemPriceStr);
                    itemCount=Integer.parseInt(itemCountStr);
                    //Get parent header of this invoice line
                    temporary = findParentHeader(invoiceNumber,invoices);
                    line= new InvoiceLine(invoiceItemName, itemPrice, itemCount, temporary);
                    //Store this child line in crossponding Invoice Header
                    temporary.getInvoicerow().add(line);
                }
                B.close();
                file.close();
        }
        }
        catch(Exception e)
        {
            selectedInvoiceHeader=null;
            selectedInvoiceLine=null;
            invoices.clear();
            view.getCreatNewInvoiceButton().setEnabled(false);
            view.setJOptionPaneMessagMessage(view, "Wrong File Internal Format", "Error", "ERROR_MESSAGE");
        }
        return invoices;
    }
    public void writeFile(ArrayList<InvoiceHeader> invoices)
    {
        Controller.isThereIsNotSavedEdit=false;
        /*########################### Save Invoices Header File ###########################*/
        int InvoiceLinelines=0;
        int totalInvoiceLinelines=0;
        int actualLine=0;
        FileWriter fileWriter=null;
        try
        {
            fileWriter= new FileWriter(selectedInvoiceHeader);
        }catch(Exception e)
        {}
        for(int i=0;i<invoices.size();i++)
        {
            String Line= invoices.get(i).getInoviceNumber()+","+date.format(invoices.get(i).getInoviceDate())+","+invoices.get(i).getInoviceCustomerName();
            if(i!=invoices.size()-1)
                Line+="\n";
            try
               {
                  fileWriter.write(Line);
               }catch(Exception e)
               {}
        }
        try
         {
            fileWriter.close();
         }catch(Exception e)
         {}
        /*########################### Save Invoices Lines File ###########################*/
        try
        {
            fileWriter= new FileWriter(selectedInvoiceLine);
        }catch(Exception e)
        {}
        for(int i=0;i<invoices.size();i++)
        {
            totalInvoiceLinelines+=invoices.get(i).getInvoicerow().size();
        }
        for(int i=0;i<invoices.size();i++)
        {
            InvoiceLinelines=invoices.get(i).getInvoicerow().size();
            for(int j=0;j<InvoiceLinelines;j++)
            {
                String Line= Integer.toString(invoices.get(i).getInvoicerow().get(j).getMainInvoice().getInoviceNumber())+",";
                Line+=invoices.get(i).getInvoicerow().get(j).getItemName()+",";
                Line+=Float.toString(invoices.get(i).getInvoicerow().get(j).getItemPrice())+",";
                Line+=Integer.toString(invoices.get(i).getInvoicerow().get(j).getItemCount());
                actualLine++;

                if(!(totalInvoiceLinelines == actualLine))
                {
                     Line+="\n";
                }
                else
                {}
                try
                {
                    fileWriter.write(Line);
                }catch(Exception e)
                {}
            }
        }
        view.setJOptionPaneMessagMessage(view, "The old files were overwritten by the new data", "Save Done", "INFORMATION_MESSAGE");
        try
        {
           fileWriter.close();
        }catch(Exception e)
        {}
    }
    public void testMain(ArrayList<InvoiceHeader> invoices)
    {
        if((selectedInvoiceHeader!=null)&&(selectedInvoiceLine!=null))
        {
            for(int i=0;i<invoices.size();i++)
            {
                System.out.println("Invoice"+invoices.get(i).getInoviceNumber()+"Num");
                System.out.println("{");
                System.out.println(invoices.get(i).getInoviceDate()+", "+invoices.get(i).getInoviceCustomerName());
                for(int j=0;j<invoices.get(i).getInvoicerow().size();j++)
                {
                    System.out.println(invoices.get(i).getInvoicerow().get(j).getItemName()+", "+
                            invoices.get(i).getInvoicerow().get(j).getItemPrice()+", "+
                            invoices.get(i).getInvoicerow().get(j).getItemCount());
                }
                System.out.println("}");
            }
        }
    }

    private InvoiceHeader findParentHeader(int invoiceNumber, ArrayList<InvoiceHeader> invoices) 
    {
        InvoiceHeader returnElement=null;
        for(int i=0;i<invoices.size();i++)
        {
            if(invoices.get(i).getInoviceNumber() == invoiceNumber)
                returnElement= invoices.get(i);                
        }
        return returnElement;
    }

    public void getMaxNumberOfExistedInvoices(int maxNumberOfExistedInvoices, ArrayList<InvoiceHeader> invoices) 
    {
        for(int i=0;i<invoices.size();i++)
        {
            if((invoices.get(i).getInoviceNumber())>Controller.maxNumberOfExistedInvoices)
            {
                Controller.maxNumberOfExistedInvoices=invoices.get(i).getInoviceNumber();
            }
        }
    }
}
