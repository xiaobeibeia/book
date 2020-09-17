/**
 * SendSMSServiceSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.efoxconn.sms;

public interface SendSMSServiceSoap extends java.rmi.Remote {

    /**
     * 發送任意短信
     */
    public String sendSMS(String userName, String password, String phone, String content) throws java.rmi.RemoteException;

    /**
     * 發送固定短信
     */
    public String sendFormatSMS(String userName, String password, String phone, int formatID, int spaceNum, String content) throws java.rmi.RemoteException;
}
