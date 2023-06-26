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

/**
 * Class that parses the goals from a json internal to the jar
 */
public class ClientGoalParser {
    /**
     * Parses the goals from a json file
     * @param resourcePath the path to the json
     * @return the list of goals
     * @throws IOException if a problem occurs while reading the goals
     */
    public static Map<String, ClientGoalDetail> parseGoalsFromJsonFile(String resourcePath) throws IOException {
        InputStream inputStream = ClientGoalParser.class.getResourceAsStream(resourcePath);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        Gson gson = new Gson();
        JsonReader jsonReader = new JsonReader(reader);
        ClientGoal[] goals = gson.fromJson(jsonReader, ClientGoal[].class);
        jsonReader.close();
        reader.close();
        return List.of(goals).stream().collect(Collectors.toMap(ClientGoal::getName, e -> e));
    }
}