package ch.chrummibei.silvercoin;

/**
 * A PricedItemPosition that also keeps track of the realised profit.
 */
public class YieldingItemPosition extends PricedItemPosition{
    private TotalValue realisedProfit = new TotalValue(0);

    public YieldingItemPosition(Item item) {
        super(item);
    }

    public YieldingItemPosition(Item item, int amount) {
        super(item, amount);
    }

    public YieldingItemPosition(Item item, int amount, TotalValue purchaseValue) {
        super(item, amount, purchaseValue);
    }

    public YieldingItemPosition(Item item, int amount, Price purchasePrice) {
        super(item, amount, purchasePrice);
    }

    public void add(YieldingItemPosition other) {
        super.add(other);
        realisedProfit = other.realisedProfit;
    }

    @Override
    public void removeItems(int amount, TotalValue totalValue) {
        removeItems(amount, totalValue.toPrice(amount));
    }

    @Override
    public void removeItems(int amount, Price pricePerUnit) {
        realisedProfit = realisedProfit.add(pricePerUnit.subtract(getPurchasePrice()).toTotalValue(amount));
        super.removeItems(amount, pricePerUnit);
    }
}