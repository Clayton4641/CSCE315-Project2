public class DataFrame {
    private String businessName;
    private String businessID;

    DataFrame(String businessName,String businessID){
        this.businessName = businessName;
        this.businessID = businessName;
    }

    public String getBusinessName() {
        return businessName;
    }

    public String getBusinessID() {
        return businessID;
    }
}
