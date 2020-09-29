/**
 * Data frame to contain all the information inputted from a user to conduct an initial search
 */
public class basicFilterDataFrame {
    public String businessName;
    public String state;
    public String city;
    public String postal;
    public String street;

    public int lowerStars;
    public int upperStars;

    public boolean isOpen;
    public boolean isRestaurant;
    public boolean saveToFile;

    basicFilterDataFrame(String businessName, String state, String city, String postal, String street, int lowerStars,
                         int upperStars, boolean isOpen, boolean isRestaurant,boolean saveToFile) {
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
        if (!business_string.equals("")) {
            business_string = "SELECT name, business_id FROM businesses WHERE " + business_string;
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

    public String getQuery(){
        String address_string = getAddressQuery();
        String business_string = getBusinessQuery();
        String restaurant_string = getRestaurantQuery();
        String query = "";
        int number_of_parenthesis = 0;

        if (!business_string.equals("")) {
            query = query + business_string;
            if (!address_string.equals("") || !restaurant_string.equals("")) {
                query = query + "AND business_id IN (";
                number_of_parenthesis++;
            }
        } else {
            query = query + "SELECT name, business_id FROM businesses WHERE business_id IN (";
            number_of_parenthesis++;
        }

        if (!restaurant_string.equals("")) {
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
