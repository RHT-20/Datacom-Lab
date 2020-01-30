package lab_5_client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Lab_5_Client 
{
    public static void main(String[] args) throws IOException 
    {
        Socket s = new Socket ("localhost",5555);
        DataInputStream din = new DataInputStream(s.getInputStream());
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
        
        FileReader fin = new FileReader("/home/rht/Documents/Code/DataCom/RHT/Lab_5_Client/in.txt");
        Scanner sc = new Scanner(fin);
        
        String inputTxt = "",msg = "";
        int frame=0,from=0;
        String[] str = new String[10];
        String reply = "",sstr = "";
        boolean flag = true,f = false;
        ArrayList<String> arrayListstr = new ArrayList<>();
        
        while(sc.hasNext())
        {
            if(f)   inputTxt += "*";
            inputTxt += sc.nextLine();
            f = true;
        }
        //System.out.println(inputTxt);
        
        for(int i=0; i<inputTxt.length(); i++)
        {
            sstr += inputTxt.charAt(i);
            if(i%8==0 && i!=0)
            {
                arrayListstr.add(sstr);
                sstr = "";
            }
        }
        //System.out.println(arrayListstr.size());
        
        int i;
        for(i=0; i<arrayListstr.size(); )
        {
            if(frame==8)
            {
                //System.out.println(msg);
                dout.writeUTF(msg);
                long pre_time = System.currentTimeMillis();
                f = true;
                while(Math.abs(System.currentTimeMillis()-pre_time)<1000)
                {
                    reply = din.readUTF();
                    if(reply!=null)
                    {
                        from = Integer.parseInt(reply);
                        if(from<8)
                        {
                            i -= (8-from);
                            System.out.println("Acknowledgment " + reply);
                            f = false;
                        }
                        frame = 0;
                        break;
                    }
                }
                if(f)  System.out.println("Time out");
                msg = "";
            }
            
            msg += "#";
            msg += Integer.toString(frame);
            msg += arrayListstr.get(i);
            
            if(i==(arrayListstr.size()-1))  
            {
                dout.writeUTF("Stop");
                dout.writeUTF(msg);
                break;
            }
            
            i++;
            frame++;
        }
        dout.writeUTF("Stop");
    }
}
