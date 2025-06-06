package com.example.normalizer.service;

import com.example.normalizer.model.bet_settlement.BetSettlement;
import com.example.normalizer.model.bet_settlement.FinalResult;
import com.example.normalizer.model.input.ProviderAlphaRequest;
import com.example.normalizer.model.input.ProviderBetaRequest;
import com.example.normalizer.model.odds_change.OddsChange;
import com.example.normalizer.model.odds_change.OddsChangeOdds;
import com.example.normalizer.queue.MessageQueue;
import com.example.normalizer.queue.exception.MessageDeliveryException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MessageNormalizerService {

    private static final Logger log = LoggerFactory.getLogger(MessageNormalizerService.class);

    private final MessageQueue queue;

    private static final String PROVIDER_ALPHA_ODDS_UPDATE_MSG_TYPE = "odds_update";
    private static final String PROVIDER_ALPHA_SETTLEMENT_MSG_TYPE = "settlement";
    private static final String PROVIDER_BETA_ODDS_UPDATE_MSG_TYPE = "ODDS";
    private static final String PROVIDER_BETA_SETTLEMENT_MSG_TYPE = "SETTLEMENT";

    private static final String RESULT_ALPHA_HOME = "1";
    private static final String RESULT_ALPHA_DRAW = "X";
    private static final String RESULT_ALPHA_AWAY = "2";
    private static final String RESULT_BETA_HOME = "home";
    private static final String RESULT_BETA_DRAW = "draw";
    private static final String RESULT_BETA_AWAY = "away";

    public MessageNormalizerService(MessageQueue queue) {
        this.queue = queue;
    }

    public void processProviderAlpha(ProviderAlphaRequest request) {
        String msgType = request.getMsgType();
        String eventId = request.getEventId();

        if (PROVIDER_ALPHA_ODDS_UPDATE_MSG_TYPE.equalsIgnoreCase(msgType)) {
            try {
                OddsChange msg = new OddsChange();
                msg.setEventId(eventId);
                OddsChangeOdds odds = new ObjectMapper().convertValue(request.getOdds(), OddsChangeOdds.class);
                msg.setOdds(odds);

                queue.send(msg);
            } catch (IllegalArgumentException e) {
                log.debug("Invalid odds data: {}", e.getMessage());
                throw new IllegalArgumentException("Invalid odds data: " + e.getMessage());
            } catch (MessageDeliveryException e) {
                log.error("Failed to send message to queue: {}", e.getMessage());
                throw new RuntimeException("Failed to send message to queue: " + e.getMessage());
            }

        } else if (PROVIDER_ALPHA_SETTLEMENT_MSG_TYPE.equalsIgnoreCase(msgType)) {
            try {
                String outcome = request.getOutcome();
                if (!RESULT_ALPHA_HOME.equals(outcome) && !RESULT_ALPHA_DRAW.equals(outcome) && !RESULT_ALPHA_AWAY.equals(outcome)) {
                    log.debug("Invalid match result: {}", outcome);
                }
                String matchResult = mapAlphaOutcomeToMatchResult(outcome);
                BetSettlement msg = new BetSettlement(eventId, matchResult);
                queue.send(msg);
            } catch (IllegalArgumentException e) {
                log.debug("Invalid settlement data: {}", e.getMessage());
                throw new IllegalArgumentException("Invalid settlement data: " + e.getMessage());
            } catch (MessageDeliveryException e) {
                log.error("Failed to send message to queue: {}", e.getMessage());
                throw new RuntimeException("Failed to send message to queue: " + e.getMessage());
            }
        }

    }

    private String mapAlphaOutcomeToMatchResult(String outcome) {
        return switch (outcome) {
            case RESULT_ALPHA_HOME -> FinalResult.HOME.toString();
            case RESULT_ALPHA_DRAW -> FinalResult.DRAW.toString();
            case RESULT_ALPHA_AWAY -> FinalResult.AWAY.toString();
            default -> throw new IllegalArgumentException("Invalid outcome: " + outcome);
        };
    }

    public void processProviderBeta(ProviderBetaRequest request) {
        String type = request.getMsgType();
        String eventId = request.getEventId();

        if (type == null || eventId == null) {
            throw new IllegalArgumentException("Missing required fields: msg_type or event_id");
        }

        if (PROVIDER_BETA_ODDS_UPDATE_MSG_TYPE.equalsIgnoreCase(type)) {
            try {
                OddsChangeOdds odds = new ObjectMapper().convertValue(request.getOdds(), OddsChangeOdds.class);
                OddsChange msg = new OddsChange(eventId, odds);
                queue.send(msg);
            } catch (IllegalArgumentException e) {
                log.debug("Invalid odds data: {}", e.getMessage());
                throw new IllegalArgumentException("Invalid odds data: " + e.getMessage());
            } catch (MessageDeliveryException e) {
                log.error("Failed to send message to queue: {}", e.getMessage());
                throw new RuntimeException("Failed to send message to queue: " + e.getMessage());
            }

        } else if (PROVIDER_BETA_SETTLEMENT_MSG_TYPE.equalsIgnoreCase(type)) {
            String result = request.getResult();

            if (!RESULT_BETA_HOME.equalsIgnoreCase(result) &&
                    !RESULT_BETA_DRAW.equalsIgnoreCase(result) &&
                    !RESULT_BETA_AWAY.equalsIgnoreCase(result)) {
                log.debug("Invalid match result: {}", result);
                throw new IllegalArgumentException("Invalid match result: " + result);
            }

            BetSettlement msg = new BetSettlement(eventId, result);
            try {
                queue.send(msg);
            } catch (MessageDeliveryException e) {
                log.error("Failed to send message to queue: {}", e.getMessage());
                throw new RuntimeException("Failed to send message to queue: " + e.getMessage());
            }
        }
    }
}
