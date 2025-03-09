package currency.servlet;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/convert")
public class CurrencyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/your_api_key/latest/";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fromCurrency = request.getParameter("curr1");
        String toCurrency = request.getParameter("curr2");
        String amountStr = request.getParameter("amt");

        if (fromCurrency == null || toCurrency == null || amountStr == null || amountStr.isEmpty()) {
            request.setAttribute("error", "Please enter valid values.");
            request.getRequestDispatcher("index.html").forward(request, response);
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            double convertedAmount = getConvertedAmount(amount, fromCurrency, toCurrency);

            request.setAttribute("fromCurrency", fromCurrency);
            request.setAttribute("toCurrency", toCurrency);
            request.setAttribute("amount", amount);
            request.setAttribute("convertedAmount", convertedAmount);

            request.getRequestDispatcher("result.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Conversion failed. Please try again.");
            request.getRequestDispatcher("index.html").forward(request, response);
        }
    }

    private double getConvertedAmount(double amount, String from, String to) throws IOException {
        String apiUrl = API_URL + from;  

        @SuppressWarnings("deprecation")
		HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
        conn.setRequestMethod("GET");
        

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("API request failed with response code: " + responseCode);
        }

        Scanner scanner = new Scanner(new InputStreamReader(conn.getInputStream()));
        StringBuilder jsonText = new StringBuilder();
        while (scanner.hasNext()) {
            jsonText.append(scanner.nextLine());
        }
        scanner.close();

        JsonObject jsonObject = JsonParser.parseString(jsonText.toString()).getAsJsonObject();
        JsonObject rates = jsonObject.getAsJsonObject("conversion_rates");

        if (!rates.has(to)) {
            throw new IOException("Invalid currency code.");
        }

        double exchangeRate = rates.get(to).getAsDouble();
        return amount * exchangeRate;
    }
}
