import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDateTime;  
import java.time.format.DateTimeFormatter;
import java.io.BufferedReader;
import java.io.FileOutputStream;
class Paradise{
    static Scanner sc=new Scanner(System.in);
    ArrayList<String>FoodMenu=new ArrayList<>();
    ArrayList<String>OrderCollection=new ArrayList<>();
    ArrayList<Integer>myOrder=new ArrayList<>();
    double billAmount=0.0;
    String formattedDate;
    void dataStore()
    {
        try {
            Scanner scan=new Scanner(new FileReader("orderData.csv"));
            Scanner scn=new Scanner(new FileReader("OrderRecord.csv"));
            while((scan.hasNext()))
            {
                String eachLine=scan.nextLine();
                FoodMenu.add(eachLine);
            }
            while((scn.hasNext()))
            {
                String eachLine=scn.nextLine();
                OrderCollection.add(eachLine);
            }
        } catch (Exception e) {
            System.out.println("Run time Error");
        }
    }
    void getDate(){
        LocalDateTime myDateObj = LocalDateTime.now();   
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-dd-MMM");  
        formattedDate = myDateObj.format(myFormatObj);
        // System.out.println(formattedDate); 
    }
    void OrderRegister(String inf)
    {
        getDate();
        String ref=OrderCollection.get(OrderCollection.size()-1);
        String add=ref.substring(0,ref.indexOf(","));
        // System.out.println(add);

        int id=Integer.parseInt(add)+1;
        String reg="\n"+String.valueOf(id)+","+formattedDate+","+billAmount+",";
        for(int i:myOrder)reg=reg+i+" ";
        reg=reg+","+inf;
        try {
            FileOutputStream fo=new FileOutputStream("OrderRecord.csv",true);
            byte[] text=reg.getBytes();
            fo.write(text);
            fo.close();
            //updating local data stored in arraylist after adding new order.
            dataStore();
            GotoMainMenu();
        } catch (Exception e) {
            System.out.println("error");
        }
    }
    void TakeOrder(){
        System.out.println("Enter number of order items:");
        int itemCount=sc.nextInt();
        for(int i=0;i<itemCount;i++)
        {
            System.out.println("Enter FoodItem Id:");
            int item=sc.nextInt();
            myOrder.add(item);
            System.out.println("Enter quantity of FoodItem:");
            int qnty=sc.nextInt();
            myOrder.add(qnty);
        }
        System.out.println("Press y to make another order");
        char ch=sc.next().charAt(0);
        if(ch=='y')
        {
            TakeOrder();
        }
        else{
            System.out.println("Your order details:");
            System.out.println("____________________");
            for(int i=0;i<myOrder.size();i=i+2)
            {
                String check=FoodMenu.get(myOrder.get(i)-1);
                String td[]=check.split(",");
                billAmount+=Double.parseDouble(td[2]);
                System.out.println("FoodItem Name: "+td[1]+" | "+" Price: "+td[2]+" | "+"Quantity: "+myOrder.get(i+1));
            }
        }
        System.out.println("Press y to confirm Order");
        char cnf=sc.next().charAt(0);
        if(cnf=='y')
        {
            OrderRegister("Approved");
        }
        else{
            OrderRegister("Cancelled");
        }

    }
    void GotoMainMenu(){
        System.out.println("Press Y to Go to Main Menu"); 
        char op=sc.next().charAt(0);
        if(op=='y')
        {
            Display();
        }
    }
    void edit()
    {
        System.out.println("Enter Order Id Number to edit: ");
        int orderID=sc.nextInt();
        try {
            BufferedReader br=new BufferedReader(new FileReader("OrderRecord.csv"));
            String line="";
            String oldtext="";
            String newtext="";
            while((line=br.readLine())!=null)
            {
                String check=line.substring(0,line.indexOf(","));
                String status=line.substring(line.lastIndexOf(",")+1);
                if(check.contentEquals(String.valueOf(orderID)))
                {
                    if(status.contentEquals("Approved"))
                    {
                        System.out.println(line.substring(0,line.lastIndexOf(",")+1)+"Cancelled");
                        newtext+=line.substring(0,line.lastIndexOf(",")+1)+"Cancelled"+"\n";
                    }
                    else if(status.contentEquals("Cancelled"))
                    {
                        System.out.println(line.substring(0,line.lastIndexOf(",")+1)+"Approved");
                        newtext+=line.substring(0,line.lastIndexOf(",")+1)+"Approved"+"\n";
                    }
                }
                else
                {
                    newtext+=line+"\n";
                    oldtext+=line+"\n";
                }
            }
            String newcontent=oldtext.replace(oldtext, newtext);
            FileWriter writer=new FileWriter("OrderRecord.csv");
            writer.write(newcontent);
            writer.close();
            br.close();
            dataStore();
            GotoMainMenu();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    void DaywiseCollection(){
        System.out.println("Enter date to see the collection:");
        sc.nextLine();
        String str=sc.nextLine();
        double billpay=0.0;
        int k=0;
        for(String i:OrderCollection)
        {
            String dp[]=i.split(",");
            double cd=Double.parseDouble(dp[2]);
            if(str.equals(dp[1]))
            {
                k++;
                billpay+=cd;
                System.out.println(i);
            }
        }
        if(k==0)
        {
            System.out.println("No such data is found in our Records.Kindly enter valid details.");
        }
        else{
            System.out.println("Todays total collection: "+billpay);
        }
        GotoMainMenu();
    }
    void Display()
    {
        System.out.println("WELCOME TO PARADISE FOOD RESTRAUNT VIZAG");
        System.out.println("---------------------------------------");
        System.out.println("Kindly Select Your Choice:");
        System.out.println("=======================================");
        System.out.println("+--MAIN MENU--+");
        System.out.println("1.Enter New Order.\n2.Edit Bill Status.\n3.See Collections of a day.");
        int ch=sc.nextInt();
        if(ch==1)
        {
            TakeOrder();
        }
        else if(ch==2)
        {
            edit();
        }
        else{
            DaywiseCollection();
        }

    }
}
public class RestrauntAppGradedAssgnmt {
    public static void main(String[] args) {
        Paradise rst=new Paradise();
        rst.dataStore();
        // rst.OrderRegister();
        rst.Display();
    }
}
