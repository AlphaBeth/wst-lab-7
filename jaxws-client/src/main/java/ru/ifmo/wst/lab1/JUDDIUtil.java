package ru.ifmo.wst.lab1;

import org.apache.juddi.api_v3.AccessPointType;
import org.uddi.api_v3.BindingTemplate;
import org.uddi.api_v3.BindingTemplates;
import org.uddi.api_v3.BusinessInfo;
import org.uddi.api_v3.BusinessInfos;
import org.uddi.api_v3.BusinessService;
import org.uddi.api_v3.Description;
import org.uddi.api_v3.Name;

import java.util.List;
import java.util.stream.Collectors;

public class JUDDIUtil {
    public static void printBusinessInfo(BusinessInfos businessInfos) {
        if (businessInfos == null) {
            System.out.println("No data returned");
        } else {
            for (BusinessInfo businessInfo : businessInfos.getBusinessInfo()) {
                System.out.println("===============================================");
                System.out.println("Business Key: " + businessInfo.getBusinessKey());
                System.out.println("Name: " + businessInfo.getName().stream()
                        .map(Name::getValue)
                        .collect(Collectors.joining(" "))
                );

                System.out.println("Description: " + businessInfo
                        .getDescription()
                        .stream()
                        .map(Description::getValue)
                        .collect(Collectors.joining(" "))
                );
            }
        }
    }

    public static void printServicesInfo(List<BusinessService> businessServices) {
        for (BusinessService businessService : businessServices) {
            System.out.println("-------------------------------------------");
            printServiceInfo(businessService);
        }
    }

    public static void printServiceInfo(BusinessService businessService) {
        System.out.println("Service Key: " + businessService.getServiceKey());
        System.out.println("Owning Business Key: " + businessService.getBusinessKey());
        System.out.println("Name: " + businessService.getName().stream()
                .map(JUDDIUtil::nameToString)
                .collect(Collectors.joining("\n"))
        );
        printBindingTemplates(businessService.getBindingTemplates());
    }

    /**
     * This function is useful for translating UDDI's somewhat complex data
     * format to something that is more useful.
     *
     * @param bindingTemplates
     */
    private static void printBindingTemplates(BindingTemplates bindingTemplates) {
        if (bindingTemplates == null) {
            return;
        }
        for (BindingTemplate bindingTemplate : bindingTemplates.getBindingTemplate()) {
            System.out.println("Binding Key: " + bindingTemplate.getBindingKey());

            if (bindingTemplate.getAccessPoint() != null) {
                System.out.println("Access Point: " + bindingTemplate.getAccessPoint().getValue() + " type " + bindingTemplate.getAccessPoint().getUseType());
                if (bindingTemplate.getAccessPoint().getUseType() != null) {
                    if (bindingTemplate.getAccessPoint().getUseType().equalsIgnoreCase(AccessPointType.END_POINT.toString())) {
                        System.out.println("Use this access point value as an invocation endpoint.");
                    }
                    if (bindingTemplate.getAccessPoint().getUseType().equalsIgnoreCase(AccessPointType.BINDING_TEMPLATE.toString())) {
                        System.out.println("Use this access point value as a reference to another binding template.");
                    }
                    if (bindingTemplate.getAccessPoint().getUseType().equalsIgnoreCase(AccessPointType.WSDL_DEPLOYMENT.toString())) {
                        System.out.println("Use this access point value as a URL to a WSDL document, which presumably will have a real access point defined.");
                    }
                    if (bindingTemplate.getAccessPoint().getUseType().equalsIgnoreCase(AccessPointType.HOSTING_REDIRECTOR.toString())) {
                        System.out.println("Use this access point value as an Inquiry URL of another UDDI registry, look up the same binding template there (usage varies).");
                    }
                }
            }
        }
    }

    public static String nameToString(Name name) {
        return "Lang: " + name.getLang() +
                "\n" +
                "Value: " + name.getValue();
    }
}
