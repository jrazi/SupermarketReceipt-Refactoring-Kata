package dojo.supermarket.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Teller {

    private final SupermarketCatalog catalog;
    private Map<Product, Offer> offers = new HashMap<>();
    private Map<Product, Double> bundleOffers = new HashMap<>();

    public Teller(SupermarketCatalog catalog) {
        this.catalog = catalog;
    }

    public void addSpecialOffer(SpecialOfferType offerType, Product product, double argument) {
        this.offers.put(product, new Offer(offerType, product, argument));
    }

    public void addBundleOffer(Map<Product, Double> bundle){
        this.bundleOffers = bundle;
    }

    public Receipt checksOutArticlesFrom(ShoppingCart theCart) {
        return theCart.createReceipt(this.catalog, this.offers, this.bundleOffers);
    }

}
