package Servlet;

import DTO.CurrenciesDTO;
import Mapper.CurrenciesMapper;
import Utils.RequestParser;
import Utils.Response;
import Entity.Currencies;
import Service.Impl.CurrenciesService;
import Utils.Validation;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Objects;

@WebServlet("/currencies/*")
public class CurrencyServlet extends HttpServlet {

    private static final String UTF_8_ENCODING = "UTF-8";
    private static final String ERROR_CURRENCY_NOT_FOUND = "Currency not found";
    private static final String ERROR_INVALID_CURRENCY_CODE = "Invalid or missing currency code";
    private static final String ERROR_MISSING_FORM_FIELDS = "Missing required form fields (name, sign)";
    private static final String ERROR_NOT_MODIFIED = "The form fields are already up-to-date";
    private static final String ERROR_MESSAGE = "Sorry, something went wrong on our end. Please try again later.";;

    private CurrenciesService service;

    @Override
    public void init() {
        this.service = new CurrenciesService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        try {
            if (Validation.isValidCurrencyPath(pathInfo)) {
                Currencies currency = service.findByCode(Validation.extractCurrencyCode(pathInfo));

                if (currency == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, ERROR_CURRENCY_NOT_FOUND);
                    return;
                }

                CurrenciesDTO currencyDTO = CurrenciesMapper.toDTO(currency);
                Response.writeJsonResponse(resp, currencyDTO);
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ERROR_INVALID_CURRENCY_CODE);
            }
        }
        catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ERROR_MESSAGE);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        try {
            if (Validation.isValidCurrencyPath(pathInfo)) {
                String code = Validation.extractCurrencyCode(pathInfo);

                if (service.findByCode(code) == null){
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, ERROR_CURRENCY_NOT_FOUND);
                    return;
                }

                service.delete(code);
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
            else{
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ERROR_INVALID_CURRENCY_CODE);
            }
        }catch (Exception e){
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ERROR_MESSAGE);
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

        if (!Validation.isValidCurrencyPath(pathInfo)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ERROR_INVALID_CURRENCY_CODE);
            return;
        }

        String currencyCode = Validation.extractCurrencyCode(pathInfo);

        String[] parsedData = RequestParser.parseRequestBody(req);
        String name = parsedData[0];
        String sign = parsedData[1];

        if (Validation.isNullOrEmpty(name) || Validation.isNullOrEmpty(sign)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ERROR_MISSING_FORM_FIELDS);
            return;
        }

        try {
            Currencies existingCurrency = service.findByCode(currencyCode);

            if (existingCurrency == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, ERROR_CURRENCY_NOT_FOUND);
                return;
            }

            if (name.equals(existingCurrency.getFullName()) && sign.equals(existingCurrency.getSign())) {
                resp.sendError(HttpServletResponse.SC_NOT_MODIFIED, ERROR_NOT_MODIFIED);
                return;
            }

            existingCurrency.setFullName(name);
            existingCurrency.setSign(sign);

            service.update(existingCurrency);
            resp.setStatus(HttpServletResponse.SC_OK);

            CurrenciesDTO existingCurrencyDTO = CurrenciesMapper.toDTO(existingCurrency);

            Response.writeJsonResponse(resp, existingCurrencyDTO);
        }
        catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ERROR_MESSAGE);
        }
    }
}
