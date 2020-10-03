import java.lang.Math;

/**
 * DistanceFinder is a class containing static functions used to help find the distance
 * between longitude and latitude points.
 */
public class DistanceFinder {

	public static double RADIUS_OF_EARTH_KM = 6371.0;
	
	/**
	 * Given an array of GeoLocation, find the greatest distance between each point.
	 * 
	 * @param coordinates An array of GeoLocation which holds pairs of longitudes and latitudes
	 * @return 0.0 if there are less than 2 GeoLocations. Otherwise, returns the greatest distance between 2 points among all the points.
	 */
	public static double longestDistance(GeoLocation[] coordinates) {

		if (coordinates.length <= 1) {
			System.out.println("Not a franchise.");
			return 0.0;
		}

		double biggestDistance = 0.0;

		// No duplicate points i.e. no [1, 0] if [0, 1] is already calculated.
		for (int i = 0; i < coordinates.length; ++i) {
			for (int j = i + 1; j < coordinates.length; ++j) {

				//System.out.println("[" + i + "][" + j + "]");

				double longitudeRadiansI = Math.toRadians(coordinates[i].longitude);
				double latitudeRadiansI = Math.toRadians(coordinates[i].latitude);
				double longitudeRadiansJ = Math.toRadians(coordinates[j].longitude);
				double latitudeRadiansJ = Math.toRadians(coordinates[j].latitude);

				double omega = latitudeRadiansJ - latitudeRadiansI;
				double phi = longitudeRadiansJ - longitudeRadiansI;

				double a = Math.sin(omega / 2) * Math.sin(omega / 2) +
							Math.cos(latitudeRadiansI) * Math.cos(latitudeRadiansJ) *
							Math.sin(phi / 2) * Math.sin(phi / 2);

				double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

				double distance = RADIUS_OF_EARTH_KM * c;

				if (biggestDistance < distance) {
					biggestDistance = distance;
				}
			}
		}

		return biggestDistance;
	}
}
