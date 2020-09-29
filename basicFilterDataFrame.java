/**
 * Data frame to contain all the information inputted from a user to conduct an initial search
 */

import java.io.*;
import java.util.*;

public class basicFilterDataFrame {
    public String businessName;
    public String state;
    public String city;
    public String postal;
    public String street;

    public String ambienceItems;
    public String goodForMeal;
    public String dietaryRestrictions;

    public int lowerStars;
    public int upperStars;

    public boolean isOpen;
    public boolean isRestaurant;
    public boolean saveToFile;
    public boolean businessIDs;
    public boolean address;
    public boolean parking;

    basicFilterDataFrame(String businessName, String state, String city, String postal, String street, int lowerStars,
                         int upperStars, boolean isOpen, boolean isRestaurant,boolean saveToFile,boolean businessIDs,
                         boolean address,boolean parking,String ambienceItems,String goodForMeal,
                         String dietaryRestrictions) {
        this.businessName = businessName;
        this.state = state;
        this.city = city;
        this.postal = postal;
        this.street = street;

        this.lowerStars = lowerStars;
        this.upperStars = upperStars;

        this.isOpen = isOpen;
        this.isRestaurant = isRestaurant;
        this.saveToFile = saveToFile;
        this.businessIDs = businessIDs;
        this.address = address;
        this.parking = parking;

        this.ambienceItems = ambienceItems;
        this.goodForMeal = goodForMeal;
        this.dietaryRestrictions = dietaryRestrictions;
    }

    public String getAddressQuery() {
        String address_string = "";
        if (!street.equals("")) {
            address_string = address_string + "address = '" + street + "' ";
        }

        if (!postal.equals("")) {
            if (!address_string.equals("")) {
                address_string = address_string + "AND ";
            }
            address_string = address_string + "postal_code = '" + postal + "' ";
        }

        if (!city.equals("")) {
            if (!address_string.equals("")) {
                address_string = address_string + "AND ";
            }
            address_string = address_string + "city = '" + city + "' ";
        }

        if (!state.equals("")) {
            if (!address_string.equals("")) {
                address_string = address_string + "AND ";
            }
            address_string = address_string + "state = '" + state + "' ";
        }

        if (!address_string.equals("")) {
            address_string = "SELECT business_id FROM address WHERE " + address_string;
        }

        return address_string;
    }

    public String getBusinessQuery() {
        String business_string = "";
        if (!businessName.equals("")) {
            business_string = business_string + "name = '" + businessName + "' ";
        }

        if (lowerStars != 1 || upperStars != 5) {
            if (!business_string.equals("")) {
                business_string = business_string + "AND ";
            }
            business_string = business_string + "(";
            for (int i = lowerStars; i <= upperStars; i++) {
                if (i != lowerStars) {
                    business_string = business_string + "OR ";
                }
                business_string = business_string + "stars LIKE '" + i + ".%' ";
            }
            business_string = business_string + ") ";
        }

        if (isOpen) {
            if (!business_string.equals("")) {
                business_string = business_string + "AND ";
            }
            business_string = business_string + "is_open = '1' ";
        }

        return business_string;
    }

    public String getRestaurantQuery() {
        if (!isRestaurant) {
            return "";
        }
        //String restaurant_string = "SELECT business_id FROM restaurants";
        return "SELECT business_id FROM restaurants ";
    }

    public String getAttributeQuery() {
        String attribute_string = "";
        String attribute_source = "";
        String id_source = "";
        if (!ambienceItems.equals("") && !ambienceItems.equals("None")) {
            attribute_source += "ambience";
            attribute_string += "ambience." + ambienceItems + " = 'True'";
            id_source = "ambience";
        }

        if (!goodForMeal.equals("") && !goodForMeal.equals("None")) {
            if (!attribute_source.equals("")){
                attribute_source += ", ";
                attribute_string += " AND ";
            }
            attribute_source += "goodformeal";
            attribute_string += "goodformeal." + goodForMeal + " = 'True'";
            if (id_source.equals("ambience")) {
                attribute_string += " AND ambience.business_id = goodformeal.business_id";
            } else {
                id_source = "goodformeal";
            }
        }

        if (!dietaryRestrictions.equals("") && !dietaryRestrictions.equals("None")) {
            if (!attribute_source.equals("")){
                attribute_source += ", ";
                attribute_string += " AND ";
            }
            attribute_source += "dietaryrestrictions";
            attribute_string += "dietaryrestrictions." + dietaryRestrictions + " = 'True'";
            if (id_source.equals("ambience")) {
                attribute_string += " AND ambience.business_id = dietaryrestrictions.business_id";
            }
            else if (id_source.equals("goodformeal")) {
                attribute_string += " AND goodformeal.business_id = dietaryrestrictions.business_id";
            }
            id_source = "dietaryrestrictions";
        }
        if (attribute_string.equals("")){
            return attribute_string;
        }

        attribute_string = "SELECT " + id_source + ".business_id FROM " + attribute_source + " WHERE " + attribute_string + " ";

        return attribute_string;
    }

    public String getHeader(){
        String header = "SELECT businesses.name";
        String header_source = " FROM businesses";
        String id_equating = "";
        if (businessIDs) {
            header += ", businesses.business_id";
        }
        if (address) {
            header_source += ", address";
            header += ", address.address, address.city, address.state, address.postal_code";
            id_equating += " businesses.business_id = address.business_id ";
        }
        if (parking) {
            header_source += ", businessparking";
            header += ", businessparking.garage, businessparking.street, businessparking.lot, businessparking.valet";
            if (!id_equating.equals("")) {
                id_equating += "AND";
            }
            id_equating += " businesses.business_id = businessparking.business_id ";
        }

        header += header_source;

        if (id_equating.equals("")) {
            return header;
        }


        header = header + " WHERE" + id_equating;
        return header;
    }

    public String getQuery(){
        String address_string = getAddressQuery();
        String business_string = getBusinessQuery();
        String restaurant_string = getRestaurantQuery();
        String attribute_string = getAttributeQuery();

        String query = getHeader();

        if (address_string.equals("") && business_string.equals("") && restaurant_string.equals("") && attribute_string.equals("")) {
            return query;
        }
        int number_of_parenthesis = 0;

        if (!business_string.equals("")) {
            if (address || parking) {
                query += "AND ";
            } else {
                query += " WHERE ";
            }
            query = query + business_string;
            if (!address_string.equals("") || !restaurant_string.equals("") || !attribute_string.equals("")) {
                query = query + "AND businesses.business_id IN (";
            }
        } else {
            if (address || parking) {
                query += "AND ";
            } else {
                query += " WHERE ";
            }
            query = query + "businesses.business_id IN (";
        }
        //else if (!address_string.equals("") || !restaurant_string.equals("") || !attribute_string.equals("")){
        //    query = query + "AND businesses.business_id IN (";
        //}

        number_of_parenthesis++;


        if (!attribute_string.equals("")) {
            query = query + attribute_string;
            if (!address_string.equals("")) {
                if (!dietaryRestrictions.equals("") && !dietaryRestrictions.equals("None")){
                    query = query + "AND dietaryrestrictions.business_id IN (";
                }
                else if (!goodForMeal.equals("") && !goodForMeal.equals("None")){
                    query = query + "AND goodformeal.business_id IN (";
                }
                else if (!ambienceItems.equals("") && !ambienceItems.equals("None")){
                    query = query + "AND ambience.business_id IN (";
                }

                number_of_parenthesis++;
            }
        } else if (!restaurant_string.equals("")) {
            query = query + restaurant_string;
            if (!address_string.equals("")) {
                query = query + "WHERE business_id IN (";
                number_of_parenthesis++;
            }
        }

        if (!address_string.equals("")) {
            query = query + address_string;
        }

        for (int i = 0; i < number_of_parenthesis; i++) {
            query = query + ")";
        }

        return query;
    }
}

