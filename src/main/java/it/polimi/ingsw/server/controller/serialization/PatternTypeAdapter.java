package it.polimi.ingsw.server.controller.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import it.polimi.ingsw.server.model.managers.patterns.AdjacentPattern;
import it.polimi.ingsw.server.model.managers.patterns.Pattern;
import it.polimi.ingsw.server.model.managers.patterns.PersonalPattern;
import it.polimi.ingsw.server.model.managers.patterns.SpecificPattern;

import java.io.IOException;
import java.time.LocalDateTime;

public class PatternTypeAdapter extends TypeAdapter<Pattern> {

    private final Gson gson =
            new GsonBuilder().registerTypeAdapterFactory(OptionalTypeAdapter.FACTORY).registerTypeAdapter(LocalDateTime.class, new DateTimeTypeAdapter()).create();

    @Override
    public void write(JsonWriter jsonWriter, Pattern pattern) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("type");
        if (pattern instanceof AdjacentPattern) {
            jsonWriter.value("AdjacentPattern");
            jsonWriter.name("Pattern");
            gson.toJson(pattern, AdjacentPattern.class, jsonWriter);
        } else if (pattern instanceof PersonalPattern){
            jsonWriter.value("PersonalPattern");
            jsonWriter.name("Pattern");
            gson.toJson(pattern, PersonalPattern.class, jsonWriter);
        } else if (pattern instanceof SpecificPattern){
            jsonWriter.value("SpecificPattern");
            jsonWriter.name("Pattern");
            gson.toJson(pattern, SpecificPattern.class, jsonWriter);
        } else {
            throw new JsonSyntaxException("Pattern not recognized");
        }
        jsonWriter.endObject();
    }

    @Override
    public Pattern read(JsonReader jsonReader) throws IOException {
        jsonReader.beginObject();
        Pattern pattern;
        if (jsonReader.nextName().equals("type")) {
            switch (jsonReader.nextString()) {
                case "AdjacentPattern" -> {
                    jsonReader.nextName();
                    pattern = gson.fromJson(jsonReader, AdjacentPattern.class);
                }
                case "PersonalPattern" -> {
                    jsonReader.nextName();
                    pattern = gson.fromJson(jsonReader, PersonalPattern.class);
                }
                case "SpecificPattern" -> {
                    jsonReader.nextName();
                    pattern = gson.fromJson(jsonReader, SpecificPattern.class);
                }
                default -> throw new JsonSyntaxException("Pattern not recognized");
            }
        } else {
            throw new JsonSyntaxException("Pattern not recognized");
        }
        jsonReader.endObject();
        return pattern;
    }
}
