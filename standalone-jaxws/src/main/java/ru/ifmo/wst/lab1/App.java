package ru.ifmo.wst.lab1;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.ifmo.wst.lab1.dao.ExterminatusDAO;
import ru.ifmo.wst.lab1.dao.SampleDAO;
import ru.ifmo.wst.lab1.util.Configuration;
import ru.ifmo.wst.lab1.ws.ExterminatusService;
import ru.ifmo.wst.lab1.ws.TestDAOService;
import ru.ifmo.wst.lab1.ws.TestService;

import javax.sql.DataSource;
import javax.xml.ws.Endpoint;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class App {
    public static void main(String[] args) throws Exception {
        log.info("Start application");
        Configuration conf = new Configuration("config.properties");
        String scheme = conf.get("scheme", "http:");
        String host = conf.get("host", "localhost");
        String port = conf.get("port", "8080");
        String baseUrl = scheme + "//" + host + ":" + port;

        String serviceName = conf.get("test.service.name", "TestService");
        String daoServiceName = conf.get("test.dao.service.name", "DAO");
        String exterminatusServiceName = conf.get("exterminatus.service.name");

        String testServiceUrl = baseUrl + "/" + serviceName;
        String daoServiceUrl = baseUrl + "/" + daoServiceName;
        String exterminatusUrl = baseUrl + "/" + exterminatusServiceName;

        DataSource dataSource = initDataSource();

        SampleDAO sampleDAO = new SampleDAO(dataSource);
        log.info("Publish test service onto {}", testServiceUrl);
        Endpoint publish = Endpoint.publish(testServiceUrl, new TestService());
        log.info("Publish test dao service onto {}", daoServiceUrl);
        Endpoint.publish(daoServiceUrl, new TestDAOService(sampleDAO));
        log.info("Publish exterminatus service onto {}", exterminatusUrl);
        Endpoint.publish(exterminatusUrl, new ExterminatusService(new ExterminatusDAO(dataSource)));
        log.info("Application was successfully started");
    }

    @SneakyThrows
    private static DataSource initDataSource() {
        InputStream dsPropsStream = App.class.getClassLoader().getResourceAsStream("datasource.properties");
        Properties dsProps = new Properties();
        dsProps.load(dsPropsStream);
        HikariConfig hikariConfig = new HikariConfig(dsProps);
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        return dataSource;
    }
}
