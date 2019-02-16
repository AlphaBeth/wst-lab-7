package ru.ifmo.wst.lab1;

import lombok.SneakyThrows;
import ru.ifmo.wst.lab1.client.ConsoleClientImpl;
import ru.ifmo.wst.lab1.ws.client.ExterminatusService;
import ru.ifmo.wst.lab1.ws.client.ExterminatusServiceService;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ConsoleClient {
    @SneakyThrows
    public static void main(String[] args) {
        ExterminatusServiceService exterminatusService = new ExterminatusServiceService();
        ExterminatusService service = exterminatusService.getExterminatusServicePort();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter JUDDI username");
        String username = bufferedReader.readLine().trim();
        System.out.println("Enter JUDDI user password");
        String password = bufferedReader.readLine().trim();
        JUDDIClient juddiClient = new JUDDIClient("META-INF/uddi.xml");
        juddiClient.authenticate(username, password);

        ConsoleClientImpl consoleClient = new ConsoleClientImpl(service, juddiClient);
        consoleClient.shortInfo();
        consoleClient.start();

    }

}
