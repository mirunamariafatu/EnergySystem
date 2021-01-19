package filesystem;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import utils.Constants;


import java.io.FileReader;

@SuppressWarnings("deprecation")
public final class InputLoader {
    /**
     * The path to the input file
     */
    private final String inputTest;

    public InputLoader(final String inputTest) {
        this.inputTest = inputTest;
    }

    /**
     * The method reads the input file
     * @return an Input object
     * @throws Exception in case of exceptions to reading
     */
    public Input initializeInput() throws Exception {
        JSONParser jsonParser = new JSONParser();

        // Parsing the contents of the JSON file
        JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(inputTest));
        long numberOfTurns = (long) jsonObject.get(Constants.NUMBER_OF_TURNS);
        JSONObject initialData = (JSONObject) jsonObject.get(Constants.INITIAL_DATA);
        JSONArray monthlyUpdates = (JSONArray) jsonObject.get(Constants.MONTHLY_UPDATES);

        return new Input(numberOfTurns, initialData, monthlyUpdates);
    }
}
