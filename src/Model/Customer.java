package Model;

public class Customer extends Address {

    private Integer customerId;
    private String customerName;
    private Integer addressId;
    private Integer active;

    public Customer() {}

    public Integer getCustomerId() { return customerId; }

    public String getCustomerName() { return customerName; }

    public Integer getAddressId() { return addressId; }

    public Integer getActive() { return active; }

    public void setCustomerId(Integer customerId) { this.customerId = customerId;}

    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public void setAddressId(Integer addressId) { this.addressId = addressId; }

    public void setActive(Integer active) { this.active = active; }
}
