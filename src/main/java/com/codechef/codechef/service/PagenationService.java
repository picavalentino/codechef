package com.codechef.codechef.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class PagenationService {
    // 페이지 보여줄 개수
    private static final int BAR_LELGTH = 5;

    public List<Integer> getPaginationBarNumber(int currentPageNumber, int totalPageNumber){
        int startNumber = Math.max(currentPageNumber - (BAR_LELGTH/2),0);
        int endNumber = Math.min(startNumber + BAR_LELGTH, totalPageNumber);

        return IntStream.range(startNumber, endNumber).boxed().toList();
    }

    public int currentBarLength(){
        return BAR_LELGTH;
    }
}
