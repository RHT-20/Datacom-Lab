package lab_5_server;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class Lab_5_Server 
{
    public static void main(String[] args) throws IOException 
    {
        ServerSocket ss = new ServerSocket(5555);
        Socket s = ss.accept();
        DataInputStream din = new DataInputStream(s.getInputStream());
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());

        FileWriter fout = new FileWriter("/home/rht/Documents/Code/DataCom/RHT/Lab_5_Server/out.txt");
        String str = "",out = "",msg = "";
        int frame=0,k=0;
        Random rand = new Random();
        
        while(true)
        {
            str = din.readUTF();
            //System.out.println(str);
            if(str.equals("Stop"))  break;
            
            String[] temp = str.split("#");
            
            int r = rand.nextInt(9);
            
            int i=1;
            frame = 0;
            
            while(true)
            {
                if(r==0)    break;
                
                msg = temp[i];
                r--;
                i++;
                frame++;
                
                //System.out.println(msg);
                
                while(msg.charAt(0)>='0' && msg.charAt(0)<='7')
                {
                    msg = msg.substring(1);
                }
                
                out = "";   
                for(int j=0; j<msg.length(); j++)
                {
                    if(msg.charAt(j)=='*')  out += "\r\n";
                    else    out += msg.charAt(j);
                }
                fout.write(out);
            }
            
            String send;
            send = Integer.toString(frame);
            if(frame<8) System.out.println("Acknowledgment " + send);
            else    System.out.println("Time out.");
            dout.writeUTF(send);
        }
        str = din.readUTF();
        String[] temp = str.split("#");
        out = "";
        for(int i=1; i<temp.length; i++)
        {
            msg = temp[i];
            while(msg.charAt(0)>='0' && msg.charAt(0)<='7')
            {
                msg = msg.substring(1);
            }   
            for(int j=0; j<msg.length(); j++)
            {
                if(msg.charAt(j)=='*')  out += "\r\n";
                else    out += msg.charAt(j);
            }
        }
        out += ".";
        fout.write(out);
        fout.close();
    }
}