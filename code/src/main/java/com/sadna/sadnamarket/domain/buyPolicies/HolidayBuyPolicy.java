package com.sadna.sadnamarket.domain.buyPolicies;

import com.kosherjava.zmanim.hebrewcalendar.JewishCalendar;
import com.kosherjava.zmanim.hebrewcalendar.JewishDate;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.users.MemberDTO;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HolidayBuyPolicy extends SimpleBuyPolicy{

    HolidayBuyPolicy(int id, List<BuyType> buytypes, PolicySubject subject) {
        super(id, buytypes, subject);
    }

    public HolidayBuyPolicy() {
    }

    @Override
    public boolean canBuy(List<CartItemDTO> cart, Map<Integer, ProductDTO> products, MemberDTO user) {
        if(policySubject.subjectAmount(cart, products) > 0) {
            return !isHoliday();
        }
        return true;
    }

    public boolean isHoliday() {
        JewishCalendar jewishCalendar = new JewishCalendar(LocalDate.now());
        return jewishCalendar.isRoshHashana() ||
                jewishCalendar.isYomKippur() ||
                jewishCalendar.isSuccos() ||
                jewishCalendar.isSimchasTorah() ||
                jewishCalendar.isChanukah() ||
                jewishCalendar.isPurim() ||
                jewishCalendar.isPesach() ||
                jewishCalendar.isShavuos();
    }
}
