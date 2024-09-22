package Servlet;

import DTO.CurrenciesDTO;
import Exceptions.NotFoundException;
import Exceptions.NotModifiedException;
import Exceptions.InvalidCodeException;
import Exceptions.MissingFormFieldsException;
import Utils.RequestParser;
import Utils.Response;
import Service.Impl.CurrenciesServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/currencies/*")
public class CurrencyServlet extends HttpServlet {

    private static final String UTF_8_ENCODING = "UTF-8";
    private static final String ERROR_CURRENCY_NOT_FOUND_MESSAGE = "Currency not found";
    private static final String ERROR_INVALID_CURRENCY_CODE_MESSAGE = "Invalid or missing currency code";
    private static final String ERROR_MISSING_FORM_FIELDS_MESSAGE = "Missing required form fields.";;
    private static final String ERROR_NOT_MODIFIED_MESSAGE = "No changes to apply";
    private static final String ERROR_GENERIC_MESSAGE = "Sorry, something went wrong on our end. Please try again later.";;

    private CurrenciesServiceImpl service;

    @Override
    public void init() {
        this.service = new CurrenciesServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        try {
            CurrenciesDTO currency = service.findByCode(pathInfo);
            Response.sendJsonResponse(resp, currency);
        } catch (InvalidCodeException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ERROR_INVALID_CURRENCY_CODE_MESSAGE);
        } catch (NotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, ERROR_CURRENCY_NOT_FOUND_MESSAGE);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ERROR_GENERIC_MESSAGE);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        try {
            service.delete(pathInfo);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
        catch (InvalidCodeException e){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ERROR_INVALID_CURRENCY_CODE_MESSAGE);
        }
        catch (NotFoundException e){
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, ERROR_CURRENCY_NOT_FOUND_MESSAGE);
        }
        catch (Exception e){
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ERROR_GENERIC_MESSAGE);
        }
    }
    
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        if (!method.equals("PATCH")) {
            super.service(req, resp);
            return;
        }

        this.doPatch(req, resp);
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        req.setCharacterEncoding(UTF_8_ENCODING);

        String[] parsedData = RequestParser.parseNameAndSignFromBody(req);
        String name = parsedData[0];
        String sign = parsedData[1];

        try {
            CurrenciesDTO updatedCurrency = service.update(pathInfo, name, sign);
            resp.setStatus(HttpServletResponse.SC_OK);

            Response.sendJsonResponse(resp, updatedCurrency);
        }
        catch (InvalidCodeException e){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ERROR_INVALID_CURRENCY_CODE_MESSAGE);
        }
        catch (MissingFormFieldsException e){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ERROR_MISSING_FORM_FIELDS_MESSAGE);
        }
        catch (NotFoundException e){
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, ERROR_CURRENCY_NOT_FOUND_MESSAGE);
        }
        catch (NotModifiedException e){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ERROR_NOT_MODIFIED_MESSAGE);
        }
        catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ERROR_GENERIC_MESSAGE);
        }
    }
}
