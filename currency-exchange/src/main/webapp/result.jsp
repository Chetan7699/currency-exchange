<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <title>Conversion Result</title>
    <link rel="stylesheet" href="css/jsp-style.css" />
</head>
<body>
    <div class="container">
        <h2>Conversion Result</h2>

        <% if (request.getAttribute("convertedAmount") != null) { %>
            <p><%= request.getAttribute("amount") %> <%= request.getAttribute("fromCurrency") %>  
            = <%= request.getAttribute("convertedAmount") %> <%= request.getAttribute("toCurrency") %></p>
        <% } else { %>
            <p class="error-message">Conversion failed. Please try again.</p>
        <% } %>

       <a href="index.html"><button>Convert Again</button> </a>
    </div>
</body>
</html>
