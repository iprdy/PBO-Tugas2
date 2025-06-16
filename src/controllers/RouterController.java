package controllers;

import api.Response;
import models.Villas;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RouterController {
    public static void handleGetVillaById(Response res) throws SQLException {
        VillasController vc = new VillasController();
        List<Villas> villa = vc.getAllVillas();

        ResponseController.sendJsonResponse(villa, res);
    }
}
