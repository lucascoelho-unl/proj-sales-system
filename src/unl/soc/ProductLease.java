package unl.soc;

import com.google.gson.annotations.Expose;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

/**
 * The ProductLease class represents a leased product.
 * It extends the Item class and includes fields for total lease time,
 * price, first month price, and markup price.
 * It includes Getters, ToString, HashCode and Equals methods
 */
@XStreamAlias("productLease")
public class ProductLease extends Item {
    @XStreamOmitField
    private LocalDate startDate;
    @XStreamOmitField
    private LocalDate endDate;
    @Expose
    private double price = getBasePrice();

    public ProductLease(String uniqueCode, String itemType, String name, double basePrice) {
        super(uniqueCode, itemType , name ,basePrice);
    }

    public ProductLease(Item productBeingLeased, String startDate, String endDate) {
        super(productBeingLeased.getUniqueCode(), productBeingLeased.getName(), productBeingLeased.getItemType() ,productBeingLeased.getBasePrice());
        this.startDate = LocalDate.parse(startDate);
        this.endDate = LocalDate.parse(endDate);
    }

    public int getPeriodInMonths() {
        Period period = Period.between(this.startDate, this.endDate);
        return period.getYears() * 12 + period.getMonths();
    }

    public double getBasePrice() {
        return super.getBasePrice();
    }

    public double getMarkupPrice() {
        return getBasePrice() / 2;
    }

    public double getTotalLeasePrice() {
        return getMarkupPrice() + getBasePrice();
    }

    public double getFirstMonthPrice() {
        return getTotalLeasePrice() / getPeriodInMonths();
    }

    @Override
    public double getGrossPrice() {
        return getFirstMonthPrice();
    }

    @Override
    public double getTotalTax() {
        return 0;
    }

    @Override
    public String toString() {
        return String.format("%s - Lease for %s months \n %60s %9.2f $%9.2f", getName() + " (" + getUniqueCode() + ")", getPeriodInMonths(), "$", getTotalTax(), getGrossPrice());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductLease that = (ProductLease) o;
        return Double.compare(super.getBasePrice(), that.getBasePrice()) == 0 && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), startDate, endDate, super.getBasePrice());
    }
}