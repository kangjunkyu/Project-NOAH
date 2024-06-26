package com.noah.backend.domain.review.repository.custom;

import com.noah.backend.domain.comment.dto.responseDto.CommentGetDto;
import com.noah.backend.domain.comment.dto.responseDto.CommentListGetDto;
import com.noah.backend.domain.image.dto.requestDto.ImageGetDto;
import com.noah.backend.domain.image.entity.Image;
import com.noah.backend.domain.review.dto.responseDto.ReviewGetDto;
import com.noah.backend.domain.review.dto.responseDto.ReviewListGetDto;
import com.noah.backend.domain.review.entity.Review;
import com.noah.backend.domain.suggest.dto.requestDto.SuggestImageGetDto;
import com.noah.backend.domain.suggest.dto.responseDto.SuggestListResDto;
import com.noah.backend.global.exception.suggest.SuggestNotExists;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;

import static com.noah.backend.domain.comment.entity.QComment.comment;
import static com.noah.backend.domain.image.entity.QImage.image;
import static com.noah.backend.domain.review.entity.QReview.review;
import static com.querydsl.core.types.Projections.constructor;

@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory query;


    @Override
    public Optional<List<ReviewListGetDto>> getReviewList() {
        List<ReviewListGetDto> reviewDtos = query
            .select(constructor(ReviewListGetDto.class,
//                        review.id,
                                review.expense,
                                review.country,
                                review.people,
                                review.startDate,
                                review.endDate))
            .from(review)
            .fetch();

        return Optional.ofNullable(reviewDtos.isEmpty() ? null : reviewDtos);
    }


    @Override
    public Optional<ReviewGetDto> getReviewSelect(Long reviewId) {
        ReviewGetDto reviewDto = query
            .select(Projections.constructor(ReviewGetDto.class,
//                        review.id,
                                            review.expense,
                                            review.country,
                                            review.people,
                                            review.startDate,
                                            review.endDate))
            .from(review)
            .where(review.id.eq(reviewId))
            .fetchOne();

        if (reviewDto != null) {

            List<CommentListGetDto> commentDtos = query
                    .select(Projections.constructor(CommentListGetDto.class,
                            comment.id,
                            comment.member.id,
                            comment.content))
                    .from(comment)
                    .where(comment.review.id.eq(reviewId))
                    .fetch();

            List<ImageGetDto> imageDtos = query
                .select(Projections.constructor(ImageGetDto.class,
//                            image.id,
                                                image.url))
                .from(image)
                .where(image.review.id.eq(reviewId))
                .fetch();

            reviewDto.setCommentList(commentDtos);
            reviewDto.setImageList(imageDtos);
        }
        return Optional.ofNullable(reviewDto);
    }

    //랜덤한 리뷰 아이디를 제공
    @Override
    public Optional<List<Long>> getRandomSuggestId() {
        Optional<List<Long>> reviewIdList =
            Optional.ofNullable(query.select(review.id)
                                     .from(review)
                                     .fetch());
        return reviewIdList;
    }

    //인당 환산값보다 낮은 리뷰 한개를 제공
    @Override
    public Optional<SuggestListResDto> getSuggestReviewOne(int priceOfPerson) {
        return  Optional.ofNullable(query.select(
                        Projections.constructor(SuggestListResDto.class, review.id, review.expense, review.country, review.people,
                                review.startDate, review.endDate))
                .from(review)
                .where(review.expense.divide(review.people).loe(priceOfPerson))
                .orderBy(review.expense.divide(review.people).desc())
                .fetchFirst());
    }

    //인당 환산값보다 낮은 리뷰 여러개를 제공
    @Override
    public Optional<List<SuggestListResDto>> getSuggestReviewList(int priceOfPerson) {

        return Optional.ofNullable(query.select(
                                            Projections.constructor(SuggestListResDto.class, review.id, review.expense, review.country, review.people,
                                                                    review.startDate, review.endDate))
                                        .from(review)
                                        .where(review.expense.divide(review.people).loe(priceOfPerson))
                                        .orderBy(review.expense.divide(review.people).desc())
                                        .limit(30)
                                        .fetch());
    }

}
