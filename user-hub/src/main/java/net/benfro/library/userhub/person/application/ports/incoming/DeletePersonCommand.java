package net.benfro.library.userhub.person.application.ports.incoming;

import lombok.Builder;
import lombok.Getter;

@Builder
public record DeletePersonCommand (@Getter Long personId){
}
