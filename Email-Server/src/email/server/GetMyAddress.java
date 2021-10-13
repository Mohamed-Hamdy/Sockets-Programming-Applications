/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package email.server;

/**
 *
 * @author Mohamed_El_Laithy
 */
import java.net.InetAddress;

public class GetMyAddress
{
    public static void main (String[] args)
    {
	try
	{
	    InetAddress me = InetAddress.getLocalHost ();
	    System.out.println("--------------------------------------------");
	    System.out.println("Address        : " + me.getHostAddress());
	    System.out.println("Name           : " + me.getHostName());
	    //System.out.println("Canonical name : " + me.getCanonicalHostName());
	    System.out.println("User           : " + System.getProperty("user.name"));
	    System.out.println("Home directory : " + System.getProperty("user.home"));
	    System.out.println("Current        : " + System.getProperty("user.dir"));
	    System.out.println("OS name        : " + System.getProperty("os.name"));
	    System.out.println("OS version     : " + System.getProperty("os.version"));
	    System.out.println("OS architecture: " + System.getProperty("os.arch"));
	    System.out.println("Java class path: " + System.getProperty("java.class.path"));
	    System.out.println("--------------------------------------------");
	}
	catch (Exception e)
	{
	    e.printStackTrace ();
	}
    }
}
