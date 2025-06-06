package com.example.normalizer.service;

import com.example.normalizer.model.bet_settlement.BetSettlement;
import com.example.normalizer.model.input.ProviderAlphaOdds;
import com.example.normalizer.model.input.ProviderAlphaRequest;
import com.example.normalizer.model.input.ProviderBetaOdds;
import com.example.normalizer.model.input.ProviderBetaRequest;
import com.example.normalizer.model.odds_change.OddsChange;
import com.example.normalizer.model.odds_change.OddsChangeOdds;
import com.example.normalizer.queue.MessageQueue;
import com.example.normalizer.queue.exception.MessageDeliveryException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class MessageNormalizerServiceTest {

    @Test
    void processProviderAlphaOddsUpdate_happyPath() {
        MessageQueue queue = mock(MessageQueue.class);
        MessageNormalizerService service = new MessageNormalizerService(queue);

        ProviderAlphaRequest req = new ProviderAlphaRequest();
        req.setMsgType("odds_update");
        req.setEventId("42");
        req.setOdds(new ProviderAlphaOdds(1.1, 2.2, 3.3));

        service.processProviderAlpha(req);

        ArgumentCaptor<OddsChange> captor = ArgumentCaptor.forClass(OddsChange.class);
        verify(queue).send(captor.capture());
        OddsChange sent = captor.getValue();
        assertEquals("42", sent.getEventId());
        assertNotNull(sent.getOdds());
        assertEquals(1.1, sent.getOdds().getHomeWin());
        assertEquals(2.2, sent.getOdds().getDraw());
        assertEquals(3.3, sent.getOdds().getAwayWin());
    }

    @Test
    void processProviderAlphaSettlement_happyPath() {
        MessageQueue queue = mock(MessageQueue.class);
        MessageNormalizerService service = new MessageNormalizerService(queue);

        ProviderAlphaRequest req = new ProviderAlphaRequest();
        req.setMsgType("settlement");
        req.setEventId("99");
        req.setOutcome("1");

        service.processProviderAlpha(req);

        ArgumentCaptor<BetSettlement> captor = ArgumentCaptor.forClass(BetSettlement.class);
        verify(queue).send(captor.capture());
        BetSettlement sent = captor.getValue();
        assertEquals("99", sent.getEventId());
        assertEquals("home", sent.getMatchResult());
    }

    @Test
    void processProviderAlpha_invalidOutcome_throwsException() {
        MessageQueue queue = mock(MessageQueue.class);
        MessageNormalizerService service = new MessageNormalizerService(queue);

        ProviderAlphaRequest req = new ProviderAlphaRequest();
        req.setMsgType("settlement");
        req.setEventId("1");
        req.setOutcome("bad");

        assertThrows(IllegalArgumentException.class, () -> service.processProviderAlpha(req));
        verify(queue, never()).send(any());
    }

    @Test
    void processProviderAlpha_oddsUpdate_invalidOddsData() {
        MessageQueue queue = mock(MessageQueue.class);
        MessageNormalizerService service = new MessageNormalizerService(queue);

        ProviderAlphaRequest req = new ProviderAlphaRequest();
        req.setMsgType("odds_update");
        req.setEventId("5");
        // odds will be converted with ObjectMapper which we mock to throw

        try (MockedConstruction<ObjectMapper> mocked = mockConstruction(ObjectMapper.class,
                (mock, context) -> when(mock.convertValue(any(), eq(OddsChangeOdds.class)))
                        .thenThrow(new IllegalArgumentException("bad data")))) {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> service.processProviderAlpha(req));
            assertTrue(ex.getMessage().contains("Invalid odds data"));
        }

        verify(queue, never()).send(any());
    }

    @Test
    void processProviderAlpha_oddsUpdate_queueFailure() {
        MessageQueue queue = mock(MessageQueue.class);
        doThrow(new MessageDeliveryException("fail")).when(queue).send(any());
        MessageNormalizerService service = new MessageNormalizerService(queue);

        ProviderAlphaRequest req = new ProviderAlphaRequest();
        req.setMsgType("odds_update");
        req.setEventId("5");
        req.setOdds(new ProviderAlphaOdds(1,2,3));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.processProviderAlpha(req));
        assertTrue(ex.getMessage().contains("Failed to send message to queue"));
    }

    @Test
    void processProviderAlpha_settlement_queueFailure() {
        MessageQueue queue = mock(MessageQueue.class);
        doThrow(new MessageDeliveryException("fail")).when(queue).send(any());
        MessageNormalizerService service = new MessageNormalizerService(queue);

        ProviderAlphaRequest req = new ProviderAlphaRequest();
        req.setMsgType("settlement");
        req.setEventId("5");
        req.setOutcome("1");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.processProviderAlpha(req));
        assertTrue(ex.getMessage().contains("Failed to send message to queue"));
    }

    @Test
    void processProviderBetaOddsUpdate_happyPath() {
        MessageQueue queue = mock(MessageQueue.class);
        MessageNormalizerService service = new MessageNormalizerService(queue);

        ProviderBetaRequest req = new ProviderBetaRequest();
        req.setMsgType("ODDS");
        req.setEventId("200");
        req.setOdds(new ProviderBetaOdds(1.2, 2.3, 3.4));

        service.processProviderBeta(req);

        ArgumentCaptor<OddsChange> captor = ArgumentCaptor.forClass(OddsChange.class);
        verify(queue).send(captor.capture());
        OddsChange sent = captor.getValue();
        assertEquals("200", sent.getEventId());
        assertNotNull(sent.getOdds());
        assertEquals(1.2, sent.getOdds().getHomeWin());
        assertEquals(2.3, sent.getOdds().getDraw());
        assertEquals(3.4, sent.getOdds().getAwayWin());
    }

    @Test
    void processProviderBetaSettlement_happyPath() {
        MessageQueue queue = mock(MessageQueue.class);
        MessageNormalizerService service = new MessageNormalizerService(queue);

        ProviderBetaRequest req = new ProviderBetaRequest();
        req.setMsgType("SETTLEMENT");
        req.setEventId("201");
        req.setResult("away");

        service.processProviderBeta(req);

        ArgumentCaptor<BetSettlement> captor = ArgumentCaptor.forClass(BetSettlement.class);
        verify(queue).send(captor.capture());
        BetSettlement sent = captor.getValue();
        assertEquals("201", sent.getEventId());
        assertEquals("away", sent.getMatchResult());
    }

    @Test
    void processProviderBeta_missingFields() {
        MessageQueue queue = mock(MessageQueue.class);
        MessageNormalizerService service = new MessageNormalizerService(queue);

        ProviderBetaRequest req = new ProviderBetaRequest();
        req.setEventId(null);
        req.setMsgType("ODDS");

        assertThrows(IllegalArgumentException.class, () -> service.processProviderBeta(req));
        verify(queue, never()).send(any());
    }

    @Test
    void processProviderBeta_invalidResult() {
        MessageQueue queue = mock(MessageQueue.class);
        MessageNormalizerService service = new MessageNormalizerService(queue);

        ProviderBetaRequest req = new ProviderBetaRequest();
        req.setMsgType("SETTLEMENT");
        req.setEventId("1");
        req.setResult("bad");

        assertThrows(IllegalArgumentException.class, () -> service.processProviderBeta(req));
        verify(queue, never()).send(any());
    }

    @Test
    void processProviderBeta_oddsUpdate_invalidOddsData() {
        MessageQueue queue = mock(MessageQueue.class);
        MessageNormalizerService service = new MessageNormalizerService(queue);

        ProviderBetaRequest req = new ProviderBetaRequest();
        req.setMsgType("ODDS");
        req.setEventId("1");

        try (MockedConstruction<ObjectMapper> mocked = mockConstruction(ObjectMapper.class,
                (mock, context) -> when(mock.convertValue(any(), eq(OddsChangeOdds.class)))
                        .thenThrow(new IllegalArgumentException("bad")))) {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> service.processProviderBeta(req));
            assertTrue(ex.getMessage().contains("Invalid odds data"));
        }

        verify(queue, never()).send(any());
    }

    @Test
    void processProviderBeta_oddsUpdate_queueFailure() {
        MessageQueue queue = mock(MessageQueue.class);
        doThrow(new MessageDeliveryException("fail")).when(queue).send(any());
        MessageNormalizerService service = new MessageNormalizerService(queue);

        ProviderBetaRequest req = new ProviderBetaRequest();
        req.setMsgType("ODDS");
        req.setEventId("1");
        req.setOdds(new ProviderBetaOdds(1,2,3));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.processProviderBeta(req));
        assertTrue(ex.getMessage().contains("Failed to send message to queue"));
    }

    @Test
    void processProviderBeta_settlement_queueFailure() {
        MessageQueue queue = mock(MessageQueue.class);
        doThrow(new MessageDeliveryException("fail")).when(queue).send(any());
        MessageNormalizerService service = new MessageNormalizerService(queue);

        ProviderBetaRequest req = new ProviderBetaRequest();
        req.setMsgType("SETTLEMENT");
        req.setEventId("1");
        req.setResult("home");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.processProviderBeta(req));
        assertTrue(ex.getMessage().contains("Failed to send message to queue"));
    }
}
