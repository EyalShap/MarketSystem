package com.sadna.sadnamarket.domain.buyPolicies;

import com.kosherjava.zmanim.hebrewcalendar.JewishCalendar;
import com.kosherjava.zmanim.hebrewcalendar.JewishDate;
import com.sadna.sadnamarket.domain.products.ProductDTO;
import com.sadna.sadnamarket.domain.users.CartItemDTO;
import com.sadna.sadnamarket.domain.users.MemberDTO;
import com.sadna.sadnamarket.service.Error;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class HolidayBuyPolicy extends SimpleBuyPolicy{

    public HolidayBuyPolicy(int id, List<BuyType> buytypes, PolicySubject subject) {
        super(id, buytypes, subject);
    }

    public HolidayBuyPolicy() {
    }

    @Override
    public Set<String> canBuy(List<CartItemDTO> cart, Map<Integer, ProductDTO> products, MemberDTO user) {
        Set<String> error = new HashSet<>();
        if(policySubject.get(0).subjectAmount(cart, products) > 0) {
            if(isHoliday()) {
                error.add(Error.makeHolidayBuyPolicyError(policySubject.get(0).getSubject()));
            }
        }
        return error;
    }

    public static boolean isHoliday() {
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

    @Override
    protected boolean dependsOnUser() {
        return false;
    }

    @Override
    public String getPolicyDesc() {
        return String.format("%s can not be bought on a holiday.", policySubject.get(0).getDesc());
    }

}
