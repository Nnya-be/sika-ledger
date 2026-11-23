package xellenet.sikaledger.shared.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void createNewMoney() {
        Money money1 = Money.of("10", "GHS");
        assertEquals(new BigDecimal("10.00"), money1.amount());
    }

    @Test
    void rejectNullCurrency() {
        assertThrows(NullPointerException.class,
                () -> new Money(new BigDecimal("10.2"), null));
    }

    @Test
    void equalAmountsWithDifferentScalesAreEqual() {
        assertEquals(Money.of("10.5", "GHS"), Money.of("10.50", "GHS"));
    }

    @Test
    void rejectsTooManyDecimals() {
        assertThrows(IllegalArgumentException.class,
                () -> Money.of("10.005", "GHS"));
    }

    @Test
    void rejectsAddingDifferentCurrencies() {
        Money cedis = Money.of("10.00", "GHS");
        Money dollars = Money.of("5.00", "USD");
        assertThrows(IllegalArgumentException.class, () -> cedis.plus(dollars));
    }

    @Test
    void addsSameCurrency() {
        Money result = Money.of("10.00", "GHS").plus(Money.of("5.25", "GHS"));
        assertEquals(Money.of("15.25", "GHS"), result);
    }

    @Test
    void negateFlipsTheSign() {
        assertEquals(Money.of("-100", "GHS"), Money.of("100", "GHS").negate());
    }

    @Test
    void zeroIsZeroWhateverTheScale() {
        assertTrue(Money.of("0", "GHS").isZero());
    }
}