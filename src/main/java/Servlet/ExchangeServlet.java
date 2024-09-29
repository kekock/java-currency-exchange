package Servlet;

import DTO.ExchangeResultDTO;
import Exceptions.MissingFormFieldsException;
import Exceptions.NotFoundException;
import Exceptions.SameCodeException;
import Service.ExchangeService;
import Utils.Response;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {

    private static final String UTF_8_ENCODING = "UTF-8";
    private static final String PARAM_FROM = "from";
    private static final String PARAM_TO = "to";
    private static final String PARAM_AMOUNT = "amount";
    private static final String ERROR_GENERIC_MESSAGE = "Sorry, something went wrong on our end. Please try again later.";
    private static final String ERROR_MISSING_FIELDS_MESSAGE = "Invalid or missing required form fields.";
    private static final String ERROR_PAIR_NOT_FOUND_MESSAGE = "Exchange rate for pair '%s' cannot be found.";
    private static final String ERROR_BASE_CURRENCY_SAME_AS_TARGET_MESSAGE = "Base and target currencies must be different.";

    private ExchangeService service;

    @Override
    public void init(){
        this.service = new ExchangeService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding(UTF_8_ENCODING);

        String fromCurrencyCode = req.getParameter(PARAM_FROM);
        String toCurrencyCode = req.getParameter(PARAM_TO);
        String exchangeAmount = req.getParameter(PARAM_AMOUNT);

        try {
            ExchangeResultDTO result = service.convertCurrency(fromCurrencyCode, toCurrencyCode, exchangeAmount );
            Response.sendJsonResponse(resp, result);
        }
        catch (MissingFormFieldsException e) {
            Response.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, ERROR_MISSING_FIELDS_MESSAGE);
        }
        catch (SameCodeException e) {
            Response.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, ERROR_BASE_CURRENCY_SAME_AS_TARGET_MESSAGE);
        }
        catch (NotFoundException e) {
            Response.sendErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, String.format(ERROR_PAIR_NOT_FOUND_MESSAGE, fromCurrencyCode + toCurrencyCode));
        }
        catch (Exception e) {
            Response.sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ERROR_GENERIC_MESSAGE);
        }
    }
}
