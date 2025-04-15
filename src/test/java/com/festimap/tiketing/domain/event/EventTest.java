package com.festimap.tiketing.domain.event;

import com.festimap.tiketing.domain.event.dto.*;
import com.festimap.tiketing.global.error.ErrorCode;
import com.festimap.tiketing.global.error.exception.BaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@SuppressWarnings("NonAsciiCharacters")
public class EventTest {

    private Event event;
    private EventCreateReqDto eventCreateReqDto;

    @BeforeEach
    void setUp() {
        eventCreateReqDto = new EventCreateReqDto();
        setField(eventCreateReqDto,"festivalId",1L);
        setField(eventCreateReqDto,"name","이벤트1");
        setField(eventCreateReqDto,"openAt", LocalDateTime.now().plusMinutes(5));
        setField(eventCreateReqDto,"totalTickets",10);
        setField(eventCreateReqDto,"prefix","MW");

        event = Event.from(eventCreateReqDto);
    }

    @Test
    void 정적팩토리메서드_생성_테스트(){
        //when
        Event event1 = Event.from(eventCreateReqDto);

        //then
        assertThat(event1).isNotNull();
        assertThat(event1.getFestivalId()).isEqualTo(1L);
        assertThat(event1.getOpenAt()).isAfter(LocalDateTime.now());
        assertThat(event1.getTotalTickets()).isEqualTo(10);
        assertThat(event1.getPrefix()).isEqualTo("MW");
        assertThat(event1.isFinished()).isFalse();
        assertThat(event1.isDeleted()).isFalse();
    }

    @Test
    void remainingTickets_필드_감소_테스트(){
        //when
        event.decreaseRemainingTickets(2);

        //then
        assertThat(event.getRemainingTickets()).isEqualTo(8);
        assertThat(event.getTotalTickets()).isEqualTo(10);
    }

    @Test
    void remainingTickets_필드_감소_실패_테스트(){
        //when
        assertThatThrownBy(() -> event.decreaseRemainingTickets(11))
                .isInstanceOf(BaseException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.TICKET_SOLD_OUT);
    }

    @Test
    void event_정보_전체_수정_테스트(){
        //given
        EventInfoUpdateDto eventInfoUpdateDto = new EventInfoUpdateDto();
        setField(eventInfoUpdateDto,"name","수정된 이름");
        setField(eventInfoUpdateDto,"openAt",LocalDateTime.now().plusMinutes(10));
        setField(eventInfoUpdateDto,"totalTickets",20);
        setField(eventInfoUpdateDto,"prefix","AB");

        //when
        event.updateEventInfo(eventInfoUpdateDto);

        //then
        assertThat(event).isNotNull();
        assertThat(event.getName()).isEqualTo("수정된 이름");
        assertThat(event.getFestivalId()).isEqualTo(1L);
        assertThat(event.getOpenAt()).isAfter(LocalDateTime.now().plusMinutes(5));
        assertThat(event.getTotalTickets()).isEqualTo(20);
        assertThat(event.getPrefix()).isEqualTo("AB");
    }

    @Test
    void event_정보_수정_실패_테스트(){
        //given
        setField(event,"openAt",LocalDateTime.now().minusMinutes(10));
        EventInfoUpdateDto eventInfoUpdateDto = new EventInfoUpdateDto();
        setField(eventInfoUpdateDto,"name","수정된 이름");
        setField(eventInfoUpdateDto,"openAt",LocalDateTime.now().plusMinutes(10));
        setField(eventInfoUpdateDto,"totalTickets",20);
        setField(eventInfoUpdateDto,"prefix","AB");

        //when & then
        assertThatThrownBy(() -> event.updateEventInfo(eventInfoUpdateDto))
                .isInstanceOf(BaseException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.EVENT_ALREADY_OPEN);
    }

    @Test
    void event_이름_수정_테스트(){
        //given
        EventNameUpdateDto eventNameUpdateDto = new EventNameUpdateDto();
        setField(eventNameUpdateDto,"name","수정된 이름");

        //when
        event.updateName(eventNameUpdateDto);

        //then
        assertThat(event.getName()).isEqualTo("수정된 이름");
    }

    @Test
    void event_전체티켓수_증가_테스트(){
        // given
        EventTotalTicketsUpdateDto eventTotalTicketsUpdateDto = new EventTotalTicketsUpdateDto();
        setField(eventTotalTicketsUpdateDto,"increaseAmount",20);

        // when
        event.increaseTotalTickets(eventTotalTicketsUpdateDto);

        // then
        assertThat(event.getTotalTickets()).isEqualTo(30);
    }

    @Test
    void event_isFinished_변경_성공_테스트(){
        // given
        EventIsFinishedUpdateDto eventIsFinishedUpdateDto = new EventIsFinishedUpdateDto();
        setField(eventIsFinishedUpdateDto,"isFinished",true);

        // when
        event.updateIsFinished(eventIsFinishedUpdateDto);

        // then
        assertThat(event.isFinished()).isTrue();
    }

    @Test
    void event_isFinished_변경_실패_테스트(){
        //given
        setField(event,"remainingTickets",0);
        EventIsFinishedUpdateDto eventIsFinishedUpdateDto = new EventIsFinishedUpdateDto();
        setField(eventIsFinishedUpdateDto,"isFinished",false);

        //when & then
        assertThatThrownBy(() -> event.updateIsFinished(eventIsFinishedUpdateDto))
                .isInstanceOf(BaseException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.NO_REMAINING_TICKETS);
    }

    @Test
    void event_softDeleted_테스트(){
        //wehn
        event.softDelete();

        //then
        assertThat(event.isDeleted()).isTrue();
    }
}
