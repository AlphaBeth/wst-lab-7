package ru.ifmo.wst.lab1.ws;

import ru.ifmo.wst.lab1.dao.ExterminatusDAO;
import ru.ifmo.wst.lab1.model.ExterminatusEntity;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@WebService
public class ExterminatusService {
    @Inject
    private ExterminatusDAO exterminatusDAO;

    public ExterminatusService(ExterminatusDAO exterminatusDAO) {
        this.exterminatusDAO = exterminatusDAO;
    }

    public ExterminatusService() {
    }

    @WebMethod
    public List<ExterminatusEntity> findAll() throws ExterminatusServiceException {
        return wrapException(() -> exterminatusDAO.findAll());
    }

    @WebMethod
    public List<ExterminatusEntity> filter(@WebParam(name = "id") Long id, @WebParam(name = "initiator") String initiator,
                                           @WebParam(name = "reason") String reason, @WebParam(name = "method") String method,
                                           @WebParam(name = "planet") String planet, @WebParam(name = "date") Date date)
            throws ExterminatusServiceException {
        return wrapException(() -> exterminatusDAO.filter(id, initiator, reason, method, planet, date));
    }

    @WebMethod
    public long create(@WebParam(name = "initiator") String initiator,
                       @WebParam(name = "reason") String reason, @WebParam(name = "method") String method,
                       @WebParam(name = "planet") String planet, @WebParam(name = "date") Date date)
            throws ExterminatusServiceException {
        notNullArg("initiator", initiator);
        notNullArg("method", method);
        notNullArg("planet", planet);
        notNullArg("date", date);
        return wrapException(() -> exterminatusDAO.create(initiator, reason, method, planet, date));
    }

    @WebMethod
    public int delete(@WebParam(name = "id") long id) throws ExterminatusServiceException {
        return wrapException(() -> {
            int deletedCount = exterminatusDAO.delete(id);
            if (deletedCount <= 0) {
                String message = String.format("No records with id %d found to delete", id);
                throw new ExterminatusServiceException(message, new ExterminatusServiceFault(message));
            }
            return deletedCount;
        });
    }

    @WebMethod
    @WebResult(name = "updatedCount")
    public int update(@WebParam(name = "id") long id, @WebParam(name = "initiator") String initiator,
                      @WebParam(name = "reason") String reason, @WebParam(name = "method") String method,
                      @WebParam(name = "planet") String planet, @WebParam(name = "date") Date date) throws ExterminatusServiceException {
        notNullArg("initiator", initiator);
        notNullArg("method", method);
        notNullArg("planet", planet);
        notNullArg("date", date);
        return wrapException(() -> {
            int updatedCount = exterminatusDAO.update(id, initiator, reason, method, planet, date);
            if (updatedCount <= 0) {
                String message = String.format("No records with id %d found to update", id);
                throw new ExterminatusServiceException(message, new ExterminatusServiceFault(message));
            }
            return updatedCount;
        });
    }

    private void notNullArg(String argName, Object argValue) throws ExterminatusServiceException {
        if (argValue == null) {
            String message = argName + " must be not null";
            throw new ExterminatusServiceException(message, new ExterminatusServiceFault(message));
        }
    }

    private <T> T wrapException(Supplier<T> supplier) throws ExterminatusServiceException {
        try {
            return supplier.produce();
        } catch (ExterminatusServiceException exc) {
            throw exc;
        } catch (SQLException exc) {
            String message = "Unexpected SQL exception with message " + exc.getMessage() + " and sql state " + exc.getSQLState();
            throw new ExterminatusServiceException(message, exc, new ExterminatusServiceFault(message));
        } catch (Exception exc) {
            String message = "Unexpected exception " + exc.getClass().getName() + " with message " + exc.getMessage();
            throw new ExterminatusServiceException(message, exc, new ExterminatusServiceFault(message));
        }
    }

    private interface Supplier<T> {
        T produce() throws Exception;
    }
}
