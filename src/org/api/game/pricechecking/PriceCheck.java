package org.api.game.pricechecking;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PriceCheck {

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();
    private static Map<Integer, Integer> rsPriceCache = new HashMap<>();
    private static JsonObject osbuddySummaryJson;

    /**
     * Gets the price of the item id from the RuneScape website. Retrieved values are cached for later use.
     *
     * @param id The id of the item.
     * @return The price of the item; 0 otherwise.
     */
    public static int getRSPrice(int id) throws IOException {
        if (getRsPriceCache().containsKey(id))
            return getRsPriceCache().get(id);

        final Request request = new Request.Builder()
                .url("http://services.runescape.com/m=itemdb_oldschool/api/catalogue/detail.json?item=" + id)
                .get()
                .build();
        final Response response = HTTP_CLIENT.newCall(request).execute();
        if (!response.isSuccessful())
            return 0;

        if (response.body() == null)
            return 0;

        final Gson gson = new Gson().newBuilder().create();
        final String priceText = gson.fromJson(response.body().string(), JsonObject.class)
                .getAsJsonObject("item")
                .getAsJsonObject("current")
                .get("price")
                .getAsString();
        final int price = Integer.parseInt(priceText.replaceAll("\\D+", ""));
        final int formattedPrice = priceText.matches("[0-9]+") ? price : price * (priceText.charAt(0) == 'k' ? 1000 : 1000000);
        getRsPriceCache().put(id, formattedPrice);
        return formattedPrice;
    }

    /**
     * Sets the OSBuddy price summary json.
     */
    private static void getOSBuddySummaryJson() throws IOException {
        final Request request = new Request.Builder()
                .url("https://storage.googleapis.com/osbuddy-exchange/summary.json")
                .get()
                .build();
        final Response response = HTTP_CLIENT.newCall(request).execute();
        if (!response.isSuccessful())
            return;

        if (response.body() == null)
            return;

        final Gson gson = new Gson().newBuilder().create();
        setOsbuddySummaryJson(gson.fromJson(response.body().string(), JsonObject.class));
    }

    /**
     * Gets the price of the item id from the OSBuddy price summary json. The entire summary data is stored upon first
     * retrieval.
     *
     * @param id The id of the item.
     * @return The price of the item; 0 otherwise.
     */
    public static int getOSBuddyPrice(int id) throws IOException {
        if (getOsbuddySummaryJson() == null)
            getOSBuddySummaryJson();

        final JsonObject jsonObject = getOsbuddySummaryJson().getAsJsonObject(Integer.toString(id));
        if (jsonObject == null)
            return 0;

        return jsonObject.get("sell_average").getAsInt();
    }

    /**
     * Gets the price of the item name from the OSBuddy price summary json. The entire summary data is stored upon first
     * retrieval. This method is a little slower than using the item id.
     *
     * @param name The name of the item.
     * @return The price of the item; 0 otherwise.
     */
    public static int getOSBuddyPrice(String name) throws IOException {
        if (getOsbuddySummaryJson() == null)
            getOSBuddySummaryJson();

        Map.Entry<String, JsonElement> entry = getOsbuddySummaryJson().entrySet().stream().filter(a -> a.getValue().getAsJsonObject().get("name").getAsString().equals(name)).findFirst().orElse(null);
        if (entry == null)
            return 0;

        return entry.getValue().getAsJsonObject().get("sell_average").getAsInt();
    }

    private static JsonObject getOsbuddySummaryJson() {
        return osbuddySummaryJson;
    }

    private static void setOsbuddySummaryJson(JsonObject osbuddySummaryJson) {
        PriceCheck.osbuddySummaryJson = osbuddySummaryJson;
    }

    private static Map<Integer, Integer> getRsPriceCache() {
        return rsPriceCache;
    }
}
