package com.example.mycharacterfrequency;

import javafx.scene.canvas.GraphicsContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class HistogramAlphaBet {
    //Maps for the frequency and probability histograms
    Map<Character, Integer> frequency = new HashMap<>();
    Map<Character, Double> probability = new HashMap<>();

    //Constructors
    HistogramAlphaBet() {}

    HistogramAlphaBet(Map<Character, Integer> m) {frequency.putAll(m);}

    HistogramAlphaBet(HistogramAlphaBet h) {
        frequency.putAll(h.getFrequency());
        probability.putAll(h.getProbability());
    }

    HistogramAlphaBet(String text) {
        String w = text.replaceAll("[^a-zA-Z]", "").toLowerCase();
        for (int i = 0; i < w.length(); i++) {
            Character key = w.charAt(i);
            incrementFrequency(frequency, key);
        }
        probability = getProbability();
    }


    HistogramAlphaBet(ResultSet RS, String fieldKey, String fieldValue) {
        //input is database ResultSet object produced by SQL query
        //retrieves <key, value> pairs from a ResultSet
        try {
            while (RS.next()) {
                frequency.put(RS.getString(fieldKey).charAt(0), RS.getInt(fieldKey));
            }
        }
        catch (NoSuchElementException | IllegalStateException | SQLException e) {System.out.println(e); }
    }


    //Get the frequency map and cumulative frequency
    public Map<Character, Integer> getFrequency() {return frequency;}
    public Integer getCumulativeFrequency() {return frequency.values().stream().reduce(0,Integer::sum); }


    //Sort the frequency map in increasing order of values
    public Map<Character, Integer> sortUpFrequency() {
        return frequency
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    //Sort the frequency map in decreasing order of values
    public Map<Character, Integer> sortDownFrequency() {
        return frequency
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    //Get Probability
    public Map<Character, Double> getProbability() {
        double inverseCumulativeFrequency = 1.0 / getCumulativeFrequency();
        for (Character Key : frequency.keySet()) {
            probability.put(Key, (double) frequency.get(Key) * inverseCumulativeFrequency);
        }
        return probability;
    }

    //Sort the probability map in increasing order of their values
    public Map<Character, Double> sortUpProbability() {
        return getProbability().entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    //Sort the probability map in decreasing order of their values
    public Map<Character, Double> sortDownProbability() {
        return getProbability().entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    //Get sum of probabilities
    public Double getSumOfProbability() {return probability.values().stream().reduce(0.0, Double::sum); }

    //Check sum of probabilities == 1
    public boolean checkSumOfProbability() {return getSumOfProbability() == 1;}

    @Override
    public String toString(){
        String output = "Frequency of Characters: \n";
        for (Character K : frequency.keySet()) {
            output += K + ": " + frequency.get(K) + "\n";
        }
        return output;
    }

    public class MyPieChart {
        Map<Character, Slice> slices = new HashMap<>();

        int N;                  // Number of events [chars] to display
        int M;                  // Max of events [chars] to display [26]
        MyPoint center;         // Center of pie chart
        double width, height;   // Width and height of elliptic chart
        double rotateAngle;     // The starting angle [in degrees] of first slice

        MyPieChart(int N, int M, MyPoint center, double width, double height, double rotateAngle) {
            this.N = N; this.M = M;
            this.center = center;
            this.width = width; this.height = height;
            this.rotateAngle = rotateAngle < 360 ? rotateAngle : rotateAngle - 360;

            slices = getMyPieChart();
        }

        //Get methods
        public Map<Character, Slice> getMyPieChart() {
            MyColor [] colors = MyColor.getMyColors();
            int colorSize = colors.length;

            Map<Character, Double> sortedProbability = sortDownProbability();
            Random rand = new Random();
            double sliceStartAngle = this.rotateAngle;

            for (Character key : sortedProbability.keySet()) {
                double sliceValue = sortedProbability.get(key);
                double sliceArcAngle = 360 * sliceValue;

                MyColor color = colors[rand.nextInt(colorSize)];
                String sliceInformation = key + ": " + String.format("%.4f", sliceValue);
                slices.put(key, new Slice(center, width, height, sliceStartAngle, sliceArcAngle, color, sliceInformation));

                sliceStartAngle += sliceArcAngle;
                sliceStartAngle = sliceStartAngle < 360.0 ? sliceStartAngle : sliceStartAngle - 360.0;
            }
            return slices;
        }

        public void draw(GraphicsContext GC) {
            Map<Character, Double> sortedProbability = sortDownProbability();

            //Prepare canvas for drawing
            GC.clearRect(0.0, 0.0, GC.getCanvas().getWidth(), GC.getCanvas().getHeight());
            GC.setFill(MyColor.GREY.getJavaFXColor());
            GC.fillRect(0.0, 0.0, GC.getCanvas().getWidth(), GC.getCanvas().getHeight());

            int n = 0;
            double probabilityAllOtherCharacters = 1.0;
            for (Character key : sortedProbability.keySet()) {
                double sliceStartAngle = slices.get(key).getStartAngle();
                double sliceArcAngle = slices.get(key).getArcAngle();

                if (n < N) {
                    slices.get(key).draw(GC);
                    probabilityAllOtherCharacters -= sortedProbability.get(key);
                    n++;
                }
                else {
                    if (N != M) {
                        String information = "All other characters:\n" + String.format("%.4f", probabilityAllOtherCharacters);
                        if (sliceStartAngle < rotateAngle) {
                            Slice sliceAllOtherCharacters = new Slice(center, width, height, sliceStartAngle,
                                    rotateAngle - sliceStartAngle, MyColor.getRandomColor(), information);
                            sliceAllOtherCharacters.draw(GC);
                        } else {
                            Slice sliceAllOtherCharacters = new Slice(center, width, height, sliceStartAngle,
                                    360.0 - sliceStartAngle + rotateAngle, MyColor.getRandomColor(), information);
                            sliceAllOtherCharacters.draw(GC);
                        }
                        break;
                    }
                }
            }
        }
    }


    //Helper function -- increment the value by one for given key
    private static<K> void incrementFrequency(Map<K, Integer> m, K Key) {
        m.putIfAbsent(Key, 0);
        m.put(Key, m.get(Key) +1);
    }
}
