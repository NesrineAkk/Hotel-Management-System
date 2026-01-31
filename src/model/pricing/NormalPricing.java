package model.pricing;

public class NormalPricing implements PricingStrategy {

    @Override
    public double calculatePrice(double basePrice, long numberOfNights) {
        return basePrice * numberOfNights;
    }

    @Override
    public String getStrategyName() {
        return "Normal Pricing";
    }
}