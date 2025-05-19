package com.mycompany.oraclemoneda;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.*;
import java.util.Scanner;

public class OracleMoneda {

    public static void main(String[] args) {
        System.out.println("Sea bienvenido al conversor de moneda!");
        Scanner consola = new Scanner(System.in);
        int opcion = 0;
        while(opcion != 5){
            System.out.println("1. Dolar -> Peso mexicano");
            System.out.println("2. Peso mexicano -> Dolar");
            System.out.println("3. Dolar -> Euro");
            System.out.println("4. Euro -> Dolar");
            System.out.println("5. Salir");
            
            System.out.println("Elige una opción: ");
            opcion = consola.nextInt();
            switch (opcion) {
                case 1:
                    conversion("USD", "MXN");
                    break;
                case 2:
                    conversion("MXN", "USD");
                    break;
                case 3:
                    conversion("USD", "EUR");
                    break;
                case 4:
                    conversion("EUR", "USD");
                    break;
                case 5:
                    break;
                default:
                    throw new AssertionError();
            }
        }
    }
    
    public static void conversion(String monedaACambiar, String monedaCambio){
        // Tu api guardada como variable de entorno
        final String KEY = System.getenv("APICAMBIO");
        
        final String apiUrl = "https://v6.exchangerate-api.com/v6/" + KEY + "/pair/" + monedaACambiar + "/" + monedaCambio;
        
        HttpClient client = HttpClient.newHttpClient();
        // Construye la solicitud HTTP
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(apiUrl))
            .header("Authorization", "Bearer " + KEY) // Agrega la API Key en los headers
            .header("Accept", "application/json")
            .GET() // Método GET
            .build();

        try {
            // Envía la solicitud y obtiene la respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                //System.out.println("Respuesta de la API: " + response.statusCode());
                JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
                System.out.println("1 " + monedaACambiar + " = " + jsonResponse.get("conversion_rate").getAsString() +" " + monedaCambio);


            } else {
                System.out.println("Error: Código de respuesta " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
