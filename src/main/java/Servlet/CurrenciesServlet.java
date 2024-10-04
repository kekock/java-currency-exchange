package Servlet;

import DTO.CurrenciesDTO;
import Exceptions.AlreadyExistsException;
import Exceptions.MissingFormFieldsException;
import Utils.Response;
import Service.Impl.CurrenciesServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    private static final String UTF_8_ENCODING = "UTF-8";
    private static final String PARAM_CURRENCY_NAME = "name";
    private static final String PARAM_CURRENCY_CODE = "code";
    private static final String PARAM_CURRENCY_SIGN = "sign";
    private static final String ERROR_GENERIC_MESSAGE = "Sorry, something went wrong on our end. Please try again later.";
    private static final String ERROR_MISSING_FIELDS_MESSAGE = "Invalid or missing required form fields.";
    private static final String ERROR_CURRENCY_EXISTS_MESSAGE = "Currency with code '%s' already exists.";

    private CurrenciesServiceImpl service;

    @Override
    public void init() {
        this.service = new CurrenciesServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<CurrenciesDTO> currenciesList = service.findAll();
            Response.sendJsonResponse(resp, currenciesList);
        }
        catch (Exception e) {
            Response.sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ERROR_GENERIC_MESSAGE);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding(UTF_8_ENCODING);

        String name = req.getParameter(PARAM_CURRENCY_NAME);
        String code = req.getParameter(PARAM_CURRENCY_CODE);
        String sign = req.getParameter(PARAM_CURRENCY_SIGN);

        try {
            CurrenciesDTO currency = service.save(name, code, sign);
            resp.setStatus(HttpServletResponse.SC_CREATED);

            Response.sendJsonResponse(resp, currency);
        }
        catch (MissingFormFieldsException e) {
            Response.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, ERROR_MISSING_FIELDS_MESSAGE);
        }
        catch (AlreadyExistsException e) {
            Response.sendErrorResponse(resp, HttpServletResponse.SC_CONFLICT, String.format(ERROR_CURRENCY_EXISTS_MESSAGE, code));
        }
        catch (Exception e) {
            Response.sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ERROR_GENERIC_MESSAGE);
        }
    }
}