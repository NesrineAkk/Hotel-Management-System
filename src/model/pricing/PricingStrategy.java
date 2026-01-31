package model.pricing;

public interface PricingStrategy {

    double calculatePrice(double basePrice, long numberOfNights);

    String getStrategyName();
}