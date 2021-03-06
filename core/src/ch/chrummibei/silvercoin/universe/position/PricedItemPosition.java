package ch.chrummibei.silvercoin.universe.position;

import ch.chrummibei.silvercoin.universe.credit.Price;
import ch.chrummibei.silvercoin.universe.credit.TotalValue;
import ch.chrummibei.silvercoin.universe.item.Item;

/**
 * An amount of positions that can be held by a trader
 */
public class PricedItemPosition extends ItemPosition {
    private final TotalValue purchaseValue = new TotalValue(0);

    public PricedItemPosition(Item item) {
        this(item, 0, new TotalValue(0.0));
    }

    public PricedItemPosition(Item item, int amount) {
        this(item, amount, new TotalValue(0.0));
    }

    public PricedItemPosition(Item item, int amount, TotalValue purchaseValue) {
        super(item, amount);
        increasingPosition(amount, purchaseValue);
    }

    public PricedItemPosition(Item item, int amount, Price purchasePrice) {
        super(item, amount);
        increasingPosition(amount, purchasePrice.toTotalValue(amount));
    }


    public void add(PricedItemPosition other) {
        addItems(other.amount, other.getPurchaseValue());
    }

    /**
     * Add to the positions, increasing the purchase value by unit.
     * @param amount The amount of positions to add to the positions.
     * @param pricePerUnit The price of one unit of item.
     */
    public void addItems(int amount, Price pricePerUnit) {
        addItems(amount, pricePerUnit.toTotalValue(amount));
    }

    /**
     * Add to the positions, increasing the purchase value by the given value.
     * @param amount The amount of positions to add to the positions.
     * @param totalValue The total value of the added positions.
     */
    public void addItems(int amount, TotalValue totalValue) {
        if (amount == 0) throw new RuntimeException("addItems was called with amount to add = 0. This is a bug.");

        if (this.amount == 0) {
            increasingPosition(amount, totalValue);
        } else if (Math.signum(this.amount) == Math.signum(amount)) {
            increasingPosition(amount, totalValue);
        } else if (Math.abs(amount) < Math.abs(this.amount)) {
            decreasingPosition(amount, totalValue);
        } else if (this.amount == -amount) {
            zeroingPosition(amount, totalValue);
        } else {
            flippingPosition(amount, totalValue);
        }

        super.addItems(amount);
    }

    // Is only called when amount != 0
    void flippingPosition(int amount, TotalValue totalValue) {
        int decreasingAmount = -this.amount;
        int increasingAmount = amount+this.amount;

        TotalValue decreasingValue = totalValue.toPrice(amount).toTotalValue(decreasingAmount);
        TotalValue increasingValue = totalValue.toPrice(amount).toTotalValue(increasingAmount);

        zeroingPosition(decreasingAmount, decreasingValue);
        increasingPosition(increasingAmount, increasingValue);
    }

    void zeroingPosition(int amount, TotalValue totalValue) {
        purchaseValue.set(0);
    }

    void decreasingPosition(int amount, TotalValue totalValue) {
        // Decrease has no effect on purchase Price
    }

    void increasingPosition(int amount, TotalValue totalValue) {
        purchaseValue.iAdd(totalValue);
    }

    public void removeItems(int amount) {
        if (amount == 0) throw new RuntimeException("Cannot remove " + amount + " positions from empty position.");
        this.addItems(-amount, purchaseValue.toPrice(this.amount).toTotalValue(-amount));
    }

    /**
     * Remove the given amount of positions worth the given price. This will influence the realised profit.
     * @param amount The amount to reduce the positions by.
     * @param pricePerUnit The price of one unit of removed item.
     */
    public void removeItems(int amount, Price pricePerUnit) {
        addItems(-amount, pricePerUnit.toTotalValue(-amount));
    }

    /**
     * Remove the given amount of positions worth the given price. This will influence the realised profit.
     * @param amount The amount to reduce the positions by.
     * @param totalValue The positive total value of the removed positions.
     */
    public void removeItems(int amount, TotalValue totalValue) {
        addItems(-amount, totalValue.invert());
    }

    /**
     * The purchase price of one item.
     * @return Price per one item.
     */
    public Price getPurchasePrice() {
        return purchaseValue.toPrice(getAmount());
    }

    /**
     * The purchase value of the whole amount of this item currently in stock.
     * @return TotalValue of all owned positions.
     */
    public TotalValue getPurchaseValue() {
        return purchaseValue;
    }

}
