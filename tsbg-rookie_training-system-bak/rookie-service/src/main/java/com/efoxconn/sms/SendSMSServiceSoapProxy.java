package com.efoxconn.sms;

public class SendSMSServiceSoapProxy implements SendSMSServiceSoap {
    private String _endpoint = null;
    private SendSMSServiceSoap sendSMSServiceSoap = null;

    public SendSMSServiceSoapProxy() {
        _initSendSMSServiceSoapProxy();
    }

    public SendSMSServiceSoapProxy(String endpoint) {
        _endpoint = endpoint;
        _initSendSMSServiceSoapProxy();
    }

    private void _initSendSMSServiceSoapProxy() {
        try {
            sendSMSServiceSoap = (new SendSMSServiceLocator()).getSendSMSServiceSoap();
            if (sendSMSServiceSoap != null) {
                if (_endpoint != null)
                    ((javax.xml.rpc.Stub)sendSMSServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
                else
                    _endpoint = (String)((javax.xml.rpc.Stub)sendSMSServiceSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
            }

        }
        catch (javax.xml.rpc.ServiceException serviceException) {}
    }

    public String getEndpoint() {
        return _endpoint;
    }

    public void setEndpoint(String endpoint) {
        _endpoint = endpoint;
        if (sendSMSServiceSoap != null)
            ((javax.xml.rpc.Stub)sendSMSServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);

    }

    public SendSMSServiceSoap getSendSMSServiceSoap() {
        if (sendSMSServiceSoap == null)
            _initSendSMSServiceSoapProxy();
        return sendSMSServiceSoap;
    }

    public String sendSMS(String userName, String password, String phone, String content) throws java.rmi.RemoteException{
        if(sendSMSServiceSoap != null){
            return sendSMSServiceSoap.sendSMS(userName, password, phone, content);
        }
        _initSendSMSServiceSoapProxy();
        return null;

        /*if (sendSMSServiceSoap == null) {
            _initSendSMSServiceSoapProxy();
        }
        return sendSMSServiceSoap.sendSMS(userName, password, phone, content);*/
    }

    public String sendFormatSMS(String userName, String password, String phone, int formatID, int spaceNum, String content) throws java.rmi.RemoteException{
        if(sendSMSServiceSoap != null){
            return sendSMSServiceSoap.sendFormatSMS(userName, password, phone, formatID, spaceNum, content);
        }
        _initSendSMSServiceSoapProxy();
        return null;

        /*if (sendSMSServiceSoap == null){
            _initSendSMSServiceSoapProxy();
        }
        return sendSMSServiceSoap.sendFormatSMS(userName, password, phone, formatID, spaceNum, content);*/
    }


}
