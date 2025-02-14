/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.proximax.kyc.service.util;
import java.net.InetAddress;
import java.net.UnknownHostException;
/**
 *
 * @author vantran
 */
public class NetUtility {
    public static long ipToLong(InetAddress ip) 
    {
        byte[] octets = ip.getAddress();
        long result = 0;
        for (byte octet : octets) {
                                result <<= 8;
                                result |= octet & 0xff;
                        }
        return result;
    }
    public static boolean isValidRange(String ipStart, String ipEnd,
                                       String ipToCheck) 
    {
        try {
            long ipLo = ipToLong(InetAddress.getByName(ipStart));
            long ipHi = ipToLong(InetAddress.getByName(ipEnd));
            long ipToTest = ipToLong(InetAddress.getByName(ipToCheck));
            return (ipToTest >= ipLo && ipToTest <= ipHi);                       
        } catch (UnknownHostException e) 
        {
            e.printStackTrace();
            return false;
        }
    }
}
