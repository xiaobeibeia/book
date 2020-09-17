/**
 * SendSMSService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.efoxconn.sms;

public interface SendSMSService extends javax.xml.rpc.Service {

    /**
     * 調用WebService發送短信
     */
    public String getSendSMSServiceSoapAddress();

    public SendSMSServiceSoap getSendSMSServiceSoap() throws javax.xml.rpc.ServiceException;

    public SendSMSServiceSoap getSendSMSServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
