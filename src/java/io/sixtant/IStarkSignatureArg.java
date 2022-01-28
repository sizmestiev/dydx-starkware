package io.sixtant;

public interface IStarkSignatureArg {

    long positionId();

    CharSequence clientId();

    CharSequence symbol();

    CharSequence side();

    CharSequence size();

    CharSequence price();

    CharSequence limitFee();

    CharSequence expiration();

    CharSequence orderType();

    boolean postOnly();

    CharSequence timeInForce();
}
