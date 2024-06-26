package com.noah.backend.domain.review.repository.custom;

import com.noah.backend.domain.review.dto.responseDto.ReviewGetDto;
import com.noah.backend.domain.review.dto.responseDto.ReviewListGetDto;
import com.noah.backend.domain.suggest.dto.responseDto.SuggestListResDto;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface ReviewRepositoryCustom {

    Optional<List<ReviewListGetDto>> getReviewList();

    Optional<ReviewGetDto> getReviewSelect(Long ReviewId);

    //랜덤한 리뷰 아이디를 제공
    Optional<List<Long>> getRandomSuggestId();

    //인당 환산값보다 낮은 리뷰 한개를 제공
    Optional<SuggestListResDto> getSuggestReviewOne(int priceOfPerson);
    //인당 환산값보다 낮은 리뷰 여러개를 제공
    Optional<List<SuggestListResDto>> getSuggestReviewList(int priceOfPerson);
}
