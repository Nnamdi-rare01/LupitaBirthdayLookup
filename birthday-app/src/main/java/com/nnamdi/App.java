package com.nnamdi;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Looks up birthdays using the instructor's JSON data and a HashMap.
 * Adapted from BirthdayExample.java.
 */
public class App {

    // Each person's full name is a key; their birthday is the value.
    private static final HashMap<String, String> birthdayMap =
            new HashMap<>();

    /**
     * Reads the JSON file and loads the birthdays into the map.
     */
    public static void initializeMap(String pathToFile)
            throws IOException, ParseException {

        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(pathToFile)) {
            JSONArray people = (JSONArray) parser.parse(reader);

            birthdayMap.clear();

            for (Object person : people) {
                JSONObject entry = (JSONObject) person;
                String name = (String) entry.get("name");
                String birthday = (String) entry.get("birthday");

                birthdayMap.put(name, birthday);
            }
        }
    }

    public static void main(String[] args) {
        // Supports running from the project folder or repository folder.
        String pathToFile = "birthday.json";

        if (!new File(pathToFile).isFile()) {
            pathToFile = "birthday-app/birthday.json";
        }

        try {
            initializeMap(pathToFile);
        } catch (IOException | ParseException error) {
            System.out.println("Could not load the birthday data.");
            System.out.println(
                    "Check that birthday.json is inside birthday-app.");
            System.out.println("Details: " + error.getMessage());
            return;
        }

        System.out.println("Welcome to Lupita's Birthday Lookup!");
        System.out.println("Loaded " + birthdayMap.size() + " birthdays.");
        System.out.println(
                "Enter a full name using the spelling and capitalization");
        System.out.println("shown in the data. Type quit to exit.");

        try (Scanner input = new Scanner(System.in)) {
            while (true) {
                System.out.print("\nEnter a full name: ");

                if (!input.hasNextLine()) {
                    break;
                }

                String name = input.nextLine().trim();

                if (name.equalsIgnoreCase("quit")) {
                    System.out.println("Goodbye!");
                    break;
                }

                if (name.isEmpty()) {
                    System.out.println("Please enter a name.");
                    continue;
                }

                // Retrieve the birthday directly using the HashMap.
                String birthday = birthdayMap.get(name);

                if (birthday != null) {
                    System.out.println(
                            name + "'s birthday is " + birthday + ".");
                } else {
                    System.out.println(
                            "No birthday found for \"" + name + "\".");
                }
            }
        }
    }
}