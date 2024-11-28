package com.wywin.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wywin.constant.ItemStatus;
import com.wywin.dto.ItemSearchDTO;
import com.wywin.entity.Item;
import com.wywin.entity.QItem;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.*;
import org.springframework.data.repository.query.FluentQuery;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class AgencyRepositoryCustomImpl implements AgencyRepositoryCustom{

    private JPAQueryFactory queryFactory;  // 동적으로 쿼리를 생성 JPAQueryFactory란 JPA의 엔터티를 이용하여 JPQLQuery를 보다 쉽고 편리하게 작성할 수 있는 QueryDsl의 도구라 할 수 있다.

    public AgencyRepositoryCustomImpl(EntityManager em){
        // JPA를 사용하기 위해서는 Database 구조와 맵핑된 JPA Entity 들을 먼저 생성하게 된다.
        // 그리고, 모든 JPA의 동작은 이 Entity들을 기준으로 돌아가게 되는데, 이 때 Entity들을 관리하는 역할을 하는 녀석이 바로 EntityManager인 것이다.
        // 참고 https://velog.io/@juhyeon1114/JPA-%EC%98%81%EC%86%8D%EC%84%B1-%EC%BB%A8%ED%85%8D%EC%8A%A4%ED%8A%B8-%EC%9D%B4%ED%95%B4%ED%95%98%EA%B8%B0-w.-Entity-manager

        this.queryFactory = new JPAQueryFactory(em); // JPAQueryFactory생성자로 EntityManager 객체를 넣어 줌.
    }

    private BooleanExpression searchSellStatusEq(ItemStatus searchSellStatus){
        return searchSellStatus == null ? null : QItem.item.itemStatus.eq(searchSellStatus);
        // 상품 판매 상태 조건이 전체(null)일 경우 null 리턴, where 절에서 해당 조건은 무시됨
        // null이 아니라 판매중, 품절 상태라면 해당 조건의 상품만 조회
    }

    private BooleanExpression regDtsAfter(String searchDateType){

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

        return QItem.item.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){

        if(StringUtils.equals("itemNm", searchBy)){     // 상품명
            return QItem.item.itemNm.like("%" + searchQuery + "%");
        } else if(StringUtils.equals("createdBy", searchBy)){       // 상품 등록자 id
            return QItem.item.createdBy.like("%" + searchQuery + "%");
        }

        return null;
    }

    @Override
    public Page<Item> getItemPage(ItemSearchDTO itemSearchDto, Pageable pageable) {

        List<Item> content = queryFactory
                .selectFrom(QItem.item)             // 상품 데이터를 조회하기 위해서 Qitem의 item 지정
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),              // 조건 절 ,가 and 조건
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),    // 41 행에서 만든 조건
                        searchByLike(itemSearchDto.getSearchBy(),                   // 61 행에서 만든 조건
                                itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())                                       // 한번에 가지고올 시작 인덱스
                .limit(pageable.getPageSize())                                      // 한번에 가지고올 최대 개수
                .fetch();

        long total = queryFactory.select(Wildcard.count).from(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);            // 페이징 처리되어 간다.
    }




}
