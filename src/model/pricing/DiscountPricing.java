package model.pricing;

public class DiscountPricing implements PricingStrategy {
    private double discountPercentage;

    public DiscountPricing(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    @Override
    public double calculatePrice(double basePrice, long numberOfNights) {
        double totalPrice = basePrice * numberOfNights;
        double discount = totalPrice * (discountPercentage / 100.0);
        return totalPrice - discount;
    }

    @Override
    public String getStrategyName() {
        return "Discount Pricing (" + discountPercentage + "% off)";
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
}