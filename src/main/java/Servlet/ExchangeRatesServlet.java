package Servlet;

import DTO.ExchangeRatesDTO;
import Service.Impl.ExchangeRatesServiceImpl;
import Utils.Response;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {

    private static final String ERROR_MESSAGE = "Sorry, something went wrong on our end. Please try again later.";

    private ExchangeRatesServiceImpl service;

    @Override
    public void init(){
        this.service = new ExchangeRatesServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<ExchangeRatesDTO> exchangeRatesList = service.findAll();
            Response.writeJsonResponse(resp, exchangeRatesList);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ERROR_MESSAGE);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
