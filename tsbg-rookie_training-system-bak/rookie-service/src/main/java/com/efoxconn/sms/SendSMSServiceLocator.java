/**
 * SendSMSServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.efoxconn.sms;

public class SendSMSServiceLocator extends org.apache.axis.client.Service implements SendSMSService {

    /**
     * 調用WebService發送短信
     */

    public SendSMSServiceLocator() {
    }


    public SendSMSServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SendSMSServiceLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SendSMSServiceSoap
    private String SendSMSServiceSoap_address = "http://58.251.167.199:8888/SendSMSService.asmx";

    public String getSendSMSServiceSoapAddress() {
        return SendSMSServiceSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private String SendSMSServiceSoapWSDDServiceName = "SendSMSServiceSoap";

    public String getSendSMSServiceSoapWSDDServiceName() {
        return SendSMSServiceSoapWSDDServiceName;
    }

    public void setSendSMSServiceSoapWSDDServiceName(String name) {
        SendSMSServiceSoapWSDDServiceName = name;
    }

    public SendSMSServiceSoap getSendSMSServiceSoap() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SendSMSServiceSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSendSMSServiceSoap(endpoint);
    }

    public SendSMSServiceSoap getSendSMSServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            SendSMSServiceSoapStub _stub = new SendSMSServiceSoapStub(portAddress, this);
            _stub.setPortName(getSendSMSServiceSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSendSMSServiceSoapEndpointAddress(String address) {
        SendSMSServiceSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (SendSMSServiceSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                SendSMSServiceSoapStub _stub = new SendSMSServiceSoapStub(new java.net.URL(SendSMSServiceSoap_address), this);
                _stub.setPortName(getSendSMSServiceSoapWSDDServiceName());
                return _stub;
            }
        }
        catch (Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        String inputPortName = portName.getLocalPart();
        if ("SendSMSServiceSoap".equals(inputPortName)) {
            return getSendSMSServiceSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://sms.efoxconn.com", "SendSMSService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://sms.efoxconn.com", "SendSMSServiceSoap"));
        }
        return ports.iterator();
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {

        if ("SendSMSServiceSoap".equals(portName)) {
            setSendSMSServiceSoapEndpointAddress(address);
        }
        else
        { // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(javax.xml.namespace.QName portName, String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
