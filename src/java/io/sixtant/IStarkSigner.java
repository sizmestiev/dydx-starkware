package io.sixtant;

public interface IStarkSigner {
    String sign(
            boolean isTestnet,
            long positionId,
            CharSequence clientId,
            CharSequence symbol,
            CharSequence side,
            CharSequence size,
            CharSequence price,
            CharSequence limitFee,
            CharSequence expiration,
            CharSequence orderType,
            boolean postOnly,
            CharSequence timeInForce
    );
}
