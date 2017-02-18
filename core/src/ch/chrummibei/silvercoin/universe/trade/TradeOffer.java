package ch.chrummibei.silvercoin.universe.trade;

import ch.chrummibei.silvercoin.universe.credit.InvalidPriceException;
import ch.chrummibei.silvercoin.universe.credit.Price;
import ch.chrummibei.silvercoin.universe.credit.TotalValue;
import ch.chrummibei.silvercoin.universe.item.Item;

/**
 * A TradeOffer is a potential Trade offered by a Trader.
 */
public class TradeOffer {
    public enum TYPE {
        BUYING("Buy", "Buying", "is buying"), SELLING("Sell", "Selling", "is selling");

        TYPE(final String shortString,
             final String longString,
             final String verb) {
            this.shortString = shortString;
            this.longString = longString;
            this.verb = verb;
        }
        public String toString() { return this.longString; }

        public final String shortString; /* The string representation of this type */
        public final String longString; /* The string representation of this type */
        public final String verb; /* The string representation of this type */

        public TYPE opposite() { if (this == BUYING) return SELLING; else return BUYING; }
    }

    private final Trader offeringTrader;
    private final Item item;
    private final TYPE type;
    private int amount;
    private Price price;
    private Trade resultingTrade = null;

    public TradeOffer(Trader offeringTrader, Item item, TYPE type, int amount, Price price) {
        this.offeringTrader = offeringTrader;
        this.item = item;
        this.type = type;
        this.amount = amount;
        this.price = price;
    }


    public boolean isSelling() {
        return type == TYPE.SELLING;
    }

    public boolean isBuying() {
        return type == TYPE.BUYING;
    }

    public Trader getOfferingTrader() {
        return offeringTrader;
    }

    public int getAmount() {
        return amount;
    }

    public void updateAmount(int amount, Price price) { // Same price
        this.amount = amount;
        this.price = price;
    }

    public void addAmount(int amount, Price price) {
        if (amount == 0) throw new RuntimeException("Trying to divide by 0");
        try {
            this.price = this.price.toTotalValue(this.amount).add(price.toTotalValue(amount)).toPrice(this.amount + amount);
        } catch (InvalidPriceException e) {
            e.printStackTrace();
        }
        this.amount += amount;
    }

    public Item getItem() {
        return item;
    }

    public TYPE getType() {
        return type;
    }

    public Price getPrice() {
        return price;
    }

    public double getSignedPriceDouble() {
        if (isBuying()) { // If buying, the highest prices are best. So we need to order descending.
            return -getPrice().toDouble();
        } else {
            return getPrice().toDouble();
        }
    }

    public TotalValue getTotalValue() {
        return price.toTotalValue(amount);
    }

    public String toString() {
        return offeringTrader + " " + type.verb + " " + item.getName() + " for " + price;
    }

    public String compactString() {
        return price + " (" + offeringTrader + ")";
    }


    public void accept(Trader acceptingTrader, int amount) throws TradeOfferHasNotEnoughAmountLeft {
        if (this.amount < amount) {
            throw new TradeOfferHasNotEnoughAmountLeft();
        }

        // The offer decreases its amount that is offered.
        this.amount -= amount;

        resultingTrade = toTrade(acceptingTrader, amount);
        try {
            acceptingTrader.executeTrade(resultingTrade);
            offeringTrader.executeTrade(resultingTrade);
            offeringTrader.offerAccepted(this);
        } catch (TraderNotInvolvedException e) {
            // Should never happen. This is a bug
            throw new AssertionError("Created a trade where the wrong trader was set.");
        }
    }

    public Trade toTrade(Trader acceptingTrader, int amount) {
        if (amount == 0) throw new RuntimeException("Trading amount=0. This is a bug.");

        if (isBuying()) {
            // offer is to buy, so the offering Trader is buying
            return new Trade(acceptingTrader, offeringTrader, item, amount, price);
        } else {
            return new Trade(offeringTrader, acceptingTrader, item, amount, price);
        }
    }
}