package servlet;

import dto.ExchangeRatesDTO;
import exceptions.InvalidCodeException;
import exceptions.MissingFormFieldsException;
import exceptions.NotFoundException;
import exceptions.NotModifiedException;
import service.impl.ExchangeRatesServiceImpl;
import utils.RequestParser;
import utils.Response;
import utils.Validation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {

    private static final String UTF_8_ENCODING = "UTF-8";
    private static final String ERROR_GENERIC_MESSAGE = "Sorry, something went wrong on our end. Please try again later.";
    private static final String ERROR_PAIR_NOT_FOUND_MESSAGE = "Pair code not found.";
    private static final String ERROR_INVALID_PAIR_CODE_MESSAGE = "Invalid or missing pair code.";
    private static final String ERROR_MISSING_FIELDS_MESSAGE = "Invalid or missing required form fields.";
    private static final String ERROR_NOT_MODIFIED_MESSAGE = "No changes to apply";

    private ExchangeRatesServiceImpl service;

    @Override
    public void init() {
        this.service = new ExchangeRatesServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        try {
            ExchangeRatesDTO exchangeRate = service.findByCode(pathInfo);
            Response.sendJsonResponse(resp, exchangeRate);
        }
        catch (InvalidCodeException e) {
            Response.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, ERROR_INVALID_PAIR_CODE_MESSAGE);
        }
        catch (NotFoundException e) {
            Response.sendErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, ERROR_PAIR_NOT_FOUND_MESSAGE);
        }
        catch (Exception e) {
            Response.sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ERROR_GENERIC_MESSAGE);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        try {
            service.delete(pathInfo);
            resp.sendError(HttpServletResponse.SC_NO_CONTENT);
        }
        catch (InvalidCodeException e) {
            Response.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, ERROR_INVALID_PAIR_CODE_MESSAGE);
        }
        catch (NotFoundException e) {
            Response.sendErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, ERROR_PAIR_NOT_FOUND_MESSAGE);
        }
        catch (Exception e) {
            Response.sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ERROR_GENERIC_MESSAGE);
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

        if (!Validation.isValidPairPath(pathInfo)){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ERROR_INVALID_PAIR_CODE_MESSAGE);
            return;
        }
        req.setCharacterEncoding(UTF_8_ENCODING);
        String rate = RequestParser.parseRateFromBody(req);

        String pair = Validation.extractPairCode(pathInfo);
        String base = pair.substring(0, 3);
        String target = pair.substring(3);

        try {
            ExchangeRatesDTO exchangeRate = service.update(base, target, rate);
            Response.sendJsonResponse(resp, exchangeRate);
        }
        catch (MissingFormFieldsException e) {
            Response.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, ERROR_MISSING_FIELDS_MESSAGE);
        }
        catch (NotFoundException e) {
            Response.sendErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, ERROR_PAIR_NOT_FOUND_MESSAGE);
        }
        catch (NotModifiedException e) {
            Response.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, ERROR_NOT_MODIFIED_MESSAGE);
        }
        catch (Exception e) {
            Response.sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ERROR_GENERIC_MESSAGE);
        }
    }
}
