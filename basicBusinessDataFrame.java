public class basicBusinessDataFrame {
    private String businessName;
    private String businessID;

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
