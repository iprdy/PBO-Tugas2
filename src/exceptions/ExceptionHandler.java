package exceptions;

import api.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import controllers.ResponseController;

import java.net.HttpURLConnection;
import java.sql.SQLException;

public class ExceptionHandler {
    public static void handleException(Exception e, Response res) {
        if (e instanceof DataNotFoundException)
            ResponseController.sendErrorResponse(res, "Data tidak ditemukan", e.getMessage(), HttpURLConnection.HTTP_NOT_FOUND);

        else if (e instanceof BadRequestException) {
            ResponseController.sendErrorResponse(res, "Input tidak valid", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        }

        else if (e instanceof NumberFormatException) {
            ResponseController.sendErrorResponse(res, "ID pada path tidak valid", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        }

        else if (e instanceof JsonMappingException) {
            ResponseController.sendErrorResponse(res, "Invalid data structure", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        }

        else if (e instanceof JsonProcessingException) {
            ResponseController.sendErrorResponse(res, "Invalid JSON format", e.getMessage(), HttpURLConnection.HTTP_BAD_REQUEST);
        }

        else if (e instanceof SQLException) {
            ResponseController.sendErrorResponse(res, "Database error", e.getMessage(), HttpURLConnection.HTTP_INTERNAL_ERROR);
        }

        else {
            String message = e.getMessage() != null ? e.getMessage() : e.toString();
            ResponseController.sendErrorResponse(res, "Unexpected error", message, HttpURLConnection.HTTP_INTERNAL_ERROR);
        }
    }
}
