package com.festimap.tiketing.domain.ticket.utils;

import com.festimap.tiketing.domain.ticket.TicketingStrategy;
import com.festimap.tiketing.domain.ticket.util.TicketStrategyHolder;
import com.festimap.tiketing.global.error.ErrorCode;
import com.festimap.tiketing.global.error.exception.BaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SuppressWarnings("NonAsciiCharacters")
public class TicketStrategyHolderTest {

    private TicketStrategyHolder holder;

    @BeforeEach
    void setUp() throws Exception {
        holder = new TicketStrategyHolder();
    }

    @Test
    void 기본_전략은_LOCK_이어야_한다() {
        //then
        assertThat(holder.getStrategy()).isEqualTo(TicketingStrategy.LOCK);
    }

    @Test
    void queue_전략은_예외가_발생한다() {
        //when & then
        assertThatThrownBy(() -> holder.updateStrategy(TicketingStrategy.QUEUE))
                .isInstanceOf(BaseException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.NOT_SUPPORTED_STRATEGY);
    }

    @Test
    void lock_전략으로_수정시_성공(){
        //when
        holder.updateStrategy(TicketingStrategy.LOCK);

        //then
        assertThat(holder.getStrategy()).isEqualTo(TicketingStrategy.LOCK);
    }


}
