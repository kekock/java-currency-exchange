package servlet;

import dto.ExchangeRatesDTO;
import exceptions.AlreadyExistsException;
import exceptions.MissingFormFieldsException;
import exceptions.NotFoundException;
import exceptions.SameCodeException;
import service.impl.ExchangeRatesServiceImpl;
import utils.Response;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {

    private static final String UTF_8_ENCODING = "UTF-8";
    private static final String PARAM_BASE_CURRENCY_CODE = "baseCurrencyCode";
    private static final String PARAM_TARGET_CURRENCY_CODE = "targetCurrencyCode";
    private static final String PARAM_RATE = "rate";
    private static final String ERROR_MISSING_FIELDS_MESSAGE = "Invalid or missing required form fields.";
    private static final String ERROR_PAIR_EXISTS_MESSAGE = "Pair with code '%s' already exists.";
    private static final String ERROR_GENERIC_MESSAGE = "Sorry, something went wrong on our end. Please try again later.";
    private static final String ERROR_CURRENCY_NOT_FOUND_MESSAGE = "Currency with code '%s' or '%s' not found.";
    private static final String ERROR_BASE_CURRENCY_SAME_AS_TARGET_MESSAGE = "Base and target currencies must be different.";

    private ExchangeRatesServiceImpl service;

    @Override
    public void init(){
        this.service = new ExchangeRatesServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<ExchangeRatesDTO> exchangeRatesList = service.findAll();
            Response.sendJsonResponse(resp, exchangeRatesList);
        }
        catch (Exception e) {
            Response.sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ERROR_GENERIC_MESSAGE);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding(UTF_8_ENCODING);

        String baseCurrencyCode = req.getParameter(PARAM_BASE_CURRENCY_CODE);
        String targetCurrencyCode = req.getParameter(PARAM_TARGET_CURRENCY_CODE);
        String rate = req.getParameter(PARAM_RATE);

        try {
            ExchangeRatesDTO exchangeRate = service.save(baseCurrencyCode, targetCurrencyCode, rate);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            Response.sendJsonResponse(resp, exchangeRate);
        }
        catch (MissingFormFieldsException e) {
            Response.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, ERROR_MISSING_FIELDS_MESSAGE);
        }
        catch (SameCodeException e) {
            Response.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, ERROR_BASE_CURRENCY_SAME_AS_TARGET_MESSAGE);
        }
        catch (AlreadyExistsException e) {
            Response.sendErrorResponse(resp, HttpServletResponse.SC_CONFLICT,
                    String.format(ERROR_PAIR_EXISTS_MESSAGE, baseCurrencyCode + targetCurrencyCode));
        }
        catch (NotFoundException e) {
            Response.sendErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                    String.format(ERROR_CURRENCY_NOT_FOUND_MESSAGE, baseCurrencyCode, targetCurrencyCode));
        }
        catch (Exception e) {
            Response.sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ERROR_GENERIC_MESSAGE);
        }
    }
}
