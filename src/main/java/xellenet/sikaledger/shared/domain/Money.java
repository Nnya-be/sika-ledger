package xellenet.sikaledger.shared.domain;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

public record Money(BigDecimal amount, Currency currency) {

    public Money {
        Objects.requireNonNull(amount, "Amount cannot be null");
        Objects.requireNonNull(currency, "Currency cannot be null");

        int scale = currency.getDefaultFractionDigits();
        if(scale < 0) {
            scale = 0;
        }
        try{
            amount = amount.setScale(scale);
        }catch(ArithmeticException e){
           throw new IllegalArgumentException( String.format("%s has more than %d decimal places for %s",
                   amount, scale, currency.getCurrencyCode()),e);
        }
    }

    public static Money of(String amount, String currencyCode){
        return new Money( new BigDecimal(amount), Currency.getInstance(currencyCode));
    }

    public static Money of(BigDecimal amount, String currencyCode){
        return new Money(amount, Currency.getInstance(currencyCode));
    }
    public Money plus(Money other){
        requireSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money minus(Money other){
        requireSameCurrency(other);
        return new Money(this.amount.subtract(other.amount), this.currency);
    }

    public Money negate(){
        return new Money(this.amount.negate(), this.currency);
    }

    public boolean isZero(){
        return this.amount.signum() == 0;
    }

    /**Helper functions */
    private void requireSameCurrency(Money other){
        Objects.requireNonNull(other, "Money cannot be null");
        if(!this.currency.equals(other.currency)){
            throw new IllegalArgumentException(
                    "Currency Mismatch: " + this.currency + " vs " + other.currency
            );
        }
    }
}
