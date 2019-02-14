package ru.ifmo.wst.lab1;

import lombok.SneakyThrows;
import org.uddi.api_v3.BusinessService;
import ru.ifmo.wst.lab1.command.Command;
import ru.ifmo.wst.lab1.command.CommandArg;
import ru.ifmo.wst.lab1.command.CommandInterpreter;
import ru.ifmo.wst.lab1.command.NoLineFoundException;
import ru.ifmo.wst.lab1.command.args.DateArg;
import ru.ifmo.wst.lab1.command.args.EmptyStringToNull;
import ru.ifmo.wst.lab1.command.args.LongArg;
import ru.ifmo.wst.lab1.command.args.StringArg;
import ru.ifmo.wst.lab1.ws.client.Create;
import ru.ifmo.wst.lab1.ws.client.ExterminatusEntity;
import ru.ifmo.wst.lab1.ws.client.ExterminatusService;
import ru.ifmo.wst.lab1.ws.client.ExterminatusServiceException;
import ru.ifmo.wst.lab1.ws.client.ExterminatusServiceService;
import ru.ifmo.wst.lab1.ws.client.Filter;
import ru.ifmo.wst.lab1.ws.client.Update;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingProvider;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static java.util.Arrays.asList;

public class ConsoleClient {
    @SneakyThrows
    public static void main(String[] args) {
        ExterminatusServiceService exterminatusService = new ExterminatusServiceService();
        ExterminatusService service = exterminatusService.getExterminatusServicePort();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BindingProvider bindingProvider = (BindingProvider) service;
        String endpointUrl;
        endpointUrl = "http://localhost:8080/EXTERMINATE";
        System.out.print("Enter endpoint url (or empty string for default " + endpointUrl + ")\n> ");
        String line = bufferedReader.readLine();
        if (line == null) {
            return;
        }
        if (!line.trim().isEmpty()) {
            endpointUrl = line.trim();
        }
        System.out.println("Enter JUDDI username");
        String username = bufferedReader.readLine().trim();
        System.out.println("Enter JUDDI user password");
        String password = bufferedReader.readLine().trim();
        bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointUrl);
        JUDDIClient juddiClient = new JUDDIClient("META-INF/uddi.xml");
        juddiClient.authenticate(username, password);


        Command<Void> infoCommand = new Command<>("info", "Print help for commands");
        Command<Box<String>> changeEndpointAddressCommand = new Command<>("endpoint", "Changes endpoint address",
                asList(
                        new StringArg<>("Not a string", "url", "New exterminatus endpoint url", Box::setValue)
                ), Box::new
        );
        Command<Void> findAllCommand = new Command<>("findAll", "Return list of all exterminatus entities");
        Command<Filter> filterCommand = new Command<>("filter",
                "Filter exterminatus entities by column values (ignore case contains for strings), empty values are ignored",
                asList(
                        toNull(new LongArg<>("id", "Exterminatus id", Filter::setId)),
                        toNull(new StringArg<>("initiator", "Initiator name", Filter::setInitiator)),
                        toNull(new StringArg<>("reason", "Reason of exterminatus", Filter::setReason)),
                        toNull(new StringArg<>("method", "Method of exterminatus", Filter::setMethod)),
                        toNull(new StringArg<>("planet", "Exterminated planet", Filter::setPlanet)),
                        toNull(new DateArg<>("date", "Date of exterminatus", (filter, date) -> filter.setDate(fromDate(date))))
                ),
                Filter::new);
        Command<Create> createCommand = new Command<>("create",
                "Create new exterminatus entity",
                asList(
                        toNull(new StringArg<>("initiator", "Initiator name", Create::setInitiator)),
                        toNull(new StringArg<>("reason", "Reason of exterminatus", Create::setReason)),
                        toNull(new StringArg<>("method", "Method of exterminatus", Create::setMethod)),
                        toNull(new StringArg<>("planet", "Exterminated planet", Create::setPlanet)),
                        toNull(new DateArg<>("date", "Date of exterminatus", (filter, date) -> filter.setDate(fromDate(date))))
                ), Create::new);
        Command<Update> updateCommand = new Command<>("update",
                "Update exterminatus by id",
                asList(
                        new LongArg<>("id", "Exterminatus id", Update::setId),
                        toNull(new StringArg<>("initiator", "Initiator name", Update::setInitiator)),
                        toNull(new StringArg<>("reason", "Reason of exterminatus", Update::setReason)),
                        toNull(new StringArg<>("method", "Method of exterminatus", Update::setMethod)),
                        toNull(new StringArg<>("planet", "Exterminated planet", Update::setPlanet)),
                        toNull(new DateArg<>("date", "Date of exterminatus", (filter, date) -> filter.setDate(fromDate(date))))
                ), Update::new
        );
        Command<Void> exitCommand = new Command<>("exit", "Exit application");

        Command<Box<Long>> deleteCommand = new Command<>("delete", "Delete exterminatus by id",
                asList(
                        new LongArg<>("id", "Exterminatus id", Box::setValue)
                ), Box::new);

        Command<Void> listBusinesses = new Command<>("listBusinesses", "List all businesses registered on JUDDI");

        Command<Box<String>> filterServices = new Command<>("filterServices", "Filter all services list in JUDDI",
                asList(
                        new StringArg<>("filter query", "String to filter services", Box::setValue)
                ), Box::new);

        CommandInterpreter commandInterpreter = new CommandInterpreter(() -> readLine(bufferedReader),
                System.out::print,
                asList(
                        infoCommand, changeEndpointAddressCommand, listBusinesses, filterServices, createCommand,
                        findAllCommand, filterCommand,
                        updateCommand, deleteCommand, exitCommand
                ),
                "No command found",
                "Enter command", "> ");

        commandInterpreter.info();

        while (true) {
            Pair<Command, Object> withArg;
            try {
                withArg = commandInterpreter.readCommand();
            } catch (NoLineFoundException exc) {
                return;
            }
            if (withArg == null) {
                continue;
            }
            Command command = withArg.getLeft();
            try {
                if (command.equals(findAllCommand)) {
                    List<ExterminatusEntity> all = service.findAll();
                    System.out.println("Result of operation:");
                    all.forEach(ee -> System.out.println(exterminatusToString(ee)));
                } else if (command.equals(filterCommand)) {
                    Filter filterArg = (Filter) withArg.getRight();
                    List<ExterminatusEntity> filterRes = service.filter(filterArg.getId(), filterArg.getInitiator(), filterArg.getReason(), filterArg.getMethod(),
                            filterArg.getPlanet(), filterArg.getDate());
                    System.out.println("Result of operation:");
                    filterRes.forEach(ee -> System.out.println(exterminatusToString(ee)));
                } else if (command.equals(infoCommand)) {
                    commandInterpreter.info();
                } else if (command.equals(exitCommand)) {
                    break;
                } else if (command.equals(changeEndpointAddressCommand)) {
                    @SuppressWarnings("unchecked")
                    Box<String> arg = (Box<String>) withArg.getRight();
                    String newUrl = arg.getValue();
                    bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, newUrl);
                } else if (command.equals(createCommand)) {
                    Create createArg = (Create) withArg.getRight();
                    long createdId = service.create(createArg.getInitiator(), createArg.getReason(), createArg.getMethod(), createArg.getPlanet(),
                            createArg.getDate());
                    System.out.printf("Entity with id %d was created\n", createdId);
                } else if (command.equals(deleteCommand)) {
                    @SuppressWarnings("unchecked")
                    Box<Long> argRight = (Box<Long>) withArg.getRight();
                    int deletedCount = service.delete(argRight.getValue());
                    System.out.printf("%d were deleted by id %d\n", deletedCount, argRight.getValue());
                } else if (command.equals(updateCommand)) {
                    Update updateArg = (Update) withArg.getRight();
                    int updateCount = service.update(updateArg.getId(), updateArg.getInitiator(), updateArg.getReason(), updateArg.getMethod(),
                            updateArg.getPlanet(), updateArg.getDate());
                    System.out.printf("%d rows were updated by id %d\n", updateCount, updateArg.getId());
                } else if (command.equals(listBusinesses)) {
                    JUDDIUtil.printBusinessInfo(juddiClient.getBusinessList().getBusinessInfos());
                } else if (command.equals(filterServices)) {
                    @SuppressWarnings("unchecked")
                    Box<String> filterArg = (Box<String>) withArg.getRight();
                    List<BusinessService> services = juddiClient.getServices(filterArg.getValue());
                    JUDDIUtil.printServiceInfo(services);
                }
            } catch (ExterminatusServiceException exc) {
                System.out.println("Error in service:");
                System.out.println(exc.getFaultInfo().getMessage());
            } catch (Exception exc) {
                System.out.println("Unknown error");
                exc.printStackTrace();
            }

        }
    }

    private static <T, C> CommandArg<T, C> toNull(CommandArg<T, C> commandArg) {
        return new EmptyStringToNull<>(commandArg);
    }

    @SneakyThrows
    private static String readLine(BufferedReader reader) {
        return reader.readLine();
    }

    @SneakyThrows
    private static XMLGregorianCalendar fromDate(Date date) {
        if (date == null) {
            return null;
        }
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(
                gregorianCalendar);
    }

    private static String exterminatusToString(ExterminatusEntity ee) {
        return "ExterminatusEntity{" +
                "date=" + ee.getDate() +
                ", id=" + ee.getId() +
                ", initiator='" + ee.getInitiator() + '\'' +
                ", method='" + ee.getMethod() + '\'' +
                ", planet='" + ee.getPlanet() + '\'' +
                ", reason='" + ee.getReason() + '\'' +
                '}';

    }
}
