package com.custom.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.ws.Holder;

import com.mirth.connect.connectors.ws.AcceptMessage;
import com.mirth.connect.connectors.ws.WebServiceReceiver;
 
@WebService
public class CustomAcceptMessage extends AcceptMessage {
  
    public CustomAcceptMessage(WebServiceReceiver webServiceReceiver) {
        super(webServiceReceiver);
    }
  
    @WebMethod(action = "sample_operation")
    public String operation(@WebParam(name = "param_name") String param) {
        // implement the web service operation here
        return param;
    }
  
    @WebMethod(action = "add")
    public int add(@WebParam(name = "i") int i, @WebParam(name = "j") int j) {
        int k = i + j;
        return k;
    }
 
    // multiple response example
    @WebMethod(action = "calculate")
    public void calculate(@WebParam(name = "sum", mode = WebParam.Mode.OUT) Holder<Integer> sum, @WebParam(name = "multiply", mode = WebParam.Mode.OUT) Holder<Integer> multiply) {
        sum.value = 4+5;
        multiply.value = 4*5;
    }
 
    @WebMethod(action="swap")
    public Values swap(@XmlElement(required=true)
                       @WebParam(name="values") Values values)
    {
        values = new Values(values.getLeft(), values.getRight());
 
        // Tell the channel that we actually did something
        super.webServiceReceiver.processData(String.valueOf(values));
 
        return values;
    }
 
    // This class may have to be in a separate file to work properly
    // (i.e. not an inner class). It's only here to make the example
    // simpler.
    public static class Values {
        private int left;
        private int right;
        public Values(int left, int right) {
            this.left = left;
            this.right = right;
        }
        // The @XmlElement annotation lets the WS-generator know that
        // these represent properties that should be serialized to XML
        @XmlElement
        public int getLeft() { return left; }
        @XmlElement
        public int getRight() { return right; }
    }
 }
