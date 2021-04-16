package com.bird.cas.client.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @program: ce-sso
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2020-05-08 17:40
 **/
public class IP {
//    public static String  getServerIp(){
//        String serverIp = "";
//        try {
//            Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
//            InetAddress ip = null;
//            while (netInterfaces.hasMoreElements()) {
//                NetworkInterface ni = (NetworkInterface) netInterfaces
//                        .nextElement();
//                ip = (InetAddress) ni.getInetAddresses().nextElement();
//                serverIp = ip.getHostAddress();
//                if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
//                        && ip.getHostAddress().indexOf(":") == -1) {
//                    serverIp = ip.getHostAddress();
//                    break;
//                } else {
//                    ip = null;
//                }
//            }
//        } catch (SocketException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        return serverIp;
//    }


    public static String getLocalIP(){
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
           // e.printStackTrace();
        }

        byte[] ipAddr = addr.getAddress();
        String ipAddrStr = "";
        for (int i = 0; i < ipAddr.length; i++) {
            if (i > 0) {
                ipAddrStr += ".";
            }
            ipAddrStr += ipAddr[i] & 0xFF;
        }
        //System.out.println(ipAddrStr);
        return ipAddrStr;
    }


    public static void main(String[] args) {
        System.out.println(getLocalIP());
    }
}
