package com.wywin.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wywin.constant.ItemStatus;
import com.wywin.dto.ItemSearchDTO;
import com.wywin.entity.QSellingItem;
import com.wywin.entity.SellingItem;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

    private JPAQueryFactory queryFactory;

    public ItemRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    // 판매상태 조건 검색
    private BooleanExpression searchSellStatusEq(ItemStatus searchSellStatus){
        return searchSellStatus == null ? null : QSellingItem.sellingItem.itemStatus.eq(searchSellStatus);
        // 상품 판매 상태 조건이 전체(null)일 경우 null 리턴, where 절에서 해당 조건은 무시됨
        // null이 아니라 판매중, 품절 상태라면 해당 조건의 상품만 조회
    }

    // 날짜 조건 검색
    private BooleanExpression regDtsAfter(String searchDateType){   // 해당 시간 이후로 등록된 상품만 검색

        LocalDateTime dateTime = LocalDateTime.now();

        if(StringUtils.equals("all", searchDateType) || searchDateType == null){
            // 상품 등록일 전체
            return null;
        } else if(StringUtils.equals("1d", searchDateType)){    // 최근 하루 동안 등록된 상품
            dateTime = dateTime.minusDays(1);
        } else if(StringUtils.equals("1w", searchDateType)){    // 최근 일주일
            dateTime = dateTime.minusWeeks(1);
        } else if(StringUtils.equals("1m", searchDateType)){    // 최근 한달
            dateTime = dateTime.minusMonths(1);
        } else if(StringUtils.equals("6m", searchDateType)){    // 최근 6개월
            dateTime = dateTime.minusMonths(6);
        }

        return QSellingItem.sellingItem.regTime.after(dateTime);
    }

    // 상품명, 등록자 id 검색
    private BooleanExpression searchByLike(String searchBy, String searchQuery){

        if(StringUtils.equals("itemNm", searchBy)){     // 상품명
            return QSellingItem.sellingItem.sitemNm.like("%" + searchQuery + "%");
        } else if(StringUtils.equals("createdBy", searchBy)){       // 상품 등록자 id
            return QSellingItem.sellingItem.seller.like("%" + searchQuery + "%");
        }

        return null;
    }

    @Override
    public Page<SellingItem> getSellingItemPage(ItemSearchDTO itemSearchDto, Pageable pageable) {

        List<SellingItem> content = queryFactory
                .selectFrom(QSellingItem.sellingItem)             // 상품 데이터를 조회하기 위해서 Qitem의 item 지정
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),              // 조건 절 ,가 and 조건
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),    // 41 행에서 만든 조건
                        searchByLike(itemSearchDto.getSearchBy(),                   // 61 행에서 만든 조건
                                itemSearchDto.getSearchQuery()))
                .orderBy(QSellingItem.sellingItem.sid.desc())
                .offset(pageable.getOffset())                                       // 한번에 가지고올 시작 인덱스
                .limit(pageable.getPageSize())                                      // 한번에 가지고올 최대 개수
                .fetch();

        long total = queryFactory.select(Wildcard.count).from(QSellingItem.sellingItem)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .fetchOne()
                ;

        return new PageImpl<>(content, pageable, total);            // 페이징 처리되어 간다.
    }
}
