package id.co.imastudio1.sinaumaps.model;

import java.util.ArrayList;

/**
 * Created by idn on 9/29/2017.
 */

public class PlaceModel {


    ArrayList<RouteClass> routes = new ArrayList<>();
    public ArrayList<RouteClass> getRoutes() {
        return routes;
    }

    public class RouteClass {
        ArrayList<LegClass> legs = new ArrayList();
        public ArrayList<LegClass> getLegs() {
            return legs;
        }

        PolylineClass overview_polyline;
        public class PolylineClass {
            String points;
            public String getPoints() {
                return points;
            }

        }
        public PolylineClass getOverview_polyline() {
            return overview_polyline;
        }

        public class LegClass {
            DistanceClass distance;
            DurationClass duration;

            public DistanceClass getDistance() {
                return distance;
            }

            public DurationClass getDuration() {
                return duration;
            }

            public class DistanceClass {
                String text;
                Double value;

                public String getText() {
                    return text;
                }

                public Double getValue() {
                    return value;
                }
            }

            public class DurationClass {
                String text;
                Double value;

                public String getText() {
                    return text;
                }

                public Double getValue() {
                    return value;
                }
            }
        }



    }

//    {
//        "routes" : [
//        {
//            "legs" : [
//            {
//                "distance" : {
//                "text" : "316 km",
//                        "value" : 315525
//            },
//                "duration" : {
//                "text" : "7 jam 0 menit",
//                        "value" : 25215
//            },
//                "steps" : [
//                {
//                    "polyline" : {
//                    "points" : "lewi@i~`aTq@M"
//                }
//               }
//             ]
//          }
//        ]
//      }
//   ]
// }
}
