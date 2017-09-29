package id.co.imastudio1.sinaumaps.model;

public class Keempat {

    Distances distance;

    public Distances getDistance() {
        return distance;
    }

    public Durations getDuration() {
        return duration;

    }

    Durations duration;

    public class Distances {
        String text;
        Double value;

        public String getText() {
            return text;
        }

        public Double getValue() {
            return value;
        }
    }

    public class Durations {
        String text;
        int value;

        public String getText() {
            return text;
        }

        public int getValue() {
            return value;
        }
    }
}
