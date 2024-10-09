package net.benfro.library.bookhub.event;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginEvent(@JsonProperty("consultantId") Long consultantId,
                         @JsonProperty("ipAddress") String ipAddress,
                         @JsonProperty("userAgent") String userAgent,
                         @JsonProperty("timestamp") Long timestamp) {
}
