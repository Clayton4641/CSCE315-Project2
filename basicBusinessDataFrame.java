/**
 * General information class intended for storing data from a initial business search
 */
public class basicBusinessDataFrame {
    public String businessName;
    public String businessID;

    basicBusinessDataFrame(String businessName, String businessID) {
        this.businessName = businessName;
        this.businessID = businessID;
    }

    public String getBusinessName() {
        return businessName;
    }

    public String getBusinessID() {
        return businessID;
    }
}
