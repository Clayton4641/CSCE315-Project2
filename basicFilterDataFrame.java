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
}
