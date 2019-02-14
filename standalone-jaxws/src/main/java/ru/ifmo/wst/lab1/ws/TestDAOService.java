package ru.ifmo.wst.lab1.ws;

import lombok.SneakyThrows;
import ru.ifmo.wst.lab1.dao.SampleDAO;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.sql.SQLException;
import java.util.List;

@WebService
public class TestDAOService {

    private SampleDAO dao;

    public TestDAOService(SampleDAO dao) {
        this.dao = dao;
    }

    public TestDAOService() {
        this.dao = null;
    }

    @WebMethod
    @SneakyThrows
    public List<String> testDAO() {
        return dao.sampleSelect();
    }
}
