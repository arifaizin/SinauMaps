package id.co.imastudio1.sinaumaps.model;

import java.util.ArrayList;

/**
 * Created by FUADMASKA on 6/16/2017.
 */

public class ResponseRoute {

    ArrayList<Kedua> routes = new ArrayList<>();

    public ArrayList<Kedua> getRoutes() {
        return routes;
    }

    public class Kedua {
        ArrayList<Keempat> legs = new ArrayList<>();
        public ArrayList<Keempat> getLegs(){
            return legs;
        }

        Ketiga overview_polyline;
        public Ketiga getOverview_polyline() {
            return overview_polyline;
        }

        public class Ketiga {
            String points;
            public String getPoints() {
                return points;
            }
        }
    }

//    ArrayList<RouteClass> routes = new ArrayList<>();
//
//    private class RouteClass {
//        ArrayList<LegClass> legs = new ArrayList();
//        ArrayList<PolylineClass> overview_polyline = new ArrayList<>();
//
//        private class LegClass {
//            DistanceClass distance;
//            DurationClass duration;
//
//            private class DistanceClass {
//                String text;
//                Double value;
//            }
//
//            private class DurationClass {
//                String text;
//                Double value;
//            }
//        }
//
//        private class PolylineClass {
//            String points;
//        }
//
//    }


}
