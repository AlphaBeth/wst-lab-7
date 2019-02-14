package ru.ifmo.wst.lab1;

import org.uddi.api_v3.BusinessInfo;
import org.uddi.api_v3.BusinessInfos;
import org.uddi.api_v3.BusinessService;
import org.uddi.api_v3.Description;
import org.uddi.api_v3.Name;
import org.uddi.api_v3.ServiceInfos;

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

    public static void printServiceInfo(List<BusinessService> businessServices) {
        for (BusinessService businessService : businessServices) {
            System.out.println("-------------------------------------------");
            System.out.println("Service Key: " + businessService.getServiceKey());
            System.out.println("Owning Business Key: " + businessService.getBusinessKey());
            System.out.println("Name: " + businessService.getName().stream()
                    .map(JUDDIUtil::nameToString)
                    .collect(Collectors.joining("\n"))
            );
        }
    }

    public static String nameToString(Name name) {
        return "Lang: " + name.getLang() +
                "\n" +
                "Value: " + name.getValue();
    }
}
