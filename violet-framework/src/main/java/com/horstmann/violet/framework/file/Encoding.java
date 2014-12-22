package com.horstmann.violet.framework.file;

import java.io.UnsupportedEncodingException;

public class Encoding {
    public static void main(String[] args) throws UnsupportedEncodingException {
//       String Cdata = "MARIE-HÉLÈNE";
//       byte sByte[] = Cdata.getBytes("UTF-8"); 
//       Cdata = new String(sByte,"UTF-8");
//       System.out.println(Cdata);
       
       
       String Cdata="MARIE-HÉLÈNE";
       byte sByte[]=Cdata.getBytes(); 
       Cdata= new String(sByte,"UTF-8");
       System.out.println(Cdata);
    }
 }