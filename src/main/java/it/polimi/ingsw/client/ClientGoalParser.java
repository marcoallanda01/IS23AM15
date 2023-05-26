package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClientGoalParser {
    public static Map<String, ClientGoalDetail> parseGoalsFromJsonFile(URL url) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        Gson gson = new Gson();
        JsonReader jsonReader = new JsonReader(reader);
        ClientGoal[] goals = gson.fromJson(jsonReader, ClientGoal[].class);
        jsonReader.close();
        reader.close();
        return List.of(goals).stream().collect(Collectors.toMap(ClientGoal::getName, e -> e));
    }
}