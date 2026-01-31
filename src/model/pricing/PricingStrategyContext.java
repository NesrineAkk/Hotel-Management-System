package model.pricing;

public class PricingStrategyContext {
    private PricingStrategy strategy;

    public void setStrategy(PricingStrategy strategy) {
        this.strategy = strategy;
    }

    public double calculatePrice(double basePrice, long numberOfNights) {
        if (strategy == null) {
            strategy = new NormalPricing();
        }
        return strategy.calculatePrice(basePrice, numberOfNights);
    }


    public String getCurrentStrategyName() {
        if (strategy == null) {
            return "No strategy set (using default)";
        }
        return strategy.getStrategyName();
    }
}