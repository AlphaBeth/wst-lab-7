package ru.ifmo.wst.lab1;

import lombok.SneakyThrows;
import org.apache.juddi.api_v3.AccessPointType;
import org.apache.juddi.v3.client.UDDIConstants;
import org.apache.juddi.v3.client.config.UDDIClient;
import org.apache.juddi.v3.client.transport.Transport;
import org.uddi.api_v3.AccessPoint;
import org.uddi.api_v3.AuthToken;
import org.uddi.api_v3.BindingTemplate;
import org.uddi.api_v3.BindingTemplates;
import org.uddi.api_v3.BusinessDetail;
import org.uddi.api_v3.BusinessEntity;
import org.uddi.api_v3.BusinessInfo;
import org.uddi.api_v3.BusinessInfos;
import org.uddi.api_v3.BusinessList;
import org.uddi.api_v3.BusinessService;
import org.uddi.api_v3.Description;
import org.uddi.api_v3.FindBusiness;
import org.uddi.api_v3.GetAuthToken;
import org.uddi.api_v3.GetServiceDetail;
import org.uddi.api_v3.Name;
import org.uddi.api_v3.SaveBusiness;
import org.uddi.api_v3.SaveService;
import org.uddi.api_v3.ServiceDetail;
import org.uddi.api_v3.ServiceInfo;
import org.uddi.api_v3.ServiceInfos;
import org.uddi.v3_service.UDDIInquiryPortType;
import org.uddi.v3_service.UDDIPublicationPortType;
import org.uddi.v3_service.UDDISecurityPortType;

import java.rmi.RemoteException;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

/**
 * This shows you to interact with a UDDI server by publishing a Business,
 * Service and Binding Template. It uses some fairly generic code that should be
 * mostly portable to any other UDDI client library and is therefore consider
 * "portable". URLs are set in uddi.xml
 */
public class JUDDIClient {

    private final UDDIInquiryPortType inquiry;
    private UDDISecurityPortType security = null;
    private UDDIPublicationPortType publish = null;
    private AuthToken authToken;

    @SneakyThrows
    public JUDDIClient(String configFile) {
        // create a client and read the config in the archive;
        // you can use your config file name
        UDDIClient uddiClient = new UDDIClient(configFile);
        // a UddiClient can be a client to multiple UDDI nodes, so
        // supply the nodeName (defined in your uddi.xml.
        // The transport can be WS, inVM, RMI etc which is defined in the uddi.xml
        Transport transport = uddiClient.getTransport("default");
        // Now you create a reference to the UDDI API
        security = transport.getUDDISecurityService();
        publish = transport.getUDDIPublishService();
        inquiry = transport.getUDDIInquiryService();
    }


    /**
     * This function shows you how to publish to UDDI using a fairly generic
     * mechanism that should be portable (meaning use any UDDI v3 library
     * with this code)
     */
    public void publishUrl(String serviceName, String wsdlUrl) throws RemoteException {
        BusinessDetail bd = createBusiness();

        String myBusKey = bd.getBusinessEntity().get(0).getBusinessKey();
        System.out.println("myBusiness key:  " + myBusKey);

        // Creating a service to save.  Only adding the minimum data: the parent business key retrieved from saving the business
        // above and a single name.
        BusinessService myService = new BusinessService();
        myService.setBusinessKey(myBusKey);
        Name myServName = new Name();
        myServName.setValue(serviceName);
        myService.getName().add(myServName);

        // Add binding templates, etc...
        BindingTemplate myBindingTemplate = new BindingTemplate();
        AccessPoint accessPoint = new AccessPoint();
        accessPoint.setUseType(AccessPointType.WSDL_DEPLOYMENT.toString());
        accessPoint.setValue(wsdlUrl);
        myBindingTemplate.setAccessPoint(accessPoint);
        BindingTemplates myBindingTemplates = new BindingTemplates();
        //optional but recommended step, this annotations our binding with all the standard SOAP tModel instance infos
        myBindingTemplate = UDDIClient.addSOAPtModels(myBindingTemplate);
        myBindingTemplates.getBindingTemplate().add(myBindingTemplate);

        myService.setBindingTemplates(myBindingTemplates);

        // Adding the service to the "save" structure, using our publisher's authentication info and saving away.
        SaveService ss = new SaveService();
        ss.getBusinessService().add(myService);
        ss.setAuthInfo(authToken.getAuthInfo());
        ServiceDetail sd = publish.saveService(ss);
        String myServKey = sd.getBusinessService().get(0).getServiceKey();
        System.out.println("myService key:  " + myServKey);

    }

    private BusinessDetail createBusiness() throws RemoteException {
        // Creating the parent business entity that will contain our service.
        BusinessEntity myBusEntity = new BusinessEntity();
        Name myBusName = new Name();
        myBusName.setValue("My Business");
        myBusEntity.getName().add(myBusName);

        // Adding the business entity to the "save" structure, using our publisher's authentication info and saving away.
        SaveBusiness sb = new SaveBusiness();
        sb.getBusinessEntity().add(myBusEntity);
        sb.setAuthInfo(authToken.getAuthInfo());
        return publish.saveBusiness(sb);
    }

    private AuthToken getAuthToken(String userId, String userCred) throws RemoteException {
        // Login aka retrieve its authentication token
        GetAuthToken getAuthTokenMyPub = new GetAuthToken();
        getAuthTokenMyPub.setUserID(userId);                    //your username
        getAuthTokenMyPub.setCred(userCred);                          //your password
        return security.getAuthToken(getAuthTokenMyPub);
    }

    public BusinessList getBusinessList() throws RemoteException {
        FindBusiness fb = new FindBusiness();
        fb.setAuthInfo(authToken.getAuthInfo());
        org.uddi.api_v3.FindQualifiers fq = new org.uddi.api_v3.FindQualifiers();
        fq.getFindQualifier().add(UDDIConstants.APPROXIMATE_MATCH);

        fb.setFindQualifiers(fq);
        Name searchname = new Name();
        searchname.setValue(UDDIConstants.WILDCARD);
        fb.getName().add(searchname);
        BusinessList findBusiness = inquiry.findBusiness(fb);
        return findBusiness;
    }

    public List<BusinessService> getServices(String filter) throws RemoteException {
        BusinessList businessList = getBusinessList();
        BusinessInfos businessInfos = businessList.getBusinessInfos();
        GetServiceDetail gsd = new GetServiceDetail();
        for (BusinessInfo businessInfo : businessInfos.getBusinessInfo()) {
            for (ServiceInfo serviceInfo : businessInfo.getServiceInfos().getServiceInfo()) {
                gsd.getServiceKey().add(serviceInfo.getServiceKey());
            }
        }
        gsd.setAuthInfo(authToken.getAuthInfo());
        ServiceDetail serviceDetail = inquiry.getServiceDetail(gsd);
        if (filter == null || filter.trim().isEmpty()) {
            return serviceDetail.getBusinessService();
        }
        String filterTrim = filter.trim();
        return serviceDetail.getBusinessService().stream()
                .map(bs -> new Pair<>(bs, String.join(" ",
                        bs.getServiceKey(),
                        bs.getDescription().stream().map(Description::getValue).collect(joining(" ")),
                        bs.getName().stream().map(Name::getValue).collect(joining(" "))
                )))
                .map(bsd -> new Pair<>(bsd.getLeft(), bsd.getRight().trim().toLowerCase()))
                .filter(bsd -> bsd.getRight().contains(filterTrim))
                .map(Pair::getLeft)
                .collect(Collectors.toList());
    }

    public void authenticate(String userId, String userCred) throws RemoteException {
        this.authToken = getAuthToken(userId, userCred);
    }

}
